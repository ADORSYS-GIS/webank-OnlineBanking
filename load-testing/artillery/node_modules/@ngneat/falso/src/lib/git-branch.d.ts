import { FakeOptions } from './core/core';
/**
 * Generate a random git branch.
 *
 * @category git
 *
 * @example
 *
 * randGitBranch()
 *
 * @example
 *
 * randGitBranch({ length: 10 })
 *
 */
export declare function randGitBranch<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
