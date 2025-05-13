import { FakeOptions } from './core/core';
/**
 * Generate a random svg.
 *
 * @category general
 *
 * @example
 *
 * randSvg()
 *
 * @example
 *
 * randSvg({ length: 10 })
 *
 */
export declare function randSvg<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
