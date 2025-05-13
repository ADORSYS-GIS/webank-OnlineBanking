import { FakeOptions } from './core/core';
/**
 * Generate a random font family.
 *
 * @category code
 *
 * @example
 *
 * randFontFamily()
 *
 * @example
 *
 * randFontFamily({ length: 10 })
 *
 */
export declare function randFontFamily<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
