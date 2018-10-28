/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.BatchSize;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;


/**
 *
 * @author Chema
 */
@Entity
@Indexed
@BatchSize(size=100)
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @DocumentId
    private Long id;
    private String nombre;
    private String clave;
    private String direccion;
    private String correo;
    private String aficiones;

    public Usuario() {
    }

    public Usuario(String nombre, String clave) {
        this.nombre = nombre;
        this.clave = clave;
    }

    public Usuario(String nombre, String clave, String direccion) {
        this.nombre = nombre;
        this.clave = clave;
        this.direccion = direccion;
    }

    @Length(max = 1024)
    @Field(index = Index.YES, store = Store.NO)
    public String getAficiones() {
        return aficiones;
    }

    public void setAficiones(String aficiones) {
        this.aficiones = aficiones;
    }

    @Email(message = "El formato es erróneo")
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @NotNull(message = "La dirección de una persona no puede ser nula")
    @Length(min = 4, max = 10, message = "La dirección debe estar comprendida entre 4 y 10 caracteres")
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        if (this.nombre != other.nombre && (this.nombre == null || !this.nombre.equals(other.nombre))) {
            return false;
        }
        return !(this.clave != other.clave && (this.clave == null || !this.clave.equals(other.clave)));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.nombre != null ? this.nombre.hashCode() : 0);
        hash = 67 * hash + (this.clave != null ? this.clave.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nombre=" + nombre + ", clave=" + clave + ", direccion=" + direccion + ", correo=" + correo + ", aficiones=" + aficiones + '}';
    }
}
