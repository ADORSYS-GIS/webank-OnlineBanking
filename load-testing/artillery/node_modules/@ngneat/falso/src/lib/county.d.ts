import { FakeOptions } from './core/core';
/**
 * Generate a random county.
 *
 * @category Address
 *
 * @example
 *
 * randCounty()
 *
 * @example
 *
 * randCounty({ length: 10 })
 *
 */
export declare function randCounty<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
