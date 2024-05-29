package models;
import etu1789.dao.Generic2;
import etu1789.dao.annotation.Column;
import etu1789.dao.annotation.Table;
import etu1789.dao.annotation.ForeignKey;

@Table(libelle = "avion", base = "postgresql")

public class Avion extends Generic2{
    @Column(libelle = "id", primaryKey = true)
private Integer id;
public Integer getId(){ return id; }
public void setId(Integer o){ id=o; }
@Column(libelle = "matricules")
private String matricules;
public String getMatricules(){ return matricules; }
public void setMatricules(String o){ matricules=o; }
@Column(libelle = "place")
private Integer place;
public Integer getPlace(){ return place; }
public void setPlace(Integer o){ place=o; }

    public Avion(){}
public Avion(Integer o){ id=o; }

}

