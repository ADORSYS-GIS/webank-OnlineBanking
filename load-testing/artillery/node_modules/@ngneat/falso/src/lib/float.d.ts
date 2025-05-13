import { FakeOptions, RandomInRangeOptions } from './core/core';
export interface RandomFloatOptions extends RandomInRangeOptions, FakeOptions {
}
/**
 * Generate a random float.
 *
 * @category general, math
 *
 * @example
 *
 * randFloat()
 *
 * @example
 *
 * randFloat({ length: 10 })
 *
 * @example
 *
 * randFloat({ min: 10, max: 20, fraction: 1 }) // 12.5
 *
 * @example
 *
 * randFloat({ min: 10, max: 20, fraction: 2 }) // 12.52
 */
export declare function randFloat<Options extends RandomFloatOptions = never>(options?: Options): import("./core/core").Return<number, Options>;
