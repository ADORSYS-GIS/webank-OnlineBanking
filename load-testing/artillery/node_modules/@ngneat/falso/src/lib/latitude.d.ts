import { FakeOptions } from './core/core';
/**
 * Generate a random latitude.
 *
 * @category address
 *
 * @example
 *
 * randLatitude()
 *
 * @example
 *
 * randLatitude({ length: 10 })
 *
 */
export declare function randLatitude<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<number, Options>;
