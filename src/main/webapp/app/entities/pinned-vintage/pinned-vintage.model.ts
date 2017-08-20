import { BaseEntity } from './../../shared';

export class PinnedVintage implements BaseEntity {
    constructor(
        public id?: number,
        public userId?: number,
        public vintage?: BaseEntity,
    ) {
    }
}
