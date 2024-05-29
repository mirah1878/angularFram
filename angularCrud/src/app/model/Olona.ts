export class Olona{
    id?:number;
    nom!:string;
    prenom!:string;

    constructor(nom:string,prenom:string,id?:number){
        this.id=id;
        this.nom = nom;
        this.prenom = prenom;
    }
}