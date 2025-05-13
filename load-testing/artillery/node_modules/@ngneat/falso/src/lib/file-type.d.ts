import { FakeOptions } from './core/core';
/**
 * Generate a random file type.
 *
 * @category system
 *
 * @example
 *
 * randFileType()
 *
 * @example
 *
 * randFileType({ length: 10 })
 *
 */
export declare function randFileType<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
