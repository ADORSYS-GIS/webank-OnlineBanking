import { FakeOptions } from './core/core';
/**
 * Generate a random emoji.
 *
 * @category text
 *
 * @example
 *
 * randEmoji()
 *
 * @example
 *
 * randEmoji({ length: 10 })
 *
 */
export declare function randEmoji<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
