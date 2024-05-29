import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { OlonaService } from '../service/OlonaService';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Log } from '../model/Log';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [CommonModule, ReactiveFormsModule]
})
export class Login {
  logForm!: FormGroup;
  errorMessage: string | null = null;

  constructor(private formBuilder: FormBuilder, private olonaservice: OlonaService, private router: Router) {
    this.logForm = this.formBuilder.group({
      email: ['', Validators.required],
      passwords: ['', Validators.required]
    });
  }

  retour(): void {
    this.router.navigateByUrl('listeOlona');
  }

  submit() {
    if (this.logForm.valid) {
      const logData = this.logForm.value as Log;
      this.olonaservice.checklogin(logData).subscribe({
        next: (response) => {
          if (response.data) {
            console.log('Login successful:', response);
            console.log('Email:', logData.email);
            console.log('Mot de passe:', logData.passwords);
            this.router.navigateByUrl('listeOlona');
          } else {
            console.error('Login failed');
            this.errorMessage = 'Login failed,Please try again';
            this.router.navigateByUrl('login');
          }
        },
        error: (error) => {
          console.error('Error:', error);
          this.errorMessage = 'An error occurred. Please try again.';
          this.router.navigateByUrl('login');
        }
      });
    }
  }
}
