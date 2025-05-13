import { FakeOptions } from './core/core';
/**
 * Generate a random priority.
 *
 * @category general
 *
 * @example
 *
 * randPriority()
 *
 * @example
 *
 * randPriority({ length: 10 })
 *
 */
export declare function randPriority<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
