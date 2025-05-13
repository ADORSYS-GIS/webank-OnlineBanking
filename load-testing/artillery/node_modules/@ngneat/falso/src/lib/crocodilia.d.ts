import { FakeOptions } from './core/core';
/**
 * Generate a random crocodilia.
 *
 * @category animals
 *
 * @example
 *
 * randCrocodilia()
 *
 * @example
 *
 * randCrocodilia({ length: 10 })
 *
 */
export declare function randCrocodilia<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
