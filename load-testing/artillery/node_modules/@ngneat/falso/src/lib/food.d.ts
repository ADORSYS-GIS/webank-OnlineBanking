import { FakeOptions } from './core/core';
export type FoodOrigin = 'china' | 'italy' | 'india' | 'mexico' | 'japan' | 'france' | 'lebanon' | 'thailand' | 'greece' | 'turkey' | 'spain' | 'venezuela' | 'chile' | 'argentina' | 'colombia' | 'ecuador' | 'peru' | 'el salvador' | 'romania';
export interface FoodOptions extends FakeOptions {
    origin?: FoodOrigin;
}
/**
 * Generate a random food name.
 *
 * @category Food
 *
 * @example
 *
 * randFood()
 *
 * @example
 *
 * randFood({ origin: 'italy' })
 *
 * @example
 *
 * randFood({ length: 10 })
 *
 */
export declare function randFood<Options extends FoodOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
