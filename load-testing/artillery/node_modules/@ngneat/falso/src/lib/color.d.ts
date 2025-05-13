import { FakeOptions } from './core/core';
/**
 * Generate a random color.
 *
 * @category colors
 *
 * @example
 *
 * randColor()
 *
 * @example
 *
 * randColor({ length: 10 })
 *
 */
export declare function randColor<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
