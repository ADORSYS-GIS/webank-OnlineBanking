import { FakeOptions } from './core/core';
/**
 * Generate a random git commit.
 *
 * @category git
 *
 * @example
 *
 * randGitCommitEntry()
 *
 * @example
 *
 * randGitCommitEntry({ length: 10 })
 *
 */
export declare function randGitCommitEntry<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
