import { FakeOptions } from './core/core';
/**
 * Generate a random locale.
 *
 * @category address
 *
 * @example
 *
 * randLocale()
 *
 * @example
 *
 * randLocale({ length: 10 })
 *
 */
export declare function randLocale<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
