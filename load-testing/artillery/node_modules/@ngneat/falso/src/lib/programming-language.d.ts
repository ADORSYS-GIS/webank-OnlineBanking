import { FakeOptions } from './core/core';
/**
 * Generate a random programming language.
 *
 * @category code
 *
 * @example
 *
 * randProgrammingLanguage()
 *
 * @example
 *
 * randProgrammingLanguage({ length: 10 })
 *
 */
export declare function randProgrammingLanguage<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
