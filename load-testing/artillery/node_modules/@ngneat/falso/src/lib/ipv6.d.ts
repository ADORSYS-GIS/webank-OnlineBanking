import { FakeOptions } from './core/core';
/**
 * Generate a random ipv6.
 *
 * @category internet
 *
 * @example
 *
 * randIpv6()
 *
 * @example
 *
 * randIpv6({ length: 10 })
 *
 */
export declare function randIpv6<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
