import { FakeOptions } from './core/core';
import { CountryCode } from './country-code';
export interface IbanOptions extends FakeOptions {
    countryCode?: CountryCode;
}
/**
 * Generate a random IBAN number.
 *
 * @category finance
 *
 * @example
 *
 * randIban()
 *
 * @example
 *
 * randSwift({ countryCode: 'DE' }) // country code with ISO country code
 *
 * @example
 *
 * randIban({ length: 10 })
 *
 */
export declare function randIban<Options extends IbanOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
