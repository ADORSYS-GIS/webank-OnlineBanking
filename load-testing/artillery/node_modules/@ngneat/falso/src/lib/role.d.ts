import { FakeOptions } from './core/core';
/**
 * Generate a random user role
 *
 * @category user
 *
 * @example
 *
 * randRole()
 *
 * @example
 *
 * randRole({ length: 10 })
 *
 */
export declare function randRole<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
