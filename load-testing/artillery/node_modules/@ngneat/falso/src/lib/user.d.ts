import { FakeOptions } from './core/core';
export interface User {
    id: string;
    firstName: string;
    lastName: string;
    username: string;
    email: string;
    img: string;
    address: {
        street: string;
        city: string;
        zipCode: string;
    };
    phone: string;
}
/**
 * Generate a random user.
 *
 * @category entities, user
 *
 * @example
 *
 * randUser()
 *
 * @example
 *
 * randUser({ length: 10 })
 *
 */
export declare function randUser<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<User, Options>;
