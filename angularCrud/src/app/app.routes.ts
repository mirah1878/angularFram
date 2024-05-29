import { Routes } from '@angular/router';
import { ListeComponent } from './liste/liste.component';
import { Update } from './update/update.component';
import { Login } from './login/login.component';
import { Add } from './add/add';
import { Nbre } from './nbre/nbre.component';

export const routes: Routes = [
    {'path':"",component: ListeComponent},
    {'path':"login",component: Login},
    {'path':"listeOlona",component:ListeComponent},
    {'path':"versUpdate/:id",component:Update},
    {'path':"versCreate",component:Add},
    {'path':"versNbre",component:Nbre}
];
