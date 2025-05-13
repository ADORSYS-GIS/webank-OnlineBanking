import { FakeOptions } from '../../core/core';
export interface Airport {
    name: string;
    code: string;
    city: string;
    country: string;
}
/**
 * Generate a random airport.
 *
 * @category flight
 *
 * @example
 *
 * randAirport()
 *
 * @example
 *
 * randAirport({ length: 10 })
 *
 */
export declare function randAirport<Options extends FakeOptions = never>(options?: Options): import("../../core/core").Return<{
    name: string;
    code: string;
    city: string;
    country: string;
}, Options>;
