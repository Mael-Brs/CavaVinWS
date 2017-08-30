import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Vintage } from './vintage.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class VintageService {

    private resourceUrl = 'api/vintages';
    private resourceSearchUrl = 'api/_search/vintages';

    constructor(private http: Http) { }

    create(vintage: Vintage): Observable<Vintage> {
        const copy = this.convert(vintage);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(vintage: Vintage): Observable<Vintage> {
        const copy = this.convert(vintage);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Vintage> {
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

    private convert(vintage: Vintage): Vintage {
        const copy: Vintage = Object.assign({}, vintage);
        return copy;
    }
}
