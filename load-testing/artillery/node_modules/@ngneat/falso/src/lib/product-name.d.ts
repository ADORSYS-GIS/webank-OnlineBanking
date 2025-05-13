import { FakeOptions } from './core/core';
/**
 * Generate a random product name.
 *
 * @category commerce
 *
 * @example
 *
 * randProductName()
 *
 * @example
 *
 * randProductName({ length: 10 })
 *
 */
export declare function randProductName<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
