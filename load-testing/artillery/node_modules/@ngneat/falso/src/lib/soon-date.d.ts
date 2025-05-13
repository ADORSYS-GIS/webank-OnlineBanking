import { FakeOptions } from './core/core';
interface SoonOptions extends FakeOptions {
    days?: number;
}
/**
 * Generate a random soon.
 *
 * @category date
 *
 * @example
 *
 * randSoonDate()
 *
 * @example
 *
 * randSoonDate({ days: 5 }) // default is 1
 *
 * @example
 *
 * randSoonDate({ length: 10 })
 *
 */
export declare function randSoonDate<Options extends SoonOptions = never>(options?: Options): import("./core/core").Return<Date, Options>;
export {};
