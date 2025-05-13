import { FakeOptions } from './core/core';
export interface PasswordOptions extends FakeOptions {
    size?: number;
}
/**
 * Generate a random password.
 *
 * @category person
 *
 * @example
 *
 * randPassword()
 *
 * @example
 *
 * randPassword({ length: 10 })
 *
 * @example
 *
 * randPassword({ size: 10 }) // default is 15
 *
 */
export declare function randPassword<Options extends PasswordOptions = never>(options?: Options): import("./core/core").Return<string[], Options>;
