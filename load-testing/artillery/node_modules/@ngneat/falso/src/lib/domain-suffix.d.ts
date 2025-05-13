import { FakeOptions } from './core/core';
/**
 * Generate a random domain suffix.
 *
 * @category Internet
 *
 * @example
 *
 * randDomainSuffix()
 *
 * @example
 *
 * randDomainSuffix({ length: 10 })
 *
 */
export declare function randDomainSuffix<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
