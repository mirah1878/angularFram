package models;
import etu1789.dao.Generic2;
import etu1789.dao.annotation.Column;
import etu1789.dao.annotation.Table;
import etu1789.dao.annotation.ForeignKey;

@Table(libelle = "olona", base = "postgresql")

public class Olona extends Generic2{
    @Column(libelle = "id", primaryKey = true)
private Integer id;
public Integer getId(){ return id; }
public void setId(Integer o){ id=o; }
@Column(libelle = "nom")
private String nom;
public String getNom(){ return nom; }
public void setNom(String o){ nom=o; }
@Column(libelle = "prenom")
private String prenom;
public String getPrenom(){ return prenom; }
public void setPrenom(String o){ prenom=o; }

    public Olona(){}
public Olona(Integer o){ id=o; }

}

