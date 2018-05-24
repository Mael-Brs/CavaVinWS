import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { WineAgingData } from './wine-aging-data.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class WineAgingDataService {

    private resourceUrl = SERVER_API_URL + 'api/wine-aging-data';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/wine-aging-data';

    constructor(private http: Http) { }

    create(wineAgingData: WineAgingData): Observable<WineAgingData> {
        const copy = this.convert(wineAgingData);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(wineAgingData: WineAgingData): Observable<WineAgingData> {
        const copy = this.convert(wineAgingData);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<WineAgingData> {
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
     * Convert a returned JSON object to WineAgingData.
     */
    private convertItemFromServer(json: any): WineAgingData {
        const entity: WineAgingData = Object.assign(new WineAgingData(), json);
        return entity;
    }

    /**
     * Convert a WineAgingData to a JSON which can be sent to the server.
     */
    private convert(wineAgingData: WineAgingData): WineAgingData {
        const copy: WineAgingData = Object.assign({}, wineAgingData);
        return copy;
    }
}
