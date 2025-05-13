import { FakeOptions } from '../../core/core';
/**
 * Generate a random weekday.
 *
 * @category date
 *
 * @example
 *
 * randWeekday()
 *
 * @example
 *
 * randWeekday({ length: 10 })
 *
 */
export declare function randWeekday<Options extends FakeOptions = never>(options?: Options): import("../../core/core").Return<string, Options>;
