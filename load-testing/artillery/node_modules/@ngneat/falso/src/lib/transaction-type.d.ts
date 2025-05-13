import { FakeOptions } from './core/core';
/**
 * Generate a random transaction type.
 *
 * @category Finance
 *
 * @example
 *
 * randTransactionType()
 *
 * @example
 *
 * randTransactionType({ length: 10 })
 *
 */
export declare function randTransactionType<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
