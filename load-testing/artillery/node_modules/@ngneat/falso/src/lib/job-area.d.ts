import { FakeOptions } from './core/core';
/**
 * Generate a random job area.
 *
 * @category Jobs
 *
 * @example
 *
 * randJobArea()
 *
 * @example
 *
 * randJobArea({ length: 10 })
 *
 */
export declare function randJobArea<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
