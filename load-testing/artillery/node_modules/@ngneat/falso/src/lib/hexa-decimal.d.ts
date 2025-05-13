import { FakeOptions } from './core/core';
/**
 * Generate a random hexa decimal.
 *
 * @category system
 *
 * @example
 *
 * randHexaDecimal()
 *
 * @example
 *
 * randHexaDecimal({ length: 10 })
 *
 */
export declare function randHexaDecimal<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
