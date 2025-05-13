import { FakeOptions } from './core/core';
/**
 * Generate a random phrase.
 *
 * @category text
 *
 * @example
 *
 * randPhrase()
 *
 * @example
 *
 * randPhrase({ length: 10 })
 *
 */
export declare function randPhrase<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
