import { FakeOptions } from './core/core';
/**
 * Generate a random fish.
 *
 * @category animals
 *
 * @example
 *
 * randFish()
 *
 * @example
 *
 * randFish({ length: 10 })
 *
 */
export declare function randFish<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
