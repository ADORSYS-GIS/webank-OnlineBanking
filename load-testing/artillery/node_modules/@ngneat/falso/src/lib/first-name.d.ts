import { NameOptions } from './full-name';
/**
 * Generate a random first name.
 *
 * @category person
 *
 * @example
 *
 * randFirstName()
 *
 * @example
 *
 * randFirstName({ withAccents: true })
 *
 * @example
 *
 * randFirstName({ gender: 'female' }) // Emma
 *
 * @example
 *
 * randFirstName({ length: 10 })
 *
 */
export declare function randFirstName<Options extends NameOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
