import { FakeOptions } from './core/core';
/**
 * Generate a random hex.
 *
 * @category colors
 *
 * @example
 *
 * randHex()
 *
 * @example
 *
 * randHex({ length: 10 })
 *
 */
export declare function randHex<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
