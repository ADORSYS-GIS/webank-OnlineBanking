import { FakeOptions } from '../../core/core';
/**
 * Generate a random brand.
 *
 * @category company
 *
 * @example
 *
 * randBrand()
 *
 * @example
 *
 * randBrand({ length: 10 })
 *
 */
export declare function randBrand<Options extends FakeOptions = never>(options?: Options): import("../../core/core").Return<string, Options>;
