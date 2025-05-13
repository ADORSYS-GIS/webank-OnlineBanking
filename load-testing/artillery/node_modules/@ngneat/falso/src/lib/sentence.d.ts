import { FakeOptions } from './core/core';
/**
 * Generate a random sentence.
 *
 * @category text
 *
 * @example
 *
 * randSentence()
 *
 * @example
 *
 * randSentence({ length: 10 })
 *
 */
export declare function randSentence<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
