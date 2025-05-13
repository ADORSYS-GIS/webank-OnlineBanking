import { FakeOptions } from '../../core/core';
/**
 * Generate a random first name.
 *
 * @category person
 *
 * @example
 *
 * randFirstName()
 *
 * @example
 *
 * randFirstName({ length: 10 })
 *
 */
export declare function randFirstName<Options extends FakeOptions = never>(options?: Options): import("../../core/core").Return<string, Options>;
