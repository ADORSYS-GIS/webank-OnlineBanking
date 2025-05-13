import { FakeOptions } from './core/core';
/**
 * Generate a random mac address.
 *
 * @category system
 *
 * @example
 *
 * randMac()
 *
 * @example
 *
 * randMac({ length: 10 })
 *
 */
export declare function randMac<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
