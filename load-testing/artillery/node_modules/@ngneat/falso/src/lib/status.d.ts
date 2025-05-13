import { FakeOptions } from './core/core';
type Type = 'Project' | 'User Story' | 'Task';
interface StatusOptions extends FakeOptions {
    type?: Type;
}
/**
 * Generate a random status.
 *
 * @category general
 *
 * @example
 *
 * randStatus()
 *
 * @example
 *
 * randStatus({ type: 'User Story' })
 *
 * @example
 *
 * randStatus({ length: 10 })
 *
 */
export declare function randStatus<Options extends StatusOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
export {};
