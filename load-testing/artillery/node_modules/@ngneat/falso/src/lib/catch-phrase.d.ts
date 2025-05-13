import { FakeOptions } from './core/core';
/**
 * Generate a random catch phrase.
 *
 * @category text
 *
 * @example
 *
 * randCatchPhrase()
 *
 * @example
 *
 * randCatchPhrase({ length: 10 })
 *
 */
export declare function randCatchPhrase<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
