import { FakeOptions } from './core/core';
/**
 * Generate a random skill.
 *
 * @category Person
 *
 * @example
 *
 * randSkill()
 *
 * @example
 *
 * randSkill({ length: 10 })
 *
 */
export declare function randSkill<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
