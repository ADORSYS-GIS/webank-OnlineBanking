import { FakeOptions } from '../../core/core';
/**
 * Generate a random baseball team.
 *
 * @category sports
 *
 * @example
 *
 * randBaseballTeam()
 *
 * @example
 *
 * randBaseballTeam({ length: 10 })
 *
 */
export declare function randBaseballTeam<Options extends FakeOptions = never>(options?: Options): import("../../core/core").Return<string, Options>;
