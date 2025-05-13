import { FakeOptions } from './core/core';
/**
 * Generate a random database column.
 *
 * @category database
 *
 * @example
 *
 * randDatabaseColumn()
 *
 * @example
 *
 * randDatabaseColumn({ length: 10 })
 *
 */
export declare function randDatabaseColumn<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
