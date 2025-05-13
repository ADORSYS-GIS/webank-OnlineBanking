import { FakeOptions } from './core/core';
/**
 * Generate a random adverb for definite or indefinite frequency.
 *
 * @category date
 *
 * @example
 *
 * randFrequency()
 *
 * @example
 *
 * randFrequency({ length: 10 })
 *
 */
export declare function randFrequency<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
