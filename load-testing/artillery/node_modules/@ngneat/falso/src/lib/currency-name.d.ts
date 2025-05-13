import { FakeOptions } from './core/core';
/**
 * Generate a random currency name.
 *
 * @category Finance
 *
 * @example
 *
 * randCurrencyName()
 *
 * @example
 *
 * randCurrencyName({ length: 10 })
 *
 */
export declare function randCurrencyName<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
