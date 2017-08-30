import { BaseEntity } from './../../shared';

export class Wine implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public appellation?: string,
        public producer?: string,
        public creatorId?: number,
        public region?: BaseEntity,
        public color?: BaseEntity,
    ) {
    }
}
