import { FakeOptions } from './core/core';
/**
 * Generate a random ordinal direction.
 *
 * @category address
 *
 * @example
 *
 * randOrdinalDirection()
 *
 * @example
 *
 * randOrdinalDirection({ length: 10 })
 *
 */
export declare function randOrdinalDirection<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
