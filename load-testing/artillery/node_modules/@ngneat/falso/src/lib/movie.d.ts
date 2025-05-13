import { FakeOptions } from './core/core';
/**
 * Generate a random movie.
 *
 * @category Movie
 *
 * @example
 *
 * randMovie()
 *
 * @example
 *
 * randMovie({ length: 10 })
 *
 */
export declare function randMovie<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
