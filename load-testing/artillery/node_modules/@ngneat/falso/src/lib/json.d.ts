import { FakeOptions } from './core/core';
export interface RandomJSONOptions extends FakeOptions {
    totalKeys?: number;
    minKeys?: number;
    maxKeys?: number;
}
/**
 * Generate a random JSON Object.
 *
 * @category Datatype
 *
 * @example
 * randJSON()
 *
 * @example If a fixed number of keys are required
 *
 * randJSON({ totalKeys: 10 })
 *
 * @example If a random number of keys are required
 *
 * randJSON({ minKeys: 1, maxKeys: 10 })
 *
 */
export declare function randJSON<Options extends RandomJSONOptions = never>(options?: Options): import("./core/core").Return<{
    [key: string]: any;
}, Options>;
