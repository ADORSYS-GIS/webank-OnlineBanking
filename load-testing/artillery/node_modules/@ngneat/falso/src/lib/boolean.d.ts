import { FakeOptions } from './core/core';
/**
 * Generate a random boolean.
 *
 * @category general
 *
 * @example
 *
 * randBoolean()
 *
 * @example
 *
 * randBoolean({ length: 10 })
 *
 */
export declare function randBoolean<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<boolean, Options>;
