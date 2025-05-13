import { FakeOptions } from '../../core/core';
/**
 * Generate a random city.
 *
 * @category Address
 *
 * @example
 *
 * randCity()
 *
 * @example
 *
 * randCity({ length: 10 })
 *
 */
export declare function randCity<Options extends FakeOptions = never>(options?: Options): import("../../core/core").Return<string, Options>;
