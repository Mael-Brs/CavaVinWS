import { BaseEntity } from './../../shared';

export class Vintage implements BaseEntity {
    constructor(
        public id?: number,
        public year?: number,
        public childYear?: number,
        public apogeeYear?: number,
        public bareCode?: number,
        public wine?: BaseEntity,
    ) {
    }
}
