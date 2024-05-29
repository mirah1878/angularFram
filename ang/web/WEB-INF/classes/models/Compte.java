package models;
import etu1789.dao.Generic2;
import etu1789.dao.annotation.Column;
import etu1789.dao.annotation.Table;
import etu1789.dao.annotation.ForeignKey;

@Table(libelle = "compte", base = "postgresql")

public class Compte extends Generic2{
    @Column(libelle = "id", primaryKey = true)
private Integer id;
public Integer getId(){ return id; }
public void setId(Integer o){ id=o; }
@Column(libelle = "email")
private String email;
public String getEmail(){ return email; }
public void setEmail(String o){ email=o; }
@Column(libelle = "mdp")
private String mdp;
public String getMdp(){ return mdp; }
public void setMdp(String o){ mdp=o; }

    public Compte(){}
public Compte(Integer o){ id=o; }

}

