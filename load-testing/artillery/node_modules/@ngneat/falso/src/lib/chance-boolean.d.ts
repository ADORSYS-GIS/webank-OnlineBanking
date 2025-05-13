import { FakeOptions } from './core/core';
interface RandChangeBooleanOptions extends FakeOptions {
    chanceTrue?: number;
    chanceFalse?: number;
}
/**
 * Generate a random chance boolean.
 *
 * @category general
 *
 * @example
 *
 * randChanceBoolean({ chanceTrue: 0.78 }) // Will be true 78% of times
 *
 * @example
 *
 * randChanceBoolean({ chanceFalse: 0.156 }) // Will be false 15.6% of times
 *
 * @example
 *
 * randChanceBoolean({ length: 10 })
 *
 */
export declare function randChanceBoolean<Options extends RandChangeBooleanOptions = never>(options?: Options): import("./core/core").Return<boolean, Options>;
export {};
