import { FakeOptions } from './core/core';
/**
 * Generate a random integration.
 *
 * @category internet
 *
 * @example
 *
 * randIntegrations()
 *
 * @example
 *
 * randIntegrations({ length: 10 })
 *
 */
export declare function randIntegration<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
