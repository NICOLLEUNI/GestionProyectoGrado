package co.unicauca.domain.entities;

import java.time.LocalDate;

public class Anteproyecto {
    private Long id;
    private String titulo;
    private LocalDate fechaCreacion;
    private String archivoPDF;
    private String estado;
    private Long idProyectoGrado;
    private String emailEvaluador1;
    private String emailEvaluador2;


    public Anteproyecto(Long id,
                        String titulo,
                        LocalDate fechaCreacion,
                        String archivoPDF,
                        String estado,
                        Long idProyectoGrado,
                        String emailEvaluador1,
                        String emailEvaluador2) {



        this.id = id;
        this.titulo = titulo;
        this.fechaCreacion = fechaCreacion;
        this.archivoPDF = archivoPDF;
        this.estado = estado;
        this.idProyectoGrado = idProyectoGrado;
        this.emailEvaluador1 = emailEvaluador1;
        this.emailEvaluador2 = emailEvaluador2;
    }

    public Anteproyecto() {
    }

    public void asignarId(Long id) {
        if (id != null && id <= 0) {
            throw new IllegalArgumentException("El ID del anteproyecto debe ser mayor a cero si es proporcionado.");
        }
        this.id = id;
    }

    public void asignarTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título del anteproyecto no puede estar vacío.");
        }
        this.titulo = titulo;
    }

    public void asignarFechaCreacion(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha de creación es obligatoria.");
        }
        if (fecha.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de creación no puede estar en el futuro.");
        }
        this.fechaCreacion = fecha;
    }

    public void asignarArchivoPDF(String archivoPDF) {
        if (archivoPDF == null || archivoPDF.isBlank()) {
            throw new IllegalArgumentException("El archivo PDF es obligatorio.");
        }
        if (!archivoPDF.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("El archivo debe ser un PDF válido.");
        }
        this.archivoPDF = archivoPDF;
    }

    public void asignarEmailEvaluador(String email, String campo) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email de " + campo + " es obligatorio.");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("El email de " + campo + " no es válido.");
        }

        if (campo.equals("Evaluador 1")) {
            this.emailEvaluador1 = email;
        } else if (campo.equals("Evaluador 2")) {
            this.emailEvaluador2 = email;
        }
    }

    public void asignarIdProyecto(Long idProyectoGrado) {
        if (idProyectoGrado == null || idProyectoGrado <= 0) {
            throw new IllegalArgumentException("El ID del proyecto de grado debe ser válido.");
        }
        this.idProyectoGrado = idProyectoGrado;
    }

    public void asignar() {
        if (!"ENTREGADO".equalsIgnoreCase(this.estado)) {
            throw new IllegalStateException(
                    "El anteproyecto solo puede pasar a estado ASIGNAR si previamente estaba en ENTREGADO."
            );
        }

        this.estado = "ASIGNAR";
    }
    public void asignarEstado(String estado) {
        if ("ENTREGADO".equalsIgnoreCase(estado)) {
            this.estado = "ENTREGADO";
        } else {
            throw new IllegalArgumentException("El estado solo puede ser 'ENTREGADO'");
        }
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public String getArchivoPDF() { return archivoPDF; }
    public String getEstado() { return estado; }
    public Long getIdProyectoGrado() { return idProyectoGrado; }
    public String getEmailEvaluador1() { return emailEvaluador1; }
    public String getEmailEvaluador2() { return emailEvaluador2; }



}
