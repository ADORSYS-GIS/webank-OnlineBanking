import { FakeOptions } from './core/core';
/**
 * Generate a random vehicle type.
 *
 * @category vehicle
 *
 * @example
 *
 * randVehicleType()
 *
 * @example
 *
 * randVehicleType({ length: 10 })
 *
 */
export declare function randVehicleType<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
