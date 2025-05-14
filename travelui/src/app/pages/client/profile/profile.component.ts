import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from 'src/app/core/services/user.service';
import { JwtHelperService } from 'src/app/core/services/jwt-helper.service';
import { User } from 'src/app/core/models/interface/user';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent implements OnInit {
  user: User | null = null;
  profileForm!: FormGroup;
  passwordForm!: FormGroup;
  loading = false;
  passwordLoading = false;
  isEditing = false;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private jwtHelperService: JwtHelperService,
  ) { }

  ngOnInit(): void {
    this.initForms();
    this.loadUserProfile();
  }

  private initForms(): void {
    this.profileForm = this.fb.group({
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', Validators.required],
      dateOfBirth: [null]
    });

    this.passwordForm = this.fb.group({
      currentPassword: ['', Validators.required],
      newPassword: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', Validators.required]
    }, { validator: this.passwordMatchValidator });
  }

  private passwordMatchValidator(form: FormGroup) {
    return form.get('newPassword')?.value === form.get('confirmPassword')?.value
      ? null : { mismatch: true };
  }

  private loadUserProfile(): void {
    this.loading = true;
    
    try {
      this.user = {
        id: this.jwtHelperService.getUserId(),
        firstname: this.jwtHelperService.getFirstName(),
        lastname: this.jwtHelperService.getLastName(),
        phoneNumber: this.jwtHelperService.getPhoneNumber(),
        email: this.jwtHelperService.getUserEmail(),
        loginProvider: this.jwtHelperService.getLoginProvider(),
        password: ''
      };

      this.profileForm.patchValue({
        firstname: this.user.firstname,
        lastname: this.user.lastname,
        email: this.user.email,
        phoneNumber: this.user.phoneNumber,
      });
    } catch (error) {
      console.error('Error loading user profile:', error);
    } finally {
      this.loading = false;
    }
  }

  toggleEdit(): void {
    this.isEditing = !this.isEditing;
    if (!this.isEditing && this.user) {
      this.profileForm.patchValue({
        firstname: this.user.firstname,
        lastname: this.user.lastname,
        email: this.user.email,
        phoneNumber: this.user.phoneNumber,
      });
    }
  }

  saveProfile(): void {
    if (this.profileForm.invalid || !this.user) return;

    this.loading = true;
    const updatedUser: User = {
      ...this.user,
      ...this.profileForm.value,
      dateOfBirth: this.profileForm.value.dateOfBirth
        ? this.profileForm.value.dateOfBirth.toISOString().split('T')[0]
        : null
    };

    this.userService.updateUser(updatedUser).subscribe({
      next: (res: User) => {
        this.user = res;
        this.isEditing = false;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error updating profile:', error);
        this.loading = false;
      }
    });
  }

  changePassword(): void {
    if (this.passwordForm.invalid || !this.user?.id) {
      return;
    }

    this.passwordLoading = true;
    const { currentPassword, newPassword } = this.passwordForm.value;

    this.userService.changePassword(this.user.id, currentPassword, newPassword).subscribe({
      next: () => {
        this.passwordForm.reset();
        this.passwordLoading = false;
      },
      error: (error) => {
        console.error('Error changing password:', error);
        this.passwordLoading = false;
      }
    });
  }
}