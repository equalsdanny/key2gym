/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package census.persistence;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author Danylo Vashchilenko
 */
@Entity
@Table(name = "administrator_adm")
@NamedQueries({
    @NamedQuery(name = "Administrator.findAll", query = "SELECT a FROM Administrator a"),
    @NamedQuery(name = "Administrator.findById", query = "SELECT a FROM Administrator a WHERE a.id = :id"),
    @NamedQuery(name = "Administrator.findByUsernameAndPassword", query = "SELECT a FROM Administrator a WHERE a.username = :username AND a.password = :password"),
    @NamedQuery(name = "Administrator.findByPermissionsLevel", query = "SELECT a FROM Administrator a WHERE a.permissionsLevel = :permissionsLevel")})
public class Administrator implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_adm")
    private Short id;
    
    @Basic(optional = false)
    @Lob
    @Column(name = "username")
    private String username;
    
    @Basic(optional = false)
    @Lob
    @Column(name = "full_name")
    private String fullName;
    
    @Basic(optional = false)
    @Lob
    @Column(name = "password")
    private String password;
    
    @Basic(optional = false)
    @Lob
    @Column(name = "address")
    private String address;
    
    @Basic(optional = false)
    @Lob
    @Column(name = "telephone")
    private String telephone;
    
    @Basic(optional = false)
    @Lob
    @Column(name = "note")
    private String note;
    
    @Basic(optional = false)
    @Column(name = "permissions_level")
    private Short permissionsLevel;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "administrator")
    private List<Session> sessionsList;

    public Administrator() {
    }

    public Administrator(Short id) {
        this.id = id;
    }

    public Administrator(Short id, String username, String fullName, String password, String address, String telephone, String note, Short permissionsLevel) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.address = address;
        this.telephone = telephone;
        this.note = note;
        this.permissionsLevel = permissionsLevel;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Short getPermissionsLevel() {
        return permissionsLevel;
    }

    public void setPermissionsLevel(Short permissionsLevel) {
        this.permissionsLevel = permissionsLevel;
    }
    
    public List<Session> getSessions() {
        return sessionsList;
    }

    public void setSessions(List<Session> sessionList) {
        this.sessionsList = sessionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Administrator)) {
            return false;
        }
        Administrator other = (Administrator) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "census.persistence.Administrator[ id=" + id + " ]";
    }
}
