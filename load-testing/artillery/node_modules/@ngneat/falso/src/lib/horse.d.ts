import { FakeOptions } from './core/core';
/**
 * Generate a random horse.
 *
 * @category animals
 *
 * @example
 *
 * randHorse()
 *
 * @example
 *
 * randHorse({ length: 10 })
 *
 */
export declare function randHorse<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
