import { FakeOptions } from './core/core';
/**
 * Generate a random genre.
 *
 * @category music
 *
 * @example
 *
 * randMusicGenre()
 *
 * @example
 *
 * randMusicGenre({ length: 10 })
 *
 */
export declare function randMusicGenre<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
