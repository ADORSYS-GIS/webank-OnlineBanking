import { FakeOptions, RandomInRangeOptions } from './core/core';
export interface RandomAggregationOptions extends RandomInRangeOptions, FakeOptions {
    totalValue?: number;
    noZeros?: boolean;
}
type Tuple<V, N extends number, T extends V[] = []> = N extends T['length'] ? T : Tuple<V, N, [...T, V]>;
/**
 * Generate a list of numbers that add up to a total value.
 *
 * @category general, math
 *
 * @example
 *
 * randAggregation() // default values is 2 and totalValue is 100
 *
 * @example
 *
 * randAggregation({ values: 3, totalValue: 1500, length: 10 })
 *
 * @example
 *
 * randAggregation({ values: 3 })
 *
 */
export declare function randAggregation<T extends number = 2, Options extends RandomAggregationOptions = never>(options?: Options & {
    values?: T;
}): import("./core/core").Return<number extends T ? number[] : Tuple<number, T, []>, Options & {
    values?: T | undefined;
}>;
export {};
