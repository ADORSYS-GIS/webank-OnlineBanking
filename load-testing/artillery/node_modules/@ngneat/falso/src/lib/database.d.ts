import { FakeOptions } from './core/core';
/**
 * Generate a random database.
 *
 * @category database
 *
 * @example
 *
 * randDatabase()
 *
 * @example
 *
 * randDatabase({ length: 10 })
 *
 */
export declare function randDatabase<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
