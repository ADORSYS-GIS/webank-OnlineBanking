import { FakeOptions } from './core/core';
/**
 * Generate a random database type.
 *
 * @category database
 *
 * @example
 *
 * randDatabaseType()
 *
 * @example
 *
 * randDatabaseType({ length: 10 })
 *
 */
export declare function randDatabaseType<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
