import { FakeOptions } from './core/core';
/**
 * Generate a random zip code.
 *
 * @category address
 *
 * @example
 *
 * randZipCode()
 *
 * @example
 *
 * randZipCode({ length: 10 })
 *
 */
export declare function randZipCode<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
