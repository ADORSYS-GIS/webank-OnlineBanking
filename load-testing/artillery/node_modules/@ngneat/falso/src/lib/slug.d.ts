import { FakeOptions } from './core/core';
/**
 * Generate a random slug.
 *
 * @category Internet
 *
 * @example
 *
 * randSlug()
 *
 * @example
 *
 * randSlug({ length: 10 })
 *
 */
export declare function randSlug<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
