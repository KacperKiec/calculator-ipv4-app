import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {catchError, Observable, tap} from "rxjs";
import {Subnet} from "../types/subnet";
import {ErrorHandlerService} from "../error-handler-service/error-handler.service";

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private apiURL = 'http://localhost:8080'

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  getNetAddress(address: string, mask: string): Observable<Subnet> {
    let params = new HttpParams();
    params = params.append('address', address);
    params = params.append('mask', mask);

    return this.http.get<Subnet>(`${this.apiURL}/net-address`, {params})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getBroadcastAddress(address: string, mask: string): Observable<Subnet> {
    let params = new HttpParams();
    params = params.append('address', address);
    params = params.append('mask', mask);

    return this.http.get<Subnet>(`${this.apiURL}/broadcast-address`, {params})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getSubnetsNets(address: string, mask: string, numberOfSubnets: number): Observable<Subnet[]> {
    let params = new HttpParams();
    params = params.append('address', address);
    params = params.append('mask', mask);
    params = params.append('numberOfSubnets', numberOfSubnets);

    return this.http.get<Subnet[]>(`${this.apiURL}/subnets-net`, {params})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getSubnetsHosts(address: string, mask: string, numberOfHosts: number): Observable<Subnet[]> {
    let params = new HttpParams();
    params = params.append('address', address);
    params = params.append('mask', mask);
    params = params.append('numberOfHosts', numberOfHosts);

    return this.http.get<Subnet[]>(`${this.apiURL}/subnets-host`, {params})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getSubnetsOptimal(address: string, mask: string, listOfHosts: number[]) {
    let params = new HttpParams();
    params = params.append('address', address);
    params = params.append('mask', mask);
    listOfHosts.forEach((subnet, index) => {
      params = params.append('listOfHosts', subnet);
    });

    return this.http.get<Subnet[]>(`${this.apiURL}/subnets-optimal`, {params})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }
}
