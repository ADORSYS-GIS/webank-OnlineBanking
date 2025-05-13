import { FakeOptions } from './core/core';
/**
 * Generate a random ethereum address.
 *
 * @category Finance
 *
 * @example
 *
 * randEthereumAddress()
 *
 * @example
 *
 * randEthereumAddress({ length: 10 })
 *
 */
export declare function randEthereumAddress<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
