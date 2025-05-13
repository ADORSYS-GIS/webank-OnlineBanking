import { FakeOptions } from './core/core';
/**
 * Generate a random database collation.
 *
 * @category database
 *
 * @example
 *
 * randDatabaseCollation()
 *
 * @example
 *
 * randDatabaseCollation({ length: 10 })
 *
 */
export declare function randDatabaseCollation<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
