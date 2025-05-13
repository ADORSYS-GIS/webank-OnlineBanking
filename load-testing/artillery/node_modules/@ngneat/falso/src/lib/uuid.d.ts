import { FakeOptions } from './core/core';
/**
 * Generate a random uuid.
 *
 * @category general, database
 *
 * @example
 *
 * randUuid()
 *
 * @example
 *
 * randUuid({ length: 10 })
 *
 */
export declare function randUuid<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
