import { FakeOptions } from './core/core';
/**
 * Generate a random url.
 *
 * @category internet
 *
 * @example
 *
 * randUrl()
 *
 * @example
 *
 * randUrl({ length: 10 })
 *
 */
export declare function randUrl<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
