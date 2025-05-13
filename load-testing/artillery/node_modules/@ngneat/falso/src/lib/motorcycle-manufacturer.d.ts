import { FakeOptions } from './core/core';
/**
 * Generate a random motorcycle manufacturer.
 *
 * @category motorcycle
 *
 * @example
 *
 * randMotorcycleManufacturer()
 *
 * @example
 *
 * randMotorcycleManufacturer({ length: 10 })
 *
 */
export declare function randMotorcycleManufacturer<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
/**
 * @deprecated there is a typo in the function name, use randMotorcycleManufacturer;
 * Generate a random motorcylce manufacturer.
 *
 * @category motorcycle
 *
 * @example
 *
 * randMotorcylceManufacturer()
 *
 * @example
 *
 * randMotorcylceManufacturer({ length: 10 })
 *
 */
export declare function randMotorcylceManufacturer<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
