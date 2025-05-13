import { FakeOptions } from './core/core';
/**
 * Generate a random quote.
 *
 * @category text
 *
 * @example
 *
 * randQuote()
 *
 * @example
 *
 * randQuote({ length: 10 })
 *
 */
export declare function randQuote<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
