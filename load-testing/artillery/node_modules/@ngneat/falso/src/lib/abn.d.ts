import { FakeOptions } from './core/core';
export interface AbnOptions extends FakeOptions {
    /**
     * Will generate only valid ABN's when `true`, otherwise randomised between valid and invalid.
     */
    onlyValid?: boolean;
}
/**
 * Generate a random abn.
 *
 * @category Finance
 *
 * @example
 *
 * randAbn()
 *
 * @example
 *
 * randAbn({ length: 10 })
 *
 * @example
 *
 * randAbn({ onlyValid: false }) //default is true
 */
export declare function randAbn<Options extends AbnOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
