import { FakeOptions } from './core/core';
/**
 * Generate a random dog.
 *
 * @category animals
 *
 * @example
 *
 * randDog()
 *
 * @example
 *
 * randDog({ length: 10 })
 *
 */
export declare function randDog<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
