import { FakeOptions } from '../../core/core';
/**
 * Generate a random bird.
 *
 * @category animals
 *
 * @example
 *
 * randBird()
 *
 * @example
 *
 * randBird({ length: 10 })
 *
 */
export declare function randBird<Options extends FakeOptions = never>(options?: Options): import("../../core/core").Return<string, Options>;
