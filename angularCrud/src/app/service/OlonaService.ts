import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Olona } from "../model/Olona";
import { Log } from "../model/Log";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";

const baseUrl = 'http://localhost:8080/ang/olona';
const pagUrl = 'http://localhost:8080/ang/olonaPaging';
const total = 'http://localhost:8080/ang/olonaCount';
const login = 'http://localhost:8080/ang/loginVue';


@Injectable({
    providedIn : 'root'
})
export class OlonaService{

    constructor(private http: HttpClient) { }

    getAll(): Observable<Olona[]> {
        return this.http.get<Olona[]>(baseUrl);
    }

    create(data: Olona): Observable<any> {
        return this.http.post(baseUrl, data);
    }

    update(id: any, data: any): Observable<any> {
        return this.http.put(`${baseUrl}?id=${id}`, data);
    }

    delete(id: any): Observable<any> {
        return this.http.delete(`${baseUrl}/${id}`);
    }

    pagination(limit: any, offset: any): Observable<Olona[]> {
      return this.http.get<Olona[]>(`${pagUrl}?limit=${limit}&offset=${offset}`).pipe(
        map((response: any) => response.data)
      );
    }

    olonacunt(): Observable<number> {
      return this.http.get<number>(total).pipe(
        map((response: any) => response.data)
      );
    }

  checklogin(data: Log): Observable<any> {
    return this.http.post(login, data);
  }


}
