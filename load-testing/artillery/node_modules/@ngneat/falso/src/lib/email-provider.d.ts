import { FakeOptions } from './core/core';
/**
 * Generate a random email provider.
 *
 * @category Internet
 *
 * @example
 *
 * randEmailProvider()
 *
 * @example
 *
 * randEmailProvider({ length: 10 })
 *
 */
export declare function randEmailProvider<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
