package models;
import etu1789.dao.Generic2;
import etu1789.dao.annotation.Column;
import etu1789.dao.annotation.Table;
import etu1789.dao.annotation.ForeignKey;

@Table(libelle = "login", base = "postgresql")

public class Login extends Generic2{
    @Column(libelle = "id_user", primaryKey = true)
private Integer idUser;
public Integer getIdUser(){ return idUser; }
public void setIdUser(Integer o){ idUser=o; }
@Column(libelle = "email")
private String email;
public String getEmail(){ return email; }
public void setEmail(String o){ email=o; }
@Column(libelle = "passwords")
private String passwords;
public String getPasswords(){ return passwords; }
public void setPasswords(String o){ passwords=o; }
@Column(libelle = "role")
private Integer role;
public Integer getRole(){ return role; }
public void setRole(Integer o){ role=o; }

    public Login(){}
public Login(Integer o){ idUser=o; }

}

