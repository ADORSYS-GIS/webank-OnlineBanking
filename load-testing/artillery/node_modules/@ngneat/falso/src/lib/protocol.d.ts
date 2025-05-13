import { FakeOptions } from './core/core';
interface ProtocolOptions extends FakeOptions {
    fullName?: boolean;
}
/**
 * Generate a random protocol.
 *
 * @category internet
 *
 * @example
 *
 * randProtocol()
 *
 * @example
 *
 * randProtocol({ length: 10 })
 *
 * @example
 *
 * randProtocol({ fullName: true })
 *
 */
export declare function randProtocol<Options extends ProtocolOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
export {};
