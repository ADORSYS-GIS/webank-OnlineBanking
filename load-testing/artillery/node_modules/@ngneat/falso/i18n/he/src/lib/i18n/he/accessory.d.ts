import { FakeOptions } from '../../core/core';
/**
 * Generate a random accessory.
 *
 * @category Commerce
 *
 * @example
 *
 * randAccessory()
 *
 * @example
 *
 * randAccessory({ length: 10 })
 *
 */
export declare function randAccessory<Options extends FakeOptions = never>(options?: Options): import("../../core/core").Return<string, Options>;
