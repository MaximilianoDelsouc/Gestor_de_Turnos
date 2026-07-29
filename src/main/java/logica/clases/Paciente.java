package logica.clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import logica.exceptions.CampoInvalido;

/*
La interfaz Serializable no es necesaria ya que persiste mediante ORM y por lo tanto con SQL.
 */
@Entity
@Table(name = "pacientes")
public class Paciente implements Serializable {

    public static final int LONGITUD_MAXIMA_NOMBRE = 45; // Estos valores se repiten varias veces en el código. Por eso son variables
    public static final int LONGITUD_MAXIMA_APELLIDO = 45;
    public static final int LONGITUD_MAXIMA_DNI = 8;
    public static final int LONGITUD_MAXIMA_TELEFONO = 15;
    public static final int LONGITUD_MAXIMA_CORREO_ELECTRONICO = 90;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Con IDENTITY la BD genera el ID. Es el estándar para MySQL
    @Column(name = "id_paciente")
    private long idPaciente; //long para IDs es lo ideal

    @Column(name = "nombre", nullable = false, length = LONGITUD_MAXIMA_NOMBRE)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = LONGITUD_MAXIMA_APELLIDO)
    private String apellido;

    @Column(name = "dni", nullable = false, length = LONGITUD_MAXIMA_DNI, unique = true)
    private String dni;

    @Column(name = "telefono", nullable = false, length = LONGITUD_MAXIMA_TELEFONO)
    private String telefono;

    @Column(name = "correo_electronico", length = LONGITUD_MAXIMA_CORREO_ELECTRONICO) // Por defecto nullable = true
    private String correoElectronico;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;

    @OneToMany(mappedBy = "paciente")
    private List<Turno> turnos = new ArrayList<>(); // Para evitar NullPointerException mejor tener la lista inicializada y no null. Las colecciones NO deberían ser null.
    // Usar los genéricos '<>' en la especificación de la lista evita el raw type. Ya que si no se usa, recibe Objects y esto deriva a casts internos

    //JPA necesita el constructor vacío
    public Paciente() {

    }

    public Paciente(String nombre, String apellido, String dni, String telefono, String correoElectronico, String observacion) {
        this.nombre = verificarNombre(nombre);
        this.apellido = verificarApellido(apellido);
        this.dni = verificarDni(dni);
        this.telefono = verificarTelefono(telefono);
        this.correoElectronico = verificarCorreoElectronico(correoElectronico);
        this.observacion = verificarObservacion(observacion);
    }

    public long getIdPaciente() {
        return idPaciente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = verificarNombre(nombre);
    }

    private static String verificarNombre(String nombre) { // static porque no usa ningún atributo del objeto, o sea, no necesita la instancia del objeto. Es una función pura de la clase
        if (nombre == null || nombre.isBlank()) {
            throw new CampoInvalido("El nombre del paciente no debe ser vacío.");
        }

        nombre = nombre.strip();
        if (nombre.length() > LONGITUD_MAXIMA_NOMBRE) {
            throw new CampoInvalido("El nombre del paciente no debe superar " + LONGITUD_MAXIMA_NOMBRE + " caracteres de longuitud.");
        }

        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = verificarApellido(apellido);
    }

    private static String verificarApellido(String apellido) {
        if (apellido == null || apellido.isBlank()) {
            throw new CampoInvalido("El apellido del paciente no debe ser vacío.");
        }

        apellido = apellido.strip();
        if (apellido.length() > LONGITUD_MAXIMA_APELLIDO) {
            throw new CampoInvalido("El apellido del paciente no debe superar " + LONGITUD_MAXIMA_APELLIDO + " caracteres de longuitud.");
        }

        return apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = verificarDni(dni);
    }

    private static String verificarDni(String dni) {
        if (dni == null || dni.isBlank()) {
            throw new CampoInvalido("El número de DNI del paciente no debe ser vacío.");
        }

        dni = dni.strip();
        if (dni.length() != LONGITUD_MAXIMA_DNI) { // Esta no es la mejor forma de validar un número de DNI. Se mejorará en el futuro
            throw new CampoInvalido("El número de DNI del paciente debe contener exactamente " + LONGITUD_MAXIMA_DNI + " dígitos.");
        }

        return dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = verificarTelefono(telefono);
    }

    private static String verificarTelefono(String telefono) {
        if (telefono == null || telefono.isBlank()) {
            throw new CampoInvalido("El número de teléfono del paciente no debe ser vacío.");
        }

        telefono = telefono.strip();
        if (telefono.length() > LONGITUD_MAXIMA_TELEFONO) { // Esta no es la mejor forma de validar un número telefónico. Se mejorará en el futuro
            throw new CampoInvalido("El número de teléfono del paciente no debe superar los " + LONGITUD_MAXIMA_TELEFONO + " dígitos.");
        }

        return telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = verificarCorreoElectronico(correoElectronico);
    }

    private static String verificarCorreoElectronico(String correoElectronico) {
        if (correoElectronico == null || correoElectronico.isBlank()) {
            return null;
        }

        correoElectronico = correoElectronico.strip();
        if (correoElectronico.length() > LONGITUD_MAXIMA_CORREO_ELECTRONICO) {
            throw new CampoInvalido("La dirección de correo electrónico del paciente no debe superar " + LONGITUD_MAXIMA_CORREO_ELECTRONICO + " caracteres de longitud.");
        }

        if (!correoElectronico.contains("@") || (!correoElectronico.endsWith(".com") && !correoElectronico.endsWith(".es"))) { // Esta no es la mejor forma de validar un correo electrónico. Se mejorará en el futuro
            throw new CampoInvalido("La dirección de correo electrónico del paciente debe contener un @ y un dominio .com o .es.");
        }

        return correoElectronico;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = verificarObservacion(observacion);
    }

    private static String verificarObservacion(String observacion) {
        if (observacion == null || observacion.isBlank()) {
            return null;
        }

        return observacion.strip();
    }

    public List<Turno> getTurnos() {
        return turnos;
    }

    public void setTurnos(List<Turno> turnos) {
        this.turnos = turnos;
    }

    @Override
    public String toString() {
        return "Paciente{" + "idPaciente=" + idPaciente + ", nombre=" + nombre + ", apellido=" + apellido + ", dni=" + dni + ", telefono=" + telefono
                + ", correoElectronico=" + correoElectronico + ", observacion=" + observacion + '}'; // No se coloca la lista turnos porque puede arrogar StackOverflowError si Turno sobrescribe .toString también
    }
}
