import { FakeOptions } from './core/core';
export interface TextRangeOptions extends FakeOptions {
    min: number;
    max: number;
}
/**
 * Generate a random text range.
 *
 * @category text
 *
 * @example
 *
 * randTextRange({ min: 10, max: 100 })
 *
 * @example
 *
 * randTextRange({ min: 10, max: 100, length: 10 })
 *
 */
export declare function randTextRange<Options extends TextRangeOptions = never>(options: Options): import("./core/core").Return<string, Options>;
