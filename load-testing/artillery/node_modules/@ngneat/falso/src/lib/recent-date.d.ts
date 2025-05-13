import { FakeOptions } from './core/core';
interface RecentOptions extends FakeOptions {
    days?: number;
}
/**
 * Generate a random recent date.
 *
 * @category date
 *
 * @example
 *
 * randRecentDate()
 *
 * @example
 *
 * randRecentDate({ days: 10 }) // default is 1
 *
 * @example
 *
 * randRecentDate({ length: 10 })
 *
 */
export declare function randRecentDate<Options extends RecentOptions = never>(options?: Options): import("./core/core").Return<Date, Options>;
export {};
