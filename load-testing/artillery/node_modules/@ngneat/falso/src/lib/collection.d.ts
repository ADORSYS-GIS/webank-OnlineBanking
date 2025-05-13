import { FakeOptions } from './core/core';
/**
 * Generate a collection from a custom generators functions
 *
 * @category util
 *
 * @example
 *
 * toCollection(() => {
 *   return { data: randNumber(); }
 * })
 *
 * @example
 *
 * toCollection(() => {
 *   return { data: randNumber(); }
 * }, { length: 10 }) // default is no length.
 *
 */
export declare function toCollection<Collection = never, Options extends FakeOptions = never>(generateCollection: (options?: Options) => Collection, options?: Options): Collection | Collection[];
