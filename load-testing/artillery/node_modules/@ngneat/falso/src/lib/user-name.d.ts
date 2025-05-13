import { FakeOptions } from './core/core';
export interface UserNameOptions extends FakeOptions {
    firstName?: string;
    lastName?: string;
    withAccents?: boolean;
}
/**
 * Generate a random user name.
 *
 * @category person
 *
 * @example
 *
 * randUserName()
 *
 * @example
 *
 * randUserName({ length: 10 })
 *
 * @example
 *
 * randUserName({ firstName: 'Ryan' })
 *
 * @example
 *
 * randUserName({ lastName: 'Smee' })
 *
 * @example
 *
 * randUserName({ withAccents: false }) // return username without special symbols like â, î or ô and etc
 *
 */
export declare function randUserName<Options extends UserNameOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
