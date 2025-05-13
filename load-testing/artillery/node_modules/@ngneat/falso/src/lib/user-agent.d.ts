import { FakeOptions } from './core/core';
/**
 * Generate a random user agent.
 *
 * @category internet
 *
 * @example
 *
 * randUserAgent()
 *
 * @example
 *
 * randUserAgent({ length: 10 })
 *
 */
export declare function randUserAgent<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
