import { FakeOptions } from '../../core/core';
/**
 * Generate a random language.
 *
 * @category address
 *
 * @example
 *
 * randLanguage()
 *
 * @example
 *
 * randLanguage({ length: 10 })
 *
 */
export declare function randLanguage<Options extends FakeOptions = never>(options?: Options): import("../../core/core").Return<string, Options>;
