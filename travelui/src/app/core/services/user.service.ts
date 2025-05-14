import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ApiService } from './api.service';
import { User } from '../models/interface/user';

@Injectable({
    providedIn: 'root'
})
export class UserService {
    private readonly endpoint = '/users';

    constructor(private apiService: ApiService) {}

    updateUser(user: User): Observable<User> {
        return this.apiService.put<User>(`${this.endpoint}/${user.id}`, user)
            .pipe(
                catchError(error => {
                    console.error('Update User Error:', error);
                    return throwError(() => new Error('Failed to update user'));
                })
            );
    }


    changePassword(userId: string, currentPassword: string, newPassword: string): Observable<any> {
        return this.apiService.post<any>(`${this.endpoint}/${userId}/change-password`, {
            currentPassword,
            newPassword
        }).pipe(
            catchError(error => {
                console.error('Change Password Error:', error);
                return throwError(() => new Error('Failed to change password'));
            })
        );
    }
}