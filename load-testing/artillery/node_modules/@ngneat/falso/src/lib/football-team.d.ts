import { FakeOptions } from './core/core';
/**
 * Generate a random football team.
 *
 * @category sports
 *
 * @example
 *
 * randFootballTeam()
 *
 * @example
 *
 * randFootballTeam({ length: 10 })
 *
 */
export declare function randFootballTeam<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
