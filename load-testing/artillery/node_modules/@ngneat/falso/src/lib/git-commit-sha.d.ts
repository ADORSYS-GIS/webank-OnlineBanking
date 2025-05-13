import { FakeOptions } from './core/core';
export declare function gitShaGenerator(len: number): () => string;
/**
 * Generate a random git sha.
 *
 * @category git
 *
 * @example
 *
 * randGitCommitSha()
 *
 * @example
 *
 * randGitCommitSha({ length: 10 })
 *
 */
export declare function randGitCommitSha<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
