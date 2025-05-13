import { FakeOptions } from '../../core/core';
interface BookOptions extends FakeOptions {
    category?: 'Comedy' | 'Crime' | 'Family and Self' | 'Love' | 'Science Fiction and Fantasy' | 'State of the Nation' | 'War and Travel';
}
export interface Book {
    title: string;
    author: string;
    category: BookOptions['category'];
}
/**
 * Generate a random book.
 *
 * @category entities
 *
 * @example
 *
 * randBook()
 *
 * @example
 *
 * randBook({ length: 10 })
 *
 * @example
 *
 * randBook({ category: 'Comedy' })
 *
 */
export declare function randBook<Options extends BookOptions = never>(options?: Options): import("../../core/core").Return<Book, Options>;
export {};
