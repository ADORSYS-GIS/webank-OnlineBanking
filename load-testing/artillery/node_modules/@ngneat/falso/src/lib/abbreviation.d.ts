import { FakeOptions } from './core/core';
/**
 * Generate a random abbreviation.
 *
 * @category text
 *
 * @example
 *
 * randAbbreviation()
 *
 * @example
 *
 * randAbbreviation({ length: 10 })
 *
 */
export declare function randAbbreviation<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
