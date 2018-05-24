import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { WineInCellar } from './wine-in-cellar.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class WineInCellarService {

    private resourceUrl = SERVER_API_URL + 'api/wine-in-cellars';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/wine-in-cellars';

    constructor(private http: Http) { }

    create(wineInCellar: WineInCellar): Observable<WineInCellar> {
        const copy = this.convert(wineInCellar);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(wineInCellar: WineInCellar): Observable<WineInCellar> {
        const copy = this.convert(wineInCellar);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<WineInCellar> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
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
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to WineInCellar.
     */
    private convertItemFromServer(json: any): WineInCellar {
        const entity: WineInCellar = Object.assign(new WineInCellar(), json);
        return entity;
    }

    /**
     * Convert a WineInCellar to a JSON which can be sent to the server.
     */
    private convert(wineInCellar: WineInCellar): WineInCellar {
        const copy: WineInCellar = Object.assign({}, wineInCellar);
        return copy;
    }
}
