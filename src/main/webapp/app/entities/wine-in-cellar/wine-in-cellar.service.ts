import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { WineInCellar } from './wine-in-cellar.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class WineInCellarService {

    private resourceUrl = 'api/wine-in-cellars';
    private resourceSearchUrl = 'api/_search/wine-in-cellars';

    constructor(private http: Http) { }

    create(wineInCellar: WineInCellar): Observable<WineInCellar> {
        const copy = this.convert(wineInCellar);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(wineInCellar: WineInCellar): Observable<WineInCellar> {
        const copy = this.convert(wineInCellar);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<WineInCellar> {
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

    private convert(wineInCellar: WineInCellar): WineInCellar {
        const copy: WineInCellar = Object.assign({}, wineInCellar);
        return copy;
    }
}
