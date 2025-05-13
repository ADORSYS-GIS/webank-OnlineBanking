import { FakeOptions } from './core/core';
/**
 * Generate a random rabbit.
 *
 * @category animals
 *
 * @example
 *
 * randRabbit()
 *
 * @example
 *
 * randRabbit({ length: 10 })
 *
 */
export declare function randRabbit<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
