import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators,ReactiveFormsModule } from '@angular/forms';
import { Olona } from '../model/Olona'; 
import { OlonaService } from '../service/OlonaService'; 
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-update',
  standalone: true,
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.css'],
  imports : [CommonModule,ReactiveFormsModule]
})
export class Update {
  id!: number;
  olonaForm!: FormGroup; 

  constructor(private formBuilder: FormBuilder, private olonaService: OlonaService, private router: Router, private route: ActivatedRoute) {
    this.olonaForm = this.formBuilder.group({
      nom: ['', Validators.required],
      prenom: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.id = +this.route.snapshot.params['id'];
  }

  retour(): void {
    this.router.navigateByUrl('listeOlona');
  }

  submit() {
    if (this.olonaForm.valid) {
      const olonaData = this.olonaForm.value as Olona;
      console.log('id :', this.id);
      console.log(' :', olonaData);
      this.olonaService.update(this.id, olonaData).subscribe({
          next: (response: any) => {
          console.log('Update successful:', response.data);
          this.router.navigateByUrl(`listeOlona`);
        },
        error: (error: any) => {
          console.error('Error:', error);
        }
      });
    }
  }  
}