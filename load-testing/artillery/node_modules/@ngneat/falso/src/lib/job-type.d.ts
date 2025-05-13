import { FakeOptions } from './core/core';
/**
 * Generate a random job type.
 *
 * @category jobs
 *
 * @example
 *
 * randJobType()
 *
 * @example
 *
 * randJobType({ length: 10 })
 *
 */
export declare function randJobType<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
