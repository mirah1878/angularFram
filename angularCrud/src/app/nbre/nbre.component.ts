import { Component, OnInit } from '@angular/core';
import { Enfant } from '../model/Enfant';
import { EnfantService } from '../service/EnfantService';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-liste',
  standalone : true,
  templateUrl: './nbre.component.html',
  styleUrls: ['./nbre.component.css'],
  imports : [CommonModule]
})

export class Nbre {
  listeenfant: Enfant[] = [];

  constructor(private enfantService: EnfantService, private router: Router) {}

  ngOnInit(): void {
    this.getAllEnfant();
  }

  retour(): void {
    this.router.navigateByUrl("");
  }

  getAllEnfant(): void {
    this.enfantService.getAll().subscribe({
      next: (response: any) => {
        console.log('Données du serveur:', response.data);
        this.listeenfant = response.data;
      },
      error: (error: any) => {
        console.error('Erreur:', error);
      }
    });
  }


}
