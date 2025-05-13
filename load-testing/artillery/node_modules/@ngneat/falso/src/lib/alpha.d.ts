import { FakeOptions } from './core/core';
/**
 * Get a random alpha.
 *
 * @category text
 *
 * @example
 *
 * randAlpha()
 *
 * @example
 *
 * randAlpha({ length: 3 })
 *
 */
export declare function randAlpha<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
