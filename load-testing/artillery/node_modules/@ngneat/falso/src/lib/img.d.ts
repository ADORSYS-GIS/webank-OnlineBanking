import { FakeOptions } from './core/core';
interface ImgOptions extends FakeOptions {
    width?: number;
    height?: number;
    grayscale?: boolean;
    random?: boolean;
}
/**
 * Generate a random img.
 *
 * @category General
 *
 * @example
 *
 * randImg()
 *
 * @example
 *
 * randImg({ length: 10 })
 *
 * @example
 *
 * randImg({ width: 300 }) // default is "height" or 500 (if not set either)
 *
 * @example
 *
 * randImg({ height: 200 }) // default is "width" or 500 (if not set either)
 *
 * @example
 *
 * randImg({ grayscale: true }) // return a grayscale image (default is false)
 *
 * @example
 *
 * randImg({ random: true }) // default is true, prevent the image from being cached
 *
 */
export declare function randImg<Options extends ImgOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
export {};
