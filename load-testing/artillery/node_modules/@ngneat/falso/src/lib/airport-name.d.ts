import { FakeOptions } from './core/core';
/**
 * Generate a random airport name.
 *
 * @category flight
 *
 * @example
 *
 * randAirportName()
 *
 * @example
 *
 * randAirportName({ length: 10 })
 *
 */
export declare function randAirportName<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
