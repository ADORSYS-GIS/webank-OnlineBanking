import { FakeOptions } from './core/core';
/**
 * Generate a random cat.
 *
 * @category animals
 *
 * @example
 *
 * randCat()
 *
 * @example
 *
 * randCat({ length: 10 })
 *
 */
export declare function randCat<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
