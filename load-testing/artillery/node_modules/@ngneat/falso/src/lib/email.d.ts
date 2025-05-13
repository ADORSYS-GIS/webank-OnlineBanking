import { FakeOptions } from './core/core';
export interface EmailOptions extends FakeOptions {
    firstName?: string;
    lastName?: string;
    provider?: string;
    suffix?: string;
    nameSeparator?: NameSeparators;
}
export type NameSeparators = '.' | '-' | '_' | '+' | 'none';
/**
 * Generate a random email.
 *
 * @category Person, User
 *
 * @example
 *
 * randEmail()
 *
 * @example
 *
 * randEmail({ length: 10 })
 *
 * @example
 *
 * randEmail({ firstName: 'Netanel' })
 *
 * @example
 *
 * randEmail({ lastName: 'Basal' })
 *
 * @example
 *
 * randEmail({ nameSeparator: '.' })
 *
 * @example
 *
 * randEmail({ provider: 'gmail' })
 *
 * @example
 *
 * randEmail({ suffix: 'com' })
 *
 *
 */
export declare function randEmail<Options extends EmailOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
