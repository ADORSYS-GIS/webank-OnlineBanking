import { FakeOptions } from './core/core';
interface PastOptions extends FakeOptions {
    years?: number;
}
/**
 * Generate a random past date.
 *
 * @category date
 *
 * @example
 *
 * randPastDate()
 *
 * @example
 *
 * randPastDate({ length: 10 })
 *
 * @example
 *
 * randPastDate({ years: 2 }) // default is 1
 *
 */
export declare function randPastDate<Options extends PastOptions = never>(options?: Options): import("./core/core").Return<Date, Options>;
export {};
