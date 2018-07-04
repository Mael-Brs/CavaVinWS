import {BaseEntity} from './../../shared';

export class WineInCellar implements BaseEntity {
    constructor(
        public id?: number,
        public minKeep?: number,
        public maxKeep?: number,
        public price?: number,
        public quantity?: number,
        public comments?: string,
        public location?: string,
        public cellarId?: number,
        public childYear?: number,
        public apogeeYear?: number,
        public vintage?: BaseEntity,
    ) {
    }
}
