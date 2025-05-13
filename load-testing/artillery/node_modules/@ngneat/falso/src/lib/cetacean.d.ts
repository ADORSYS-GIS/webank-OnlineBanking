import { FakeOptions } from './core/core';
/**
 * Generate a random cetacean.
 *
 * @category animals
 *
 * @example
 *
 * randCetacean()
 *
 * @example
 *
 * randCetacean({ length: 10 })
 *
 */
export declare function randCetacean<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
