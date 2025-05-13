import { FakeOptions } from './core/core';
/**
 * Generate a random street address.
 *
 * @category address
 *
 * @example
 *
 * randStreetAddress()
 *
 * @example
 *
 * randStreetAddress({ length: 10 })
 *
 */
export declare function randStreetAddress<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
