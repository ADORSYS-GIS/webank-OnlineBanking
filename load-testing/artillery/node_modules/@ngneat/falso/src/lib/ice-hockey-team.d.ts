import { FakeOptions } from './core/core';
/**
 * Generate a random ice hockey team.
 *
 * @category sports
 *
 * @example
 *
 * randIceHockeyTeam()
 *
 * @example
 *
 * randIceHockeyTeam({ length: 10 })
 *
 */
export declare function randIceHockeyTeam<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
