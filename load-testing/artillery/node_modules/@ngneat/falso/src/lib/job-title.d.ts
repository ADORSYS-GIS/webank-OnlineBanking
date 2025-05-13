import { FakeOptions } from './core/core';
/**
 * Generate a random job title.
 *
 * @category Jobs
 *
 * @example
 *
 * randJobTitle()
 *
 * @example
 *
 * randJobTitle({ length: 10 })
 *
 */
export declare function randJobTitle<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
