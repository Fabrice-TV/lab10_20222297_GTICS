package org.example.cliente_lab10.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "practicantes")
public class Practicante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombreCompleto;
    
    @Column(nullable = false)
    private String carrera;
    
    @Column(nullable = false)
    private String universidad;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String pais;
    
    @Column(nullable = false)
    private String estado; // Activo o Egresado

    // Constructores
    public Practicante() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getUniversidad() {
        return universidad;
    }

    public void setUniversidad(String universidad) {
        this.universidad = universidad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
