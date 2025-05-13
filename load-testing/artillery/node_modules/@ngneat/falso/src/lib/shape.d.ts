import { FakeOptions } from './core/core';
/**
 * Generate a random shape.
 *
 * @category general
 *
 * @example
 *
 * randShape()
 *
 * @example
 *
 * randShape({ length: 10 })
 *
 */
export declare function randShape<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
