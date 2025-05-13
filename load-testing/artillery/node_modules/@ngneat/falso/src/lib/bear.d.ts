import { FakeOptions } from './core/core';
/**
 * Generate a random bear.
 *
 * @category animals
 *
 * @example
 *
 * randBear()
 *
 * @example
 *
 * randBear({ length: 10 })
 *
 */
export declare function randBear<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
