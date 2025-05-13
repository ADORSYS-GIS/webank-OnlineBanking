import { FakeOptions } from './core/core';
/**
 * Generate a random git message.
 *
 * @category git
 *
 * @example
 *
 * randGitCommitMessage()
 *
 * @example
 *
 * randGitCommitMessage({ length: 10 })
 *
 */
export declare function randGitCommitMessage<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
