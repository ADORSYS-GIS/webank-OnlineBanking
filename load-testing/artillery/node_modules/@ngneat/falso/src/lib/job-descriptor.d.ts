import { FakeOptions } from './core/core';
/**
 * Generate a random job descriptor.
 *
 * @category jobs
 *
 * @example
 *
 * randJobDescriptor()
 *
 * @example
 *
 * randJobDescriptor({ length: 10 })
 *
 */
export declare function randJobDescriptor<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
