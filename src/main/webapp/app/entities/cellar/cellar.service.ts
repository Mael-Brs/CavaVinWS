import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Cellar } from './cellar.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CellarService {

    private resourceUrl = SERVER_API_URL + 'api/cellars';

    constructor(private http: Http) { }

    create(cellar: Cellar): Observable<Cellar> {
        const copy = this.convert(cellar);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(cellar: Cellar): Observable<Cellar> {
        const copy = this.convert(cellar);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Cellar> {
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

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Cellar.
     */
    private convertItemFromServer(json: any): Cellar {
        const entity: Cellar = Object.assign(new Cellar(), json);
        return entity;
    }

    /**
     * Convert a Cellar to a JSON which can be sent to the server.
     */
    private convert(cellar: Cellar): Cellar {
        const copy: Cellar = Object.assign({}, cellar);
        return copy;
    }
}
