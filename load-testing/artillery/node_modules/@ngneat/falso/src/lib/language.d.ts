import { FakeOptions } from './core/core';
interface LanguageOptions extends FakeOptions {
    code?: boolean;
}
/**
 * Generate a random language.
 *
 * @category person
 *
 * @example
 *
 * randLanguage()
 *
 * @example
 *
 * randLanguage({ length: 10 })
 *
 * @example
 *
 * randLanguage({ code: true }) // default is false
 *
 */
export declare function randLanguage<Options extends LanguageOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
export {};
