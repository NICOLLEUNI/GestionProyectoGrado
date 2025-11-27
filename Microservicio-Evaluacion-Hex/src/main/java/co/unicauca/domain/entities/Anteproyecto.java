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

        validarTitulo(titulo);
        validarFechaCreacion(fechaCreacion);
        validarArchivoPDF(archivoPDF);
        validarEmail(emailEvaluador1, "Evaluador 1");
        validarEmail(emailEvaluador2, "Evaluador 2");
        validarIdProyecto(idProyectoGrado);

        this.id = id;
        this.titulo = titulo;
        this.fechaCreacion = fechaCreacion;
        this.archivoPDF = archivoPDF;
        this.estado = estado;
        this.idProyectoGrado = idProyectoGrado;
        this.emailEvaluador1 = emailEvaluador1;
        this.emailEvaluador2 = emailEvaluador2;
    }



    private void validarTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título del anteproyecto no puede estar vacío.");
        }
    }

    private void validarFechaCreacion(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha de creación es obligatoria.");
        }
        if (fecha.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de creación no puede estar en el futuro.");
        }
    }

    private void validarArchivoPDF(String archivoPDF) {
        if (archivoPDF == null || archivoPDF.isBlank()) {
            throw new IllegalArgumentException("El archivo PDF es obligatorio.");
        }
        if (!archivoPDF.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("El archivo debe ser un PDF válido.");
        }
    }

    private void validarEmail(String email, String campo) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email de " + campo + " es obligatorio.");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("El email de " + campo + " no es válido.");
        }
    }

    private void validarIdProyecto(Long idProyectoGrado) {
        if (idProyectoGrado == null || idProyectoGrado <= 0) {
            throw new IllegalArgumentException("El ID del proyecto de grado debe ser válido.");
        }
    }

    public void asignar() {
        if (!"ENTREGADO".equalsIgnoreCase(this.estado)) {
            throw new IllegalStateException(
                    "El anteproyecto solo puede pasar a estado ASIGNAR si previamente estaba en ENTREGADO."
            );
        }

        this.estado = "ASIGNAR";
    }
    // ===========================
    //        GETTERS
    // ===========================

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public String getArchivoPDF() { return archivoPDF; }
    public String getEstado() { return estado; }
    public Long getIdProyectoGrado() { return idProyectoGrado; }
    public String getEmailEvaluador1() { return emailEvaluador1; }
    public String getEmailEvaluador2() { return emailEvaluador2; }



}
