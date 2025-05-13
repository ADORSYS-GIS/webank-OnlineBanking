import { FakeOptions, Return } from './core/core';
export declare const numericChars = "0123456789";
export declare const alphaChars = "abcdefghijklmnopqrstuvwxyz";
export declare const specialChars = "<>@!#$%^&*()_+[]{}?:;|'\"\\,./~`-=";
export declare const numericAlphaChars: string;
type CharTypes = 'numeric' | 'alpha' | 'alphaNumeric' | 'special';
type RandomSequenceOptions2 = {
    size?: number;
    charType?: CharTypes;
    chars?: string;
} & FakeOptions;
type ReturnTypeFromCharType<CharType extends CharTypes | undefined> = CharType extends 'numeric' | 'alpha' | 'alphaNumeric' | 'special' ? string : string[];
/**
 * Generate a random sequence.
 *
 * @category general
 *
 * @example
 *
 * randSequence()
 *
 * @example
 *
 * randSequence({ size: 10 })
 *
 * @example
 *
 * randSequence({ chars: 'aAbBcC@#' })
 *
 * @example
 *
 * randSequence({ charType: 'numeric' })
 *
 * @example
 *
 * randSequence({ length: 10 })
 *
 */
export declare function randSequence<Options extends RandomSequenceOptions2 = never>(options?: Options): Return<ReturnTypeFromCharType<Options['charType']>, Options>;
export {};
