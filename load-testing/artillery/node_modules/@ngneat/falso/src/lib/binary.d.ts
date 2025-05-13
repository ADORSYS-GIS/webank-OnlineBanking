import { FakeOptions } from './core/core';
/**
 * Generate a random binary.
 *
 * @category Math
 *
 * @example
 *
 * randBinary()
 *
 * @example
 *
 * randBinary({ length: 10 })
 *
 */
export declare function randBinary<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
