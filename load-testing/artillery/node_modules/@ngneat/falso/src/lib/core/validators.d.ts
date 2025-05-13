export declare function isNil<T>(value: T | null | undefined): value is null | undefined;
/**
 * Validates the Australian Business Number (ABN)
 * https://abr.business.gov.au/Help/AbnFormat
 * @param abnNumber the abn number to verify
 * @returns true if the abn is valid otherwise false
 */
export declare const isValidAbn: (abnNumber: string) => boolean;
