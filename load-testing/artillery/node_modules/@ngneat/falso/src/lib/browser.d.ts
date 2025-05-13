import { FakeOptions } from './core/core';
/**
 * Generate a random browser.
 *
 * @category internet
 *
 * @example
 *
 * randBrowser()
 *
 * @example
 *
 * randBrowser({ length: 10 })
 *
 */
export declare function randBrowser<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
