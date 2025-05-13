import { FakeOptions } from './core/core';
interface CreditCardNumberOptions extends FakeOptions {
    brand?: Brand;
}
export type Brand = 'American Express' | 'UnionPay' | 'Diners Club' | 'Discover Card' | 'RuPay' | 'JCB' | 'Maestro' | 'Dankort' | 'Mastercard' | 'Visa' | 'Visa Electron' | 'UATP';
/**
 * Generate a random credit card number.
 *
 * @category finance
 *
 * @example
 *
 * randCreditCardNumber()
 *
 * @example
 *
 * randCreditCardNumber({ brand: 'Visa' })
 *
 * @example
 *
 * randCreditCardNumber({ length: 10 })
 *
 */
export declare function randCreditCardNumber<Options extends CreditCardNumberOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
export {};
