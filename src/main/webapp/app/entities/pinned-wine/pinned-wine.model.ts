import { BaseEntity } from './../../shared';

export class PinnedWine implements BaseEntity {
    constructor(
        public id?: number,
        public userId?: number,
        public wine?: BaseEntity,
    ) {
    }
}
