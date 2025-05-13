import { AddressOptions } from './address';
/**
 * Generate a random full address.
 *
 * @category Address
 *
 * @example
 *
 * randFullAddress()
 *
 * @example
 *
 * randFullAddress({ includeCounty: false }) // default is true
 *
 * @example
 *
 * randFullAddress({ includeCountry: false }) // default is true
 *
 * @example
 *
 * randFullAddress({ length: 10 })
 *
 */
export declare function randFullAddress<Options extends AddressOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
