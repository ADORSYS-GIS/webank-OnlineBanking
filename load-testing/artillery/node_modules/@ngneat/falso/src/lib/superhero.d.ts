import { FakeOptions } from './core/core';
export type ComicBookCompany = 'Marvel' | 'DC';
export interface SuperheroOptions extends FakeOptions {
    company?: ComicBookCompany;
}
export interface Superhero {
    realName: string;
    alterEgo: string;
    company: string;
}
export interface SuperheroEntity extends Superhero {
    id: string;
}
/**
 * Generate a random superhero.
 *
 * @category entities, comic book
 *
 * @example
 *
 * randSuperhero()
 *
 * @example
 *
 * randSuperhero({ company: 'Marvel' })
 *
 * @example
 *
 * randSuperhero({ length: 10 })
 *
 */
export declare function randSuperhero<Options extends SuperheroOptions = never>(options?: Options): import("./core/core").Return<SuperheroEntity, Options>;
