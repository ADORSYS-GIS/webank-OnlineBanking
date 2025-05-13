import { FakeOptions } from './core/core';
/**
 * Generate a random octal.
 *
 * @category Math
 *
 * @example
 *
 * randOctal()
 *
 * @example
 *
 * randOctal({ length: 10 })
 *
 */
export declare function randOctal<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<bigint, Options>;
