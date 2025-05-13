import { FakeOptions } from './core/core';
/**
 * Generate a random snake.
 *
 * @category animals
 *
 * @example
 *
 * randSnake()
 *
 * @example
 *
 * randSnake({ length: 10 })
 *
 */
export declare function randSnake<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
