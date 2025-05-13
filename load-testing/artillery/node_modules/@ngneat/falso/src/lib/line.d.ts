import { FakeOptions } from './core/core';
export interface LineOptions extends FakeOptions {
    lineCount?: number;
}
/**
 * Generate random lines.
 *
 * @category text
 *
 * @example
 *
 * randLine()
 *
 * @example
 *
 * randLine({ lineCount: 10 }) // default is 5
 *
 */
export declare function randLine<Options extends LineOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
