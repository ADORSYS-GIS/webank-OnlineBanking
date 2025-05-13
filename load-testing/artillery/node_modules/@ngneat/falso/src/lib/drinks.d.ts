import { FakeOptions } from './core/core';
/**
 * Generate a random drink.
 *
 * @category drinks
 *
 * @example
 *
 * randDrinks()
 *
 * @example
 *
 * randDrinks({ length: 10 })
 *
 */
export declare function randDrinks<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
