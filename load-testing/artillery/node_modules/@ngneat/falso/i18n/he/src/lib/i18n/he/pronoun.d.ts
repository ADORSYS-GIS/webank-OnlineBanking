import { FakeOptions } from '../../core/core';
/**
 * Generate a random pronoun.
 *
 * @category person
 *
 * @example
 *
 * randPronoun()
 *
 * @example
 *
 * randPronoun({ length: 10 })
 *
 */
export declare function randPronoun<Options extends FakeOptions = never>(options?: Options): import("../../core/core").Return<string, Options>;
