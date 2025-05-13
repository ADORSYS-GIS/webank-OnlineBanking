import { FakeOptions } from './core/core';
import { CountryCode } from './country-code';
import { AZ09 } from './core/types';
export interface SwiftOptions extends FakeOptions {
    bankCode?: string;
    countryCode?: CountryCode;
    locationCode?: `${AZ09}${AZ09}`;
    branchCode?: `${AZ09}${AZ09}${AZ09}`;
    fillBranchCode?: boolean;
}
/**
 * Generate a random swift bic code.
 *
 * @category finance
 *
 * @example
 *
 * randSwift()
 *
 * @example
 *
 * randSwift({ bankCode: 'DEUT' }) // bank code of Deutsche Bank
 *
 * @example
 *
 * randSwift({ countryCode: 'DE' }) // country code with ISO country code
 *
 * @example
 *
 * randSwift({ locationCode: 'MM' }) // location code for Milan MM or for Frankfurt FF and etc
 *
 * @example
 *
 * randSwift({ branchCode: '250' }) // bank branch code
 *
 * @example
 *
 * randSwift({ fillBranchCode: true }) // bank branch code filled with XXX
 *
 * @example
 *
 * randSwift({ length: 10 })
 *
 */
export declare function randSwift<Options extends SwiftOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
