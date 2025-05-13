import { FakeOptions } from './core/core';
/**
 * Generate a random vehicle model.
 *
 * @category vehicle
 *
 * @example
 *
 * randVehicleModel()
 *
 * @example
 *
 * randVehicleModel({ length: 10 })
 *
 */
export declare function randVehicleModel<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
