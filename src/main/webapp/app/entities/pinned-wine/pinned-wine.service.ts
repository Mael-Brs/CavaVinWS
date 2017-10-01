import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { PinnedWine } from './pinned-wine.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class PinnedWineService {

    private resourceUrl = 'api/pinned-wines';
    private resourceSearchUrl = 'api/_search/pinned-wines';

    constructor(private http: Http) { }

    create(pinnedWine: PinnedWine): Observable<PinnedWine> {
        const copy = this.convert(pinnedWine);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(pinnedWine: PinnedWine): Observable<PinnedWine> {
        const copy = this.convert(pinnedWine);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<PinnedWine> {
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

    private convert(pinnedWine: PinnedWine): PinnedWine {
        const copy: PinnedWine = Object.assign({}, pinnedWine);
        return copy;
    }
}
