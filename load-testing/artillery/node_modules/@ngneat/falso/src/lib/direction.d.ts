import { FakeOptions } from './core/core';
/**
 * Generate a random direction.
 *
 * @category address
 *
 * @example
 *
 * randDirection()
 *
 * @example
 *
 * randDirection({ length: 10 })
 *
 */
export declare function randDirection<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
