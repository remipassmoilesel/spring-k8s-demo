import {IDocument} from './IDocument';

export interface IGpgValidationResult {
    isValid: boolean;
    document: IDocument;
}
