import { FakeOptions } from './core/core';
/**
 * Generate a random movie character.
 *
 * @category movie
 *
 * @example
 *
 * randMovieCharacter()
 *
 * @example
 *
 * randMovieCharacter({ length: 10 })
 *
 */
export declare function randMovieCharacter<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
