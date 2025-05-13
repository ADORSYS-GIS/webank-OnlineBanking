import { FakeOptions } from './core/core';
export interface AddressOptions extends FakeOptions {
    includeCounty?: boolean;
    includeCountry?: boolean;
}
export interface Address {
    street: string;
    city: string;
    zipCode: string;
    county?: string;
    country?: string;
}
/**
 * Generate a random address.
 *
 * @category entities, address
 *
 * @example
 *
 * randAddress()
 *
 * @example
 *
 * randAddress({ includeCounty: false }) // default is true
 *
 * @example
 *
 * randAddress({ includeCountry: false }) // default is true
 *
 * @example
 *
 * randAddress({ length: 10 })
 *
 */
export declare function randAddress<Options extends AddressOptions = never>(options?: Options): import("./core/core").Return<Address, Options>;
