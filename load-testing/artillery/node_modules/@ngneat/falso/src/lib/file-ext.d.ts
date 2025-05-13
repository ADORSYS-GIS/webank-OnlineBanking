import { FakeOptions } from './core/core';
/**
 * Generate a random file ext.
 *
 * @category system
 *
 * @example
 *
 * randFileExt()
 *
 * @example
 *
 * randFileExt({ length: 10 })
 *
 */
export declare function randFileExt<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
