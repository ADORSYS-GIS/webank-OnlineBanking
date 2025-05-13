import { FakeOptions } from './core/core';
/**
 * Generate a random routing number.
 *
 * @category finance
 *
 * @example
 *
 * randRoutingNumber()
 *
 * @example
 *
 * randRoutingNumber({ length: 10 })
 *
 */
export declare function randRoutingNumber<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<number, Options>;
