package models;
import etu1789.dao.Generic2;
import etu1789.dao.annotation.Column;
import etu1789.dao.annotation.Table;
import etu1789.dao.annotation.ForeignKey;

@Table(libelle = "enfant", base = "postgresql")

public class Enfant extends Generic2{
    @Column(libelle = "id", primaryKey = true)
private Integer id;
public Integer getId(){ return id; }
public void setId(Integer o){ id=o; }
@ForeignKey(libelle = "id", type = Integer.class)
@Column(libelle = "idolona")
private Olona olona;
public Olona getOlona(){ return olona; }
public void setOlona(Olona o){ olona=o; }
@Column(libelle = "nbre")
private Integer nbre;
public Integer getNbre(){ return nbre; }
public void setNbre(Integer o){ nbre=o; }

    public Enfant(){}
public Enfant(Integer o){ id=o; }

}

