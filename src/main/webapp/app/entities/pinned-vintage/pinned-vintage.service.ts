import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { PinnedVintage } from './pinned-vintage.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class PinnedVintageService {

    private resourceUrl = 'api/pinned-vintages';
    private resourceSearchUrl = 'api/_search/pinned-vintages';

    constructor(private http: Http) { }

    create(pinnedVintage: PinnedVintage): Observable<PinnedVintage> {
        const copy = this.convert(pinnedVintage);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(pinnedVintage: PinnedVintage): Observable<PinnedVintage> {
        const copy = this.convert(pinnedVintage);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<PinnedVintage> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(pinnedVintage: PinnedVintage): PinnedVintage {
        const copy: PinnedVintage = Object.assign({}, pinnedVintage);
        return copy;
    }
}
