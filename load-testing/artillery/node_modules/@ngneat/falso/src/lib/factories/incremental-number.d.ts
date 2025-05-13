export interface IncrementalNumberOptions {
    from: number;
    to?: number;
    step: number;
}
/**
 * Generate incremental numbers.
 *
 * @category factories
 *
 * @example
 *
 * const factory = incrementalNumber()
 * factory() // returns 1
 * factory() // returns 2
 *
 * @example
 *
 * const factory = incrementalNumber({from: 10, to: 100, step: 10})
 * factory() // returns 10
 * factory() // returns 20
 * ...
 *
 */
export declare function incrementalNumber(options: Required<IncrementalNumberOptions>): () => number | undefined;
export declare function incrementalNumber(options?: IncrementalNumberOptions): () => number;
