import { FakeOptions } from './core/core';
import { User } from './user';
export interface Post {
    id: string;
    title: string;
    body: string;
    comments: Array<{
        id: string;
        text: string;
        user: User;
    }>;
}
/**
 * Generate a random post.
 *
 * @category entities, internet
 *
 * @example
 *
 * randPost()
 *
 * @example
 *
 * randPost({ length: 10 })
 *
 */
export declare function randPost<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<Post, Options>;
