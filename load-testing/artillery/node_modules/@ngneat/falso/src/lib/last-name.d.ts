import { NameOptions } from './full-name';
/**
 * Generate a random last name.
 *
 * @category person
 *
 * @example
 *
 * randLastName()
 *
 * @example
 *
 * randLastName({ withAccents: false })
 *
 * @example
 *
 * randLastName({ length: 10 })
 *
 */
export declare function randLastName<Options extends NameOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
