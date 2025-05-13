import { FakeOptions } from './core/core';
/**
 * Generate a random pronoun.
 *
 * @category person
 *
 * @example
 *
 * randPronoun()
 *
 */
export declare function randPronoun<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
