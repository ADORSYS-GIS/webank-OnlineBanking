import { FakeOptions } from './core/core';
/**
 * Generate a random mime type.
 *
 * @category system
 *
 * @example
 *
 * randMimeType()
 *
 * @example
 *
 * randMimeType({ length: 10 })
 *
 */
export declare function randMimeType<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
