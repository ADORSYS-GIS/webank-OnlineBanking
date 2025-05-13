import { FakeOptions } from './core/core';
export type Category = 'olympic' | 'winterOlympic' | 'outdoor';
export interface SportCategories extends FakeOptions {
    category?: Category;
}
/**
 * Generate a random sports.
 *
 * @category sports
 *
 * @example
 *
 * randSports()
 *
 * @example
 *
 * randSports({ category : 'olympic' })
 *
 * @example
 *
 * randSports({ length: 10 })
 *
 */
export declare function randSports<Options extends SportCategories = never>(options?: Options): import("./core/core").Return<string, Options>;
