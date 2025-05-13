import { FakeOptions } from './core/core';
/**
 * Generate a random department.
 *
 * @category commerce
 *
 * @example
 *
 * randDepartment()
 *
 * @example
 *
 * randDepartment({ length: 10 })
 *
 */
export declare function randDepartment<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
