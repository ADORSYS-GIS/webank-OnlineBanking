import { FakeOptions } from './core/core';
/**
 * Generate a random subscription plan.
 *
 * @category company
 *
 * @example
 *
 * randSubscriptionPlan()
 *
 * @example
 *
 * randSubscriptionPlan({ length: 10 })
 *
 */
export declare function randSubscriptionPlan<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
