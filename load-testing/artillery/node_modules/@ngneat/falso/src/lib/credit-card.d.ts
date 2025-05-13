import { FakeOptions } from './core/core';
import { Brand } from './credit-card-number';
interface CreditCardOptions extends FakeOptions {
    fullName?: string;
    brand?: Brand;
}
export interface CreditCard {
    ccv: string;
    brand: string;
    number: string;
    account: string;
    fullName: string;
    type: 'Credit' | 'Debit';
    validFrom: string;
    untilEnd: string;
}
/**
 * Generate a random credit card.
 *
 * @category finance, entities
 *
 * @example
 *
 * randCreditCard()
 *
 * @example
 *
 * randCreditCard({ fullName: 'Mr Ryan Smee' })
 *
 * @example
 *
 * randCreditCard({ brand: 'Visa' })
 *
 * @example
 *
 * randCreditCard({ length: 10 })
 *
 */
export declare function randCreditCard<Options extends CreditCardOptions = never>(options?: Options): import("./core/core").Return<CreditCard, Options>;
export {};
