import { FakeOptions } from './core/core';
/**
 * Generate a random vehicle fuel.
 *
 * @category vehicle
 *
 * @example
 *
 * randVehicleFuel()
 *
 * @example
 *
 * randVehicleFuel({ length: 10 })
 *
 */
export declare function randVehicleFuel<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
