import { FakeOptions } from '../../core/core';
/**
 * Generate a random state abbr.
 *
 * @category address
 *
 * @example
 *
 * randStateAbbr()
 *
 * @example
 *
 * randStateAbbr({ length: 10 })
 *
 */
export declare function randStateAbbr<Options extends FakeOptions = never>(options?: Options): import("../../core/core").Return<string, Options>;
