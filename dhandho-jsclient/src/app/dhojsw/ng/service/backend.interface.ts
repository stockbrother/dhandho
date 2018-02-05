import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
@Injectable()
export class BackendInterface {
    http: HttpClient;
    constructor(http: HttpClient) {
        this.http = http;
    }

    post(url: string, body: string): Observable<Object> {
        return this.http.post(url, body);
    }

}

export class MockBackend extends BackendInterface {

}
