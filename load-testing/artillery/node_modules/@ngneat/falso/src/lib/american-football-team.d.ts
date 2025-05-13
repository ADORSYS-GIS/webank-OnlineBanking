import { FakeOptions } from './core/core';
/**
 * Generate a random american football team.
 *
 * @category sports
 *
 * @example
 *
 * randAmericanFootballTeam()
 *
 * @example
 *
 * randAmericanFootballTeam({ length: 10 })
 *
 */
export declare function randAmericanFootballTeam<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
