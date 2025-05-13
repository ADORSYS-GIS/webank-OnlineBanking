export interface FakeOptions {
    length?: number;
    locale?: any | string[];
    maxCharCount?: number;
}
export type markRequired<Type, Key extends keyof Type> = Type & {
    [Property in Key]-?: Type[Property];
};
export type Return<T, O extends FakeOptions> = [O] extends [never] ? T : O['length'] extends number ? T[] : T;
type FactoryFunction<T> = (i: number) => T;
export declare function fake<T, Options extends FakeOptions>(data: Readonly<T[]> | FactoryFunction<T>, options?: Options): Return<T, Options>;
export declare function randElement<T>(arr: Readonly<T[]>): T;
export interface RandomInRangeOptions {
    min?: number;
    max?: number;
    fraction?: number;
}
export declare function getRandomInRange({ min, max, fraction, }?: RandomInRangeOptions): number;
export {};
