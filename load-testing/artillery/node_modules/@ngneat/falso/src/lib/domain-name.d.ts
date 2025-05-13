import { FakeOptions } from './core/core';
/**
 * Generate a random domain name.
 *
 * @category Internet
 *
 * @example
 *
 * randDomainName()
 *
 * @example
 *
 * randDomainName({ length: 10 })
 *
 */
export declare function randDomainName<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
