import { BaseEntity } from './../../shared';

export class Cellar implements BaseEntity {
    constructor(
        public id?: number,
        public capacity?: number,
        public userId?: number,
    ) {
    }
}
