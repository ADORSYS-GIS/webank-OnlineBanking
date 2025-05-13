import { FakeOptions } from './core/core';
/**
 * Generate a random ip.
 *
 * @category internet
 *
 * @example
 *
 * randIp()
 *
 * @example
 *
 * randIp({ length: 10 })
 *
 */
export declare function randIp<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
