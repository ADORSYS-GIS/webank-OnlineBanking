import { FakeOptions } from '../../core/core';
/**
 * Generate a random food item.
 *
 * @category food
 *
 * @example
 *
 * randFood()
 *
 * @example
 *
 * randFood({ length: 10 })
 *
 */
export declare function randFood<Options extends FakeOptions = never>(options?: Options): import("../../core/core").Return<string, Options>;
