import { FakeOptions } from './core/core';
export interface SemverOptions extends FakeOptions {
    prefix?: string;
}
/**
 * Generate a random semantic version.
 *
 * @category internet
 *
 * @example
 *
 * randSemver()
 *
 * @example
 *
 * randSemver({ length: 10 })
 *
 * @example
 *
 * randSemver({ prefix: 'v' })
 *
 */
export declare function randSemver<Options extends SemverOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
