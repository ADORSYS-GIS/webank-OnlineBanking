import { FakeOptions } from './core/core';
/**
 * Generate random screen resolution
 *
 * @category internet
 *
 * @example
 *
 * randResolution()
 *
 * @example
 *
 * randResolution()
 *
 *
 */
export declare function randResolution<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<{
    width: string;
    height: string;
}, Options>;
