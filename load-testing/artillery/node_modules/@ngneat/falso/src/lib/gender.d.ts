import { FakeOptions } from './core/core';
interface GenderOptions extends FakeOptions {
    code?: boolean;
}
/**
 * Generate a random gender.
 *
 * @category person
 *
 * @example
 *
 * randGender()
 *
 * @example
 *
 * randGender({ length: 10 })
 *
 * @example
 *
 * randGender({ code: true }) // default is false
 *
 */
export declare function randGender<Options extends GenderOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
export {};
