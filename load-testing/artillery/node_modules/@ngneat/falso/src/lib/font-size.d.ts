import { FakeOptions } from './core/core';
export type FontSizeSuffix = 'px' | 'cm' | 'em' | 'rem' | 'vw' | 'vh' | 'vmin' | 'vmax' | '%';
export interface FontSizeOptions extends FakeOptions {
    suffix?: FontSizeSuffix;
}
/**
 * Generate a random font size.
 *
 * @category code
 *
 * @example
 *
 * randFontSize()
 *
 * @example
 *
 * randFontSize({ length: 10 })
 *
 */
export declare function randFontSize<Options extends FontSizeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
