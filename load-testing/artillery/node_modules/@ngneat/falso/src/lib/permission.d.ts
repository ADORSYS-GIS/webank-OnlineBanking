import { FakeOptions } from './core/core';
interface PermissionOptions extends FakeOptions {
    numeric?: boolean;
}
/**
 * Generate a random permission.
 *
 * @category system
 *
 * @example
 *
 * randPermission()
 *
 * @example
 *
 * randPermission({ length: 10 })
 *
 * @example
 *
 * randPermission({ numeric: true })
 *
 */
export declare function randPermission<Options extends PermissionOptions = never>(options?: Options): import("./core/core").Return<number, Options> | import("./core/core").Return<string, Options>;
export {};
