import { FakeOptions } from './core/core';
export type Airline = 'RyanAir' | 'British Airways' | 'Iberia' | 'EasyJet' | 'Jet2' | 'Emirates' | 'American Airlines' | 'JetBlue' | 'Air Europa' | 'Delta Air Lines' | 'United Airlines' | 'Thai Airways' | 'Qatar Airways' | 'Virgin Atlantic';
export interface FlightNumberOptions extends FakeOptions {
    airline?: Airline;
}
/**
 * Generate a random flight number.
 *
 * @category flight
 *
 * @example
 *
 * randFlightNumber()
 *
 * @example
 *
 * randFlightNumber({ airline: 'RyanAir' })
 *
 * @example
 *
 * randFlightNumber({ length: 10 })
 *
 */
export declare function randFlightNumber<Options extends FlightNumberOptions = never>(options?: Options): import("./core/core").Return<string, Options>;
