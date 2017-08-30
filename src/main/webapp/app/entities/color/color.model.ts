import { BaseEntity } from './../../shared';

export class Color implements BaseEntity {
    constructor(
        public id?: number,
        public colorName?: string,
    ) {
    }
}
