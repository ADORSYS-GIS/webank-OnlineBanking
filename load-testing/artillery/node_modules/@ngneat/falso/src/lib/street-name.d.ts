import { FakeOptions } from './core/core';
/**
 * Generate a random street name.
 *
 * @category address
 *
 * @example
 *
 * randStreetName()
 *
 * @example
 *
 * randStreetName({ length: 10 })
 *
 */
export declare function randStreetName<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
