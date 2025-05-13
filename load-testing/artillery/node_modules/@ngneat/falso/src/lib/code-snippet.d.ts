import { FakeOptions } from './core/core';
export declare const CODE_SNIPPET_LANGUAGES: readonly ["bash", "c", "c#", "c++", "go", "html", "java", "javascript", "php", "python", "rust", "sql", "swift"];
type CodeSnippetLang = typeof CODE_SNIPPET_LANGUAGES[number];
interface CodeSnippetOptions extends FakeOptions {
    lang?: CodeSnippetLang;
}
/**
 * Generate a random code snippet.
 *
 * @category code
 *
 * @example
 *
 * randCodeSnippet()
 *
 * @example
 *
 * randCodeSnippet({ length: 10 })
 *
 * @example
 *
 * randCodeSnippet({ lang: 'html' }) // default is 'javascript'
 *
 */
export declare function randCodeSnippet<Options extends CodeSnippetOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
export {};
