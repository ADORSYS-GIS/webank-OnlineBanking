import { FakeOptions } from './core/core';
export interface MonthOptions extends FakeOptions {
    abbreviation?: boolean;
}
/**
 * Generate a random month.
 *
 * @category date
 *
 * @example
 *
 * randMonth()
 *
 * @example
 *
 * randMonth({ abbreviation: true })
 *
 *
 * @example
 *
 * randMonth({ length: 10 })
 *
 */
export declare function randMonth<Options extends MonthOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
