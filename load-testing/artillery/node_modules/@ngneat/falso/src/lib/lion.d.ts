import { FakeOptions } from './core/core';
/**
 * Generate a random lion.
 *
 * @category animals
 *
 * @example
 *
 * randLion()
 *
 * @example
 *
 * randLion({ length: 10 })
 *
 */
export declare function randLion<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
