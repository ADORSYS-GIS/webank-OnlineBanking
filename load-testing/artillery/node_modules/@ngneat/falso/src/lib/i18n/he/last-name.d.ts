import { FakeOptions } from '../../core/core';
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
 * randLastName({ length: 10 })
 *
 */
export declare function randLastName<Options extends FakeOptions = never>(options?: Options): import("../../core/core").Return<string, Options>;
