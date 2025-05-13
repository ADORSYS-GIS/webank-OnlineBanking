import { FakeOptions } from './core/core';
/**
 * Generate a random vehicle.
 *
 * @category vehicle
 *
 * @example
 *
 * randVehicle()
 *
 * @example
 *
 * randVehicle({ length: 10 })
 *
 */
export declare function randVehicle<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
