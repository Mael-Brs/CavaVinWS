import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Color } from './color.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ColorService {

    private resourceUrl = SERVER_API_URL + 'api/colors';

    constructor(private http: Http) { }

    create(color: Color): Observable<Color> {
        const copy = this.convert(color);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(color: Color): Observable<Color> {
        const copy = this.convert(color);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Color> {
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
     * Convert a returned JSON object to Color.
     */
    private convertItemFromServer(json: any): Color {
        const entity: Color = Object.assign(new Color(), json);
        return entity;
    }

    /**
     * Convert a Color to a JSON which can be sent to the server.
     */
    private convert(color: Color): Color {
        const copy: Color = Object.assign({}, color);
        return copy;
    }
}
