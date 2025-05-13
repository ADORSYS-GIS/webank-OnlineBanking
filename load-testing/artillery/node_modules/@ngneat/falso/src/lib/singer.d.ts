import { FakeOptions } from './core/core';
/**
 * Generate a random singer.
 *
 * @category music
 *
 * @example
 *
 * randSinger()
 *
 * @example
 *
 * randSinger({ length: 10 })
 *
 */
export declare function randSinger<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
