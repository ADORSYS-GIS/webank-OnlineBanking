import { FakeOptions } from './core/core';
/**
 * Generate a random sports team.
 *
 * @category sports
 *
 * @example
 *
 * randSportsTeam()
 *
 * @example
 *
 * randSportsTeam({ length: 10 })
 *
 */
export declare function randSportsTeam<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
