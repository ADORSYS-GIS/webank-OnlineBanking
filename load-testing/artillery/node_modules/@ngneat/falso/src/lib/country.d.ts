import { FakeOptions } from './core/core';
/**
 * Generate a random country.
 *
 * @category address
 *
 * @example
 *
 * randCountry()
 *
 * @example
 *
 * randCountry({ length: 10 })
 *
 */
export declare function randCountry<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
