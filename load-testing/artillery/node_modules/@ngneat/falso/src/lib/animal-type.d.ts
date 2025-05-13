import { FakeOptions } from './core/core';
/**
 * Generate a random animal type.
 *
 * @category animals
 *
 * @example
 *
 * randAnimalType()
 *
 * @example
 *
 * randAnimalType({ length: 10 })
 *
 */
export declare function randAnimalType<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
