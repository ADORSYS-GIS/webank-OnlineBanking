import { FakeOptions } from './core/core';
interface AvatarOptions extends FakeOptions {
    size?: number;
}
/**
 * Generate a random avatar.
 *
 * @category person
 *
 * @example
 *
 * randAvatar()
 *
 * @example
 *
 * randAvatar({ length: 10 })
 *
 * @example
 *
 * randAvatar({ size: 200 }) // default is 100
 *
 */
export declare function randAvatar<Options extends AvatarOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
export {};
