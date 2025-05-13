import { FakeOptions } from './core/core';
/**
 * Generate a random product material.
 *
 * @category commerce
 *
 * @example
 *
 * randProductMaterial()
 *
 * @example
 *
 * randProductMaterial({ length: 10 })
 *
 */
export declare function randProductMaterial<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
