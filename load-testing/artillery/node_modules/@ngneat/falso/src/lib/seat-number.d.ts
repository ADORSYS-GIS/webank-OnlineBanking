import { FakeOptions } from './core/core';
/**
 * Generate a random seat number.
 *
 * @category flight
 *
 * @example
 *
 * randSeatNumber()
 *
 * @example
 *
 * randSeatNumber({ length: 10 })
 *
 */
export declare function randSeatNumber<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
