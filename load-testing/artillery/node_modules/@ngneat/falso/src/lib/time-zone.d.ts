import { FakeOptions } from './core/core';
/**
 * Generate a random time zone.
 *
 * @category address
 *
 * @example
 *
 * randTimeZone()
 *
 * @example
 *
 * randTimeZone({ length: 10 })
 *
 */
export declare function randTimeZone<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
