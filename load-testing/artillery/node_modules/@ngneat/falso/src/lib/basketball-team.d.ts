import { FakeOptions } from './core/core';
/**
 * Generate a random basketball team.
 *
 * @category sports
 *
 * @example
 *
 * randBasketballTeam()
 *
 * @example
 *
 * randBasketballTeam({ length: 10 })
 *
 */
export declare function randBasketballTeam<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
