import { FakeOptions } from './core/core';
/**
 * Generate a random AWS request id.
 *
 * @category AWS
 *
 * @example
 *
 * randAwsRequestId()
 *
 * @example
 *
 * randAwsRequestId({ length: 10 })
 *
 */
export declare function randAwsRequestId<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
