import { FakeOptions } from './core/core';
/**
 * Generate a random bitcoin-address.
 *
 * @category finance
 *
 * @example
 *
 * randBitcoinAddress()
 *
 * @example
 *
 * randBitcoinAddress({ length: 10 })
 *
 */
export declare function randBitcoinAddress<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string[], Options>;
