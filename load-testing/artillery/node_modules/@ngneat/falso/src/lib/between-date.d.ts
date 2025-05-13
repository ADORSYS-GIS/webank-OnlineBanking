import { FakeOptions } from './core/core';
interface BetweenOptions extends FakeOptions {
    from: Date | string;
    to: Date | string;
}
/**
 * Generate a random date between ranges.
 *
 * @category date
 *
 * @example
 *
 * randBetweenDate({ from: new Date('10/07/2020'), to: new Date() })
 *
 * @example
 *
 * randBetweenDate({ from: new Date('10/07/2020'), to: new Date(), length: 10 })
 *
 */
export declare function randBetweenDate<Options extends BetweenOptions = never>(options: Options): import("./core/core").Return<Date, Options>;
export {};
