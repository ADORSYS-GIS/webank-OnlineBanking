import { FakeOptions } from './core/core';
/**
 * Generate a random directory path.
 *
 * @category system
 *
 * @example
 *
 * randDirectoryPath()
 *
 * @example
 *
 * randDirectoryPath({ length: 10 })
 *
 */
export declare function randDirectoryPath<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
