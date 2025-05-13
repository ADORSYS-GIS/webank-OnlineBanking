import { FakeOptions } from './core/core';
interface FutureOptions extends FakeOptions {
    years?: number;
}
/**
 * Generate a random future.
 *
 * @category date
 *
 * @example
 *
 * randFutureDate()
 *
 * @example
 *
 * randFutureDate({ length: 10 })
 *
 * @example
 *
 * randFutureDate({ years: 10 }) // default is 1
 *
 */
export declare function randFutureDate<Options extends FutureOptions = never>(options?: Options): import("./core/core").Return<Date, Options>;
export {};
