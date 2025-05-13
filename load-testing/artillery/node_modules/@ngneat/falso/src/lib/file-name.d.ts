import { FakeOptions } from './core/core';
interface FileNameOptions extends FakeOptions {
    extension?: string;
}
/**
 * Generate a random file name.
 *
 * @category system
 *
 * @example
 *
 * randFileName()
 *
 * @example
 *
 * randFileName({ length: 10 })
 *
 */
export declare function randFileName<Options extends FileNameOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
export {};
