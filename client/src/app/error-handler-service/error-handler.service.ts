import {Injectable} from '@angular/core';
import {HttpErrorResponse} from "@angular/common/http";
import {Observable, Subject, throwError} from "rxjs";
import {ErrorResponse} from "../types/errorResponse";

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {

  errorSubject = new Subject<string>();
  observableError = this.errorSubject.asObservable();

  handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage;
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Błąd: ${error.error.message}`;
    } else {

      const errorResponse: ErrorResponse = error.error;
      console.log(errorResponse);
      switch (errorResponse.errorCode) {
        case '413':
          errorMessage = 'Podana wartość parametru wykracza poza zakres.'
          break;
        case '414':
          errorMessage = 'Podana maska jest nieprawidłowa';
          break;
        case '415':
          errorMessage = 'Podany adres nie jest adresem sieci';
          break;
        case '416':
          errorMessage = 'Stworzenie podsieci jest niemożliwe';
          break;
        default:
          errorMessage = 'Wystąpił problem podczas łączenia z serwerem';
          break;
      }
    }

    this.errorSubject.next(errorMessage);

    return throwError(() => new Error(errorMessage));
  }
}
