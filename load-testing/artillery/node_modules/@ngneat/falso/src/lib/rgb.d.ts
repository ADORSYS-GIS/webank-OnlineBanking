import { FakeOptions } from './core/core';
export interface RGBOptions extends FakeOptions {
    alpha?: boolean;
}
/**
 * Generate a random rgb.
 *
 * @category colors
 *
 * @example
 *
 * randRgb()
 *
 * @example
 *
 * randRgb({ alpha: true }) // default is false
 *
 * @example
 *
 * randRgb({ length: 10 })
 *
 */
export declare function randRgb<Options extends RGBOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
