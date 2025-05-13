import { FakeOptions } from './core/core';
/**
 * Generate a random cow.
 *
 * @category animals
 *
 * @example
 *
 * randCow()
 *
 * @example
 *
 * randCow({ length: 10 })
 *
 */
export declare function randCow<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
