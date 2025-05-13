import { FakeOptions } from './core/core';
/**
 * Generate a random verb.
 *
 * @category text
 *
 * @example
 *
 * randVerb()
 *
 * @example
 *
 * randVerb({ length: 10 })
 *
 */
export declare function randVerb<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
