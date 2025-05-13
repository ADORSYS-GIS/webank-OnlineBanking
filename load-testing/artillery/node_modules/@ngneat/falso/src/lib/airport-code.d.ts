import { FakeOptions } from './core/core';
/**
 * Generate a random airport code.
 *
 * @category flight
 *
 * @example
 *
 * randAirportCode()
 *
 * @example
 *
 * randAirportCode({ length: 10 })
 *
 */
export declare function randAirportCode<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
