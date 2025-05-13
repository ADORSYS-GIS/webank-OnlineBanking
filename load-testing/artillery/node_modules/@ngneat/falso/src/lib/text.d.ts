import { FakeOptions } from './core/core';
export interface TextOptions extends FakeOptions {
    charCount?: number;
}
/**
 * Generate a random text.
 *
 * @category text
 *
 * @example
 *
 * randText()
 *
 * @example
 *
 * randText({ length: 10 })
 *
 * @example
 *
 * randText({ charCount: 10 }) // default is 10
 *
 */
export declare function randText<Options extends TextOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
