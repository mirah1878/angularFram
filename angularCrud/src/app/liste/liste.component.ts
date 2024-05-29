import { Component, OnInit } from '@angular/core';
import { Olona } from '../model/Olona';
import { OlonaService } from '../service/OlonaService';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-liste',
  standalone : true,
  templateUrl: './liste.component.html',
  styleUrls: ['./liste.component.css'],
  imports : [CommonModule]
})
export class ListeComponent {
  listeOlona: Olona[] = [];
  olonaCount: number = 0;
  currentPage: number = 0;
  pageSize: number = 3;

  constructor(private olonaService: OlonaService, private router: Router) {}

  ngOnInit(): void {
    this.olonacunt();
    this.loadPage();
  }

  loadPage(): void {
    const offset = this.currentPage * this.pageSize;
    this.olonaService.pagination(this.pageSize, offset).subscribe({
      next: (data: Olona[]) => {
        this.listeOlona = data;
      },
      error: (error: any) => {
        console.error('Erreur:', error);
      }
    });
  }

  nextPage(): void {
    if ((this.currentPage + 1) * this.pageSize < this.olonaCount) {
      this.currentPage++;
      this.loadPage();
    }
  }

  prevPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadPage();
    }
  }

  olonacunt(): void {
    this.olonaService.olonacunt().subscribe({
      next: (count: number) => {
        this.olonaCount = count;
      },
      error: (error: any) => {
        console.error('Erreur:', error);
      }
    });
  }

  insertOlona(): void {
    this.router.navigateByUrl('versCreate');
  }

  deleteOlona(id: number): void {
    this.olonaService.delete(id).subscribe({
      next: (response: any) => {
        console.log('Suppression réussie:', response);
        this.loadPage();
      },
      error: (error: any) => {
        console.error('Erreur:', error);
      }
    });
  }

  versUpdate(id: number): void {
    this.router.navigateByUrl(`/versUpdate/${id}`);
  }

  selectnbre(id: number): void {
    this.router.navigateByUrl(`versNbre,${id}`);
  }

}
