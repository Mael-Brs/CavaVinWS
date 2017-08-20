import { BaseEntity } from './../../shared';

export class WineAgingData implements BaseEntity {
    constructor(
        public id?: number,
        public minKeep?: number,
        public maxKeep?: number,
        public color?: BaseEntity,
        public region?: BaseEntity,
    ) {
    }
}
