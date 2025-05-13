import { FakeOptions } from './core/core';
/**
 * Generate a random file path.
 *
 * @category system
 *
 * @example
 *
 * randFilePath()
 *
 * @example
 *
 * randFilePath({ length: 10 })
 *
 */
export declare function randFilePath<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
