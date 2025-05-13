import { FakeOptions } from './core/core';
import { Airline } from './flight-number';
import { Airport } from './airport';
export interface FlightDetailsOptions extends FakeOptions {
    airline?: Airline;
    passenger?: string;
}
export interface FlightDetails {
    passenger: string;
    airline: string;
    flightNumber: string;
    origin: Airport;
    destination: Airport;
    date: string;
    seat: string;
    flightLength: number;
}
/**
 * Generate a random flight details.
 *
 * @category flight, entities
 *
 * @example
 *
 * randFlightDetails()
 *
 * @example
 *
 * randFlightDetails({ passenger: 'Ryan Smee' })
 *
 * @example
 *
 * randFlightDetails({ airline: 'RyanAir' })
 *
 * @example
 *
 * randFlightDetails({ length: 10 })
 *
 */
export declare function randFlightDetails<Options extends FlightDetailsOptions = never>(options?: Options): import("./core/core").Return<FlightDetails, Options>;
