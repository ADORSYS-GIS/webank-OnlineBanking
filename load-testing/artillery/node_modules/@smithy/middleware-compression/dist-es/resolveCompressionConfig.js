import { normalizeProvider } from "@smithy/util-middleware";
export const resolveCompressionConfig = (input) => {
    const { disableRequestCompression, requestMinCompressionSizeBytes: _requestMinCompressionSizeBytes } = input;
    return Object.assign(input, {
        disableRequestCompression: normalizeProvider(disableRequestCompression),
        requestMinCompressionSizeBytes: async () => {
            const requestMinCompressionSizeBytes = await normalizeProvider(_requestMinCompressionSizeBytes)();
            if (requestMinCompressionSizeBytes < 0 || requestMinCompressionSizeBytes > 10485760) {
                throw new RangeError("The value for requestMinCompressionSizeBytes must be between 0 and 10485760 inclusive. " +
                    `The provided value ${requestMinCompressionSizeBytes} is outside this range."`);
            }
            return requestMinCompressionSizeBytes;
        },
    });
};
