import { BaseEntity } from './../../shared';

export class Vintage implements BaseEntity {
    constructor(
        public id?: number,
        public bareCode?: number,
        public year?: number,
        public wine?: BaseEntity,
    ) {
    }
}
