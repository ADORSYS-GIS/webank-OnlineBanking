import { FakeOptions } from './core/core';
/**
 * Generate a random clothing size.
 *
 * @category Commerce
 *
 * @example
 *
 * randClothingSize()
 *
 * @example
 *
 * randClothingSize({ length: 10 })
 *
 */
export declare function randClothingSize<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
