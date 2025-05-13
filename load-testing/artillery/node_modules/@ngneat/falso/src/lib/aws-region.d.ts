import { FakeOptions } from './core/core';
/**
 * Generate a random AWS region.
 *
 * @category AWS
 *
 * @example
 *
 * randAwsRegion()
 *
 * @example
 *
 * randAwsRegion({ length: 10 })
 *
 */
export declare function randAwsRegion<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
