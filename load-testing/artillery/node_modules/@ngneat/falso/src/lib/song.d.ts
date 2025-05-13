import { FakeOptions } from './core/core';
/**
 * Generate a random song.
 *
 * @category Music
 *
 * @example
 *
 * randSong()
 *
 * @example
 *
 * randSong({ length: 10 })
 *
 */
export declare function randSong<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
