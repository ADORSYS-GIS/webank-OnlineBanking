import { FakeOptions } from './core/core';
/**
 * Generate a random currency symbol.
 *
 * @category finance
 *
 * @example
 *
 * randCurrencySymbol()
 *
 * @example
 *
 * randCurrencySymbol({ length: 10 })
 *
 */
export declare function randCurrencySymbol<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
