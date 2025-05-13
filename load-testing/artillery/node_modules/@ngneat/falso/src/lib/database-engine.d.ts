import { FakeOptions } from './core/core';
/**
 * Generate a random database engine.
 *
 * @category database
 *
 * @example
 *
 * randDatabaseEngine()
 *
 * @example
 *
 * randDatabaseEngine({ length: 10 })
 *
 */
export declare function randDatabaseEngine<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
