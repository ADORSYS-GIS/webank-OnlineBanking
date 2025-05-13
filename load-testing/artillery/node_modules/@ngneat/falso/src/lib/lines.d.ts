import { FakeOptions } from './core/core';
/**
 * Generate a random lines.
 *
 * @category text
 *
 * @example
 *
 * randLines()
 *
 * @example
 *
 * randLines({ length: 10 })
 *
 */
export declare function randLines<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
