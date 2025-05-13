import { FakeOptions } from './core/core';
export interface Todo {
    id: string;
    title: string;
    completed: boolean;
}
/**
 * Generate a random todo.
 *
 * @category entities
 *
 * @example
 *
 * randTodo()
 *
 * @example
 *
 * randTodo({ length: 10 })
 *
 */
export declare function randTodo<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<Todo, Options>;
