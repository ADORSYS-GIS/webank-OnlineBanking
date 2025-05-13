import { FakeOptions } from './core/core';
/**
 * Get a random alpha or numeric.
 *
 * @category text
 *
 * @example
 *
 * randAlphaNumeric()
 *
 * @example
 *
 * randAlphaNumeric({ length: 3 })
 *
 */
export declare function randAlphaNumeric<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string | number, Options>;
