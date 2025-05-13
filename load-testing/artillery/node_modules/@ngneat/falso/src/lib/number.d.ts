import { FakeOptions, RandomInRangeOptions } from './core/core';
export interface RandomNumberOptions extends RandomInRangeOptions, FakeOptions {
    precision?: number;
}
/**
 * Generate a random number.
 *
 * @category general, math
 *
 * @example
 *
 * randNumber()
 *
 * @example
 *
 * randNumber({ length: 10 })
 *
 * @example
 *
 * randNumber({ min: 10, max: 1000 }) // default is 'min': 0, 'max': 999_999
 *
 * @example
 *
 * randNumber({ min: 0.1, max: 1, fraction: 2 }) // 0.37
 *
 * @example
 *
 * randNumber({ min: 1000, max: 100000, precision: 1000 }) // 67_000
 *
 * @example
 *
 * randNumber({ min: 1000, max: 2000, precision: 100 }) // 1_200
 *
 * @example
 *
 * randNumber({ min: 1000, max: 2000, precision: 10 }) // 1_250
 */
export declare function randNumber<Options extends RandomNumberOptions = never>(options?: Options): import("./core/core").Return<number, Options>;
