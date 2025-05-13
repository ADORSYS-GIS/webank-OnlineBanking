import { FakeOptions } from './core/core';
/**
 * Generate a random paragraph.
 *
 * @category text
 *
 * @example
 *
 * randParagraph()
 *
 * @example
 *
 * randParagraph({ length: 10 })
 *
 */
export declare function randParagraph<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
