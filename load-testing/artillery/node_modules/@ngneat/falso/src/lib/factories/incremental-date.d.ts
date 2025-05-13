export interface IncrementalDateOptions {
    from: Date | string | number;
    to?: Date | string | number;
    step: number;
}
/**
 * Generate incremental numbers.
 *
 * @category factories
 *
 * @example
 *
 * const factory = incrementalDate()
 * // seeds factory with new Date()
 * factory() // returns seed
 * factory() // returns seed + 1 day
 *
 * @example
 *
 * const factory = incrementalDate({from: new Date(2022,1,1), to: new Date(2022,1,3), step: 10})
 * // seeds factory with `from` value
 * factory() // returns seed
 * factory() // returns seed + 10ms
 * ...
 *
 */
export declare function incrementalDate(options: Required<IncrementalDateOptions>): () => Date | undefined;
export declare function incrementalDate(options?: IncrementalDateOptions): () => Date;
