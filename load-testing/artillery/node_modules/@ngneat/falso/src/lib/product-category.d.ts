import { FakeOptions } from './core/core';
/**
 * Generate a random product category.
 *
 * @category Commerce
 *
 * @example
 *
 * randProductCategory()
 *
 * @example
 *
 * randProductCategory({ length: 10 })
 *
 */
export declare function randProductCategory<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
