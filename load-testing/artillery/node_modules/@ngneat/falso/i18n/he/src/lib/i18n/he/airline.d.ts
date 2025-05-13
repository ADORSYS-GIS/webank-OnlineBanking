import { FakeOptions } from '../../core/core';
/**
 * Generate a random airline.
 *
 * @category flight
 *
 * @example
 *
 * randAirline()
 *
 * @example
 *
 * randAirline({ length: 10 })
 *
 */
export declare function randAirline<Options extends FakeOptions = never>(options?: Options): import("../../core/core").Return<string, Options>;
