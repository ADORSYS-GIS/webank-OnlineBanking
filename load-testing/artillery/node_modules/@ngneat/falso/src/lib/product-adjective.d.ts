import { FakeOptions } from './core/core';
/**
 * Generate a random product adjective.
 *
 * @category commerce
 *
 * @example
 *
 * randProductAdjective()
 *
 * @example
 *
 * randProductAdjective({ length: 10 })
 *
 */
export declare function randProductAdjective<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
