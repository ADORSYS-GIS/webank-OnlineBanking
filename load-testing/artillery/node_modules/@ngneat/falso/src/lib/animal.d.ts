import { FakeOptions } from './core/core';
/**
 * Generate a random animal.
 *
 * @category animals
 *
 * @example
 *
 * randAnimal()
 *
 * @example
 *
 * randAnimal({ length: 10 })
 *
 */
export declare function randAnimal<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
