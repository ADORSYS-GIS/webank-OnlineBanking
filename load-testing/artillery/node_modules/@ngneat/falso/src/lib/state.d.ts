import { FakeOptions } from './core/core';
/**
 * Generate a random state.
 *
 * @category address
 *
 * @example
 *
 * randState()
 *
 * @example
 *
 * randState({ length: 10 })
 *
 */
export declare function randState<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
