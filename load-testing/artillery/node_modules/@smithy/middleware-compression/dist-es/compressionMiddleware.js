import { setFeature } from "@smithy/core";
import { HttpRequest } from "@smithy/protocol-http";
import { compressStream } from "./compressStream";
import { compressString } from "./compressString";
import { CLIENT_SUPPORTED_ALGORITHMS } from "./constants";
import { isStreaming } from "./isStreaming";
export const compressionMiddleware = (config, middlewareConfig) => (next, context) => async (args) => {
    if (!HttpRequest.isInstance(args.request)) {
        return next(args);
    }
    const disableRequestCompression = await config.disableRequestCompression();
    if (disableRequestCompression) {
        return next(args);
    }
    const { request } = args;
    const { body, headers } = request;
    const { encodings, streamRequiresLength } = middlewareConfig;
    let updatedBody = body;
    let updatedHeaders = headers;
    for (const algorithm of encodings) {
        if (CLIENT_SUPPORTED_ALGORITHMS.includes(algorithm)) {
            let isRequestCompressed = false;
            if (isStreaming(body)) {
                if (!streamRequiresLength) {
                    updatedBody = await compressStream(body);
                    isRequestCompressed = true;
                }
                else {
                    throw new Error("Compression is not supported for streaming blobs that require a length.");
                }
            }
            else {
                const bodyLength = config.bodyLengthChecker(body);
                const requestMinCompressionSizeBytes = await config.requestMinCompressionSizeBytes();
                if (bodyLength && bodyLength >= requestMinCompressionSizeBytes) {
                    updatedBody = await compressString(body);
                    isRequestCompressed = true;
                }
            }
            if (isRequestCompressed) {
                if (headers["content-encoding"]) {
                    updatedHeaders = {
                        ...headers,
                        "content-encoding": `${headers["content-encoding"]}, ${algorithm}`,
                    };
                }
                else {
                    updatedHeaders = { ...headers, "content-encoding": algorithm };
                }
                if (updatedHeaders["content-encoding"].includes("gzip")) {
                    setFeature(context, "GZIP_REQUEST_COMPRESSION", "L");
                }
                break;
            }
        }
    }
    return next({
        ...args,
        request: {
            ...request,
            body: updatedBody,
            headers: updatedHeaders,
        },
    });
};
export const compressionMiddlewareOptions = {
    name: "compressionMiddleware",
    step: "build",
    tags: ["REQUEST_BODY_COMPRESSION", "GZIP"],
    override: true,
    priority: "high",
};
