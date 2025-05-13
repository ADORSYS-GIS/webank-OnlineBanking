import { FakeOptions } from './core/core';
/**
 * Generate a random port.
 *
 * @category internet
 *
 * @example
 *
 * randPort()
 *
 * @example
 *
 * randPort({ length: 10 })
 *
 */
export declare function randPort<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<number, Options>;
