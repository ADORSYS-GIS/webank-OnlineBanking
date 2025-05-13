import { FakeOptions } from './core/core';
export interface HSLOptions extends FakeOptions {
    alpha?: boolean;
}
/**
 * Generate a random hsl color.
 *
 * @category colors
 *
 * @example
 *
 * randHsl()
 *
 * @example
 *
 * randHsl({ length: 3 })
 *
 * @example
 *
 * randHsl({ alpha: true }) // default is false
 *
 */
export declare function randHsl<Options extends HSLOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
