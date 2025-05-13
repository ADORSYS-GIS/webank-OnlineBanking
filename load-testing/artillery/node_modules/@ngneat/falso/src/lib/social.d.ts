import { FakeOptions } from './core/core';
/**
 * Generate random social network
 *
 * @category internet
 *
 * @example
 *
 * randSocial()
 *
 * @example
 *
 * randSocial({ length: 2 })
 *
 *
 */
export declare function randSocial<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<{
    name: string;
    link: string;
}, Options>;
