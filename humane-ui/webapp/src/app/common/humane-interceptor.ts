import { Observable, throwError } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpResponse } from '@angular/common/http';
import { catchError } from 'rxjs/operators';

@Injectable()
export class HumaneInterceptor implements HttpInterceptor {

  constructor() { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(catchError(err => {
        if (err.status === 401) {
            console.log('Unauthorized access');
        }
        return throwError(err.error);
    }));
  }

}
