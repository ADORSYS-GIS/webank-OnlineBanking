import { FakeOptions } from './core/core';
/**
 * Generate a random credit card CVV.
 *
 * @category Finance
 *
 * @example
 *
 * randCreditCardCVV()
 *
 * @example
 *
 * randCreditCardCVV({ length: 10 })
 *
 */
export declare function randCreditCardCVV<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
