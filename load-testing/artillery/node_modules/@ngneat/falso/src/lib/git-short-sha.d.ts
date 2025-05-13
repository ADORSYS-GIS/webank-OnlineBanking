import { FakeOptions } from './core/core';
/**
 * Generate a random git short sha.
 *
 * @category git
 *
 * @example
 *
 * randGitShortSha()
 *
 * @example
 *
 * randGitShortSha({ length: 10 })
 *
 */
export declare function randGitShortSha<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
