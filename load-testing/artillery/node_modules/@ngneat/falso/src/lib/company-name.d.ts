import { FakeOptions } from './core/core';
/**
 * Generate a random company name.
 *
 * @category Company
 *
 * @example
 *
 * randCompanyName()
 *
 * @example
 *
 * randCompanyName({ length: 10 })
 *
 */
export declare function randCompanyName<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
