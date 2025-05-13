import { FakeOptions } from './core/core';
export interface WordOptions extends FakeOptions {
    capitalize?: boolean;
}
/**
 * Generate a random word.
 *
 * @category text
 *
 * @example
 *
 * randWord()
 *
 * @example
 *
 * randWord({ capitalize: true }) // default is false
 *
 * @example
 *
 * randWord({ length: 10 })
 *
 */
export declare function randWord<Options extends WordOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
