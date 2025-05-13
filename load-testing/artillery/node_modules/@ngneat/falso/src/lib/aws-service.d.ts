import { FakeOptions } from './core/core';
/**
 * Generate a random AWS service name.
 *
 * @category AWS
 *
 * @example
 *
 * randAwsService()
 *
 * @example
 *
 * randAwsService({ length: 10 })
 *
 */
export declare function randAwsService<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
