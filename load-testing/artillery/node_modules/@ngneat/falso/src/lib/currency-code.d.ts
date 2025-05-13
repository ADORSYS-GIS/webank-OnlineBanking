import { FakeOptions } from './core/core';
/**
 * Generate a random currency code.
 *
 * @category finance
 *
 * @example
 *
 * randCurrencyCode()
 *
 * @example
 *
 * randCurrencyCode({ length: 10 })
 *
 */
export declare function randCurrencyCode<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
