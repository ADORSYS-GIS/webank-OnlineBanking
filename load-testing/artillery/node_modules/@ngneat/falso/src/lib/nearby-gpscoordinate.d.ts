import { FakeOptions } from './core/core';
/**
 * Generate a random GPS coordinate.
 *
 * @category address
 *
 * @example
 *
 * randNearbyGPSCoordinate()
 *
 * @example
 *
 * randNearbyGPSCoordinate({ length: 10 })
 *
 */
export declare function randNearbyGPSCoordinate<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<number[], Options>;
