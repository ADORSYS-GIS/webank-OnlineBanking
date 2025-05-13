import { FakeOptions } from './core/core';
interface MaskOptions extends FakeOptions {
    mask?: string;
    char?: string;
    digit?: string;
}
/**
 * Generate a random mask.
 *
 * @category Finance
 *
 * @example
 *
 * randMask()
 *
 * @example
 *
 * randMask({ mask: '@#### @##' })
 *
 * @example
 *
 * randMask({ length: 10 })
 *
 */
export declare function randMask<Options extends MaskOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
export {};
