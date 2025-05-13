import { FakeOptions } from './core/core';
/**
 * Generate a random cardinal direction.
 *
 * @category address
 *
 * @example
 *
 * randCardinalDirection()
 *
 * @example
 *
 * randCardinalDirection({ length: 10 })
 *
 */
export declare function randCardinalDirection<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
