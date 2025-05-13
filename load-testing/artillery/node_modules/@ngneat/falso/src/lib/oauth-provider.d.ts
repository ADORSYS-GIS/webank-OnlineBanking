import { FakeOptions } from './core/core';
/**
 * Generate a random OAuth provider.
 *
 * @category Internet
 *
 * @example
 *
 * randOAuthProvider()
 *
 * @example
 *
 * randOAuthProvider({ length: 10 })
 *
 */
export declare function randOAuthProvider<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
