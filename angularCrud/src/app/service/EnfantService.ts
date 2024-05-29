import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Enfant } from "../model/Enfant";
import { Observable } from "rxjs";

const baseUrl = 'http://localhost:8080/ang/enfant';

@Injectable({
    providedIn : 'root'
})
export class EnfantService{

    constructor(private http: HttpClient) { }

    getAll(): Observable<Enfant[]> {
        return this.http.get<Enfant[]>(baseUrl);
    }

    create(data: Enfant): Observable<any> {
        return this.http.post(baseUrl, data);
    }

    update(id: any, data: any): Observable<any> {
        return this.http.put(`${baseUrl}?id=${id}`, data);
    }

    delete(id: any): Observable<any> {
        return this.http.delete(`${baseUrl}/${id}`);
    }

    getbyid(id: any): Observable<any> {
      return this.http.get(baseUrl,id);
    }
}
