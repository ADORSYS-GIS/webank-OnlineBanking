import { FakeOptions } from './core/core';
export interface NameOptions extends FakeOptions {
    withAccents?: boolean;
    gender?: 'male' | 'female';
}
/**
 * Generate a random full name.
 *
 * @category person
 *
 * @example
 *
 * randFullName()
 *
 * @example
 *
 * randFullName({ gender: 'female' }) // Emma Marková
 *
 * @example
 *
 * randFullName({ withAccents: false }) // return full name without special symbols like â, î or ô and etc
 *
 * @example
 *
 * randFullName({ length: 10 })
 *
 */
export declare function randFullName<Options extends NameOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
