import { FakeOptions } from './core/core';
export interface Product {
    id: string;
    title: string;
    description: string;
    price: string;
    category: string;
    image: string;
    rating: {
        rate: string;
        count: string;
    };
}
/**
 * Generate a random product.
 *
 * @category entities
 *
 * @example
 *
 * randProduct()
 *
 * @example
 *
 * randProduct({ length: 10 })
 *
 */
export declare function randProduct<Options extends FakeOptions = never>(options?: Options): import("./core/core").Return<{
    id: string;
    title: string;
    description: string;
    price: string;
    category: string;
    image: string;
    rating: {
        rate: string;
        count: string;
    };
}, Options>;
