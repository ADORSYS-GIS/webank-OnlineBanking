import { FakeOptions } from './core/core';
/**
 * Generate a random vehicle manufacturer.
 *
 * @category vehicle
 *
 * @example
 *
 * randVehicleManufacturer()
 *
 * @example
 *
 * randVehicleManufacturer({ length: 10 })
 *
 */
export declare function randVehicleManufacturer<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
