import { FakeOptions } from '../../core/core';
/**
 * Generate a random person title.
 *
 * @category person
 *
 * @example
 *
 * randPersonTitle()
 *
 * @example
 *
 * randPersonTitle({ length: 10 })
 *
 */
export declare function randPersonTitle<Options extends FakeOptions = never>(options?: Options): import("../../core/core").Return<string, Options>;
