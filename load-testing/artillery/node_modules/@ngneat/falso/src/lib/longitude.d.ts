import { FakeOptions } from './core/core';
/**
 * Generate a random longitude.
 *
 * @category address
 *
 * @example
 *
 * randLongitude()
 *
 * @example
 *
 * randLongitude({ length: 10 })
 *
 */
export declare function randLongitude<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<number, Options>;
