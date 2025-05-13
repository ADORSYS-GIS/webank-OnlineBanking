import { FakeOptions } from '../../core/core';
/**
 * Generate a random gender.
 *
 * @category person
 *
 * @example
 *
 * randGender()
 *
 * @example
 *
 * randGender({ length: 10 })
 *
 */
export declare function randGender<Options extends FakeOptions = never>(options?: Options): import("../../core/core").Return<string, Options>;
