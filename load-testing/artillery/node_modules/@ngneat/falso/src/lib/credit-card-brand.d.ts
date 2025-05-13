import { FakeOptions } from './core/core';
/**
 * Generate a random credit card brand.
 *
 * @category Finance
 *
 * @example
 *
 * randCreditCardBrand()
 *
 * @example
 *
 * randCreditCardBrand({ length: 10 })
 *
 */
export declare function randCreditCardBrand<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
