import { FakeOptions } from './core/core';
export interface AccountOptions extends FakeOptions {
    accountLength?: number;
}
/**
 * Generate a random account.
 *
 * @category finance
 *
 * @example
 *
 * randAccount()
 *
 * @example
 *
 * randAccount({ accountLength: 12 }) // default is 9
 *
 * @example
 *
 * randAccount({ length: 10 })
 *
 */
export declare function randAccount<Options extends AccountOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
