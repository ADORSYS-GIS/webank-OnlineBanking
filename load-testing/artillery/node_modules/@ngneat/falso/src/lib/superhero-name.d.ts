import { SuperheroOptions } from './superhero';
/**
 * Generate a random superhero name.
 *
 * @category comic book
 *
 * @example
 *
 * randSuperheroName()
 *
 * @example
 *
 * randSuperhero({ company: 'Marvel' })
 *
 * @example
 *
 * randSuperheroName({ length: 10 })
 *
 */
export declare function randSuperheroName<Options extends SuperheroOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
