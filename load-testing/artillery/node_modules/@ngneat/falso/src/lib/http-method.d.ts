import { FakeOptions } from './core/core';
/**
 * Generate a random http method.
 *
 * @category internet
 *
 * @example
 *
 * randHttpMethod()
 *
 * @example
 *
 * randHttpMethod({ length: 10 })
 *
 */
export declare function randHttpMethod<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
