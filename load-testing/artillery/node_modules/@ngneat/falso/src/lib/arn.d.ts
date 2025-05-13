import { FakeOptions } from './core/core';
/**
 * Generate a random ARN.
 *
 * @category AWS
 *
 * @example
 *
 * randArn()
 *
 * @example
 *
 * randArn({ length: 10 })
 *
 */
export declare function randArn<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
