import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators,ReactiveFormsModule } from '@angular/forms';

import { Olona } from '../model/Olona'; 
import { OlonaService } from '../service/OlonaService'; 
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-book',
  standalone: true,
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.css'],
  imports : [CommonModule,ReactiveFormsModule]
})
export class Add {
  olonaForm!: FormGroup; 

  constructor(private formBuilder: FormBuilder, private olonaservice: OlonaService,private router:Router) {
    this.olonaForm = this.formBuilder.group({
      nom: ['', Validators.required],
      prenom: ['', Validators.required]
    });
  }

  retour(): void {
    this.router.navigateByUrl('listeOlona');
  }

  submit() {
    if (this.olonaForm.valid) {
      const olonaData = this.olonaForm.value as Olona;
      this.olonaservice.create(olonaData).subscribe({
        next: (response) => {
          console.log('Create successful:', response); 
          this.router.navigateByUrl(`listeOlona`);
        },
        error: (error) => {
          console.error('Error:', error);
        }
      });
    }
  }
}
