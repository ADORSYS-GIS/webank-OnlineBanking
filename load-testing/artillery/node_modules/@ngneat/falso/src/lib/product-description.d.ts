import { FakeOptions } from './core/core';
/**
 * Generate a random product description.
 *
 * @category Commerce
 *
 * @example
 *
 * randProductDescription()
 *
 * @example
 *
 * randProductDescription({ length: 10 })
 *
 */
export declare function randProductDescription<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
