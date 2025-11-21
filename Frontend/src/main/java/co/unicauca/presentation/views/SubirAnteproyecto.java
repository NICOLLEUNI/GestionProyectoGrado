/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package co.unicauca.presentation.views;

import co.unicauca.entity.Anteproyecto;
import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.FormatoA;
import co.unicauca.entity.Persona;
import co.unicauca.infra.DtoFormatoA;
import co.unicauca.service.SubmissionService;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author User
 */
public class SubirAnteproyecto extends javax.swing.JPanel {

    Persona personaLogueado;
    SubmissionService submissionService;
    FormatoA formatoActual;
   private File archivoPDFSeleccionado;
    /**
     * Creates new form SubirFormatoA
     */
    public SubirAnteproyecto(Persona personaLogueado, SubmissionService submissionService) {
        this.personaLogueado = personaLogueado;
        this.submissionService = submissionService;
        initComponents();
        initStyles();
        initEvents();
    }
    private void initEvents() {

    }


    private void initStyles() {

    }


    public void setFormatoA(FormatoA formato) {
        if (formato == null) return;
        this.formatoActual = formato;

        // 🏷️ Datos generales
        lblUTitulo.setText(formato.getTitle() != null ? formato.getTitle() : "Sin título");
        lblUModalidad.setText(formato.getMode() != null ? formato.getMode().toString() : "N/A");

        // 🎓 Director y codirector
        Persona director = submissionService.findPersonaByEmail(formato.getProjectManagerEmail());
        Persona codirector = submissionService.findPersonaByEmail(formato.getProjectCoManagerEmail());

        lblUDirector.setText(
                director != null ? director.getName() + " " + director.getLastname() : "Sin director"
        );

        lblUCodirector.setText(
                codirector != null ? codirector.getName() + " " + codirector.getLastname() : "Sin codirector"
        );

        // 👨‍🎓 Estudiantes
        List<String> estudianteEmails = formato.getEstudianteEmails();

        if (estudianteEmails != null && !estudianteEmails.isEmpty()) {
            // Primer estudiante
            Persona est1 = submissionService.findPersonaByEmail(estudianteEmails.get(0));
            lblUEstudiante1.setText(
                    est1 != null ? est1.getName() + " " + est1.getLastname() : "Sin estudiante"
            );

            // Segundo estudiante (si existe)
            if (estudianteEmails.size() > 1) {
                Persona est2 = submissionService.findPersonaByEmail(estudianteEmails.get(1));
                lblUEstudiante2.setText(
                        est2 != null ? est2.getName() + " " + est2.getLastname() : "Sin segundo estudiante"
                );
            } else {
                lblUEstudiante2.setText("Sin segundo estudiante");
            }
        } else {
            lblUEstudiante1.setText("Sin estudiantes");
            lblUEstudiante2.setText("Sin segundo estudiante");
        }

        // 🎯 Objetivos
        lblUObjGen.setText(
                formato.getGeneralObjetive()!= null ? formato.getGeneralObjetive(): "Sin objetivo general"
        );

        lblUObjEspec.setText(
                formato.getSpecificObjetives() != null ? formato.getSpecificObjetives() : "Sin objetivos específicos"
        );


    }
    private String copiarPDFaProyecto(File archivoOrigen) {
        try {
            // 📌 Detectar ruta raíz del proyecto
            File proyectoRoot = new File("").getAbsoluteFile();

            // 📁 Carpeta destino: /uploads/anteproyecto/
            File carpetaUploads = new File(proyectoRoot, "uploads");
            File carpetaAnteproyecto = new File(carpetaUploads, "anteproyecto");

            // Si no existen, crearlas
            if (!carpetaUploads.exists()) carpetaUploads.mkdir();
            if (!carpetaAnteproyecto.exists()) carpetaAnteproyecto.mkdir();

            // 📄 Destino final
            File destino = new File(carpetaAnteproyecto, archivoOrigen.getName());

            // Copiar archivo
            java.nio.file.Files.copy(
                    archivoOrigen.toPath(),
                    destino.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );

            return destino.getName(); // Solo el nombre del archivo para BD

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error copiando el PDF al proyecto: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Contenido = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        jSeparatorTitulo = new javax.swing.JSeparator();
        lbModalidad = new javax.swing.JLabel();
        lblDirector = new javax.swing.JLabel();
        lblCodirector = new javax.swing.JLabel();
        lblEstudiante1 = new javax.swing.JLabel();
        lblEstudiante2 = new javax.swing.JLabel();
        lbObjGen = new javax.swing.JLabel();
        lblObjetivosEspecificos = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btCrear = new javax.swing.JButton();
        jSeparatorTitulo1 = new javax.swing.JSeparator();
        jSeparatorTitulo2 = new javax.swing.JSeparator();
        jSeparatorTitulo3 = new javax.swing.JSeparator();
        jSeparatorTitulo4 = new javax.swing.JSeparator();
        jSeparatorTitulo5 = new javax.swing.JSeparator();
        jSeparatorTitulo6 = new javax.swing.JSeparator();
        jSeparatorTitulo7 = new javax.swing.JSeparator();
        lblUTitulo = new javax.swing.JLabel();
        lblUModalidad = new javax.swing.JLabel();
        lblUDirector = new javax.swing.JLabel();
        lblUCodirector = new javax.swing.JLabel();
        lblUEstudiante1 = new javax.swing.JLabel();
        lblUEstudiante2 = new javax.swing.JLabel();
        lblUObjGen = new javax.swing.JLabel();
        lblUObjEspec = new javax.swing.JLabel();
        lblRuta = new javax.swing.JLabel();
        btPDF = new javax.swing.JButton();
        txtRutaPDF = new javax.swing.JLabel();

        Contenido.setBackground(new java.awt.Color(255, 255, 255));
        Contenido.setPreferredSize(new java.awt.Dimension(646, 530));
        Contenido.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(0, 0, 0));
        lblTitulo.setText("Titulo");
        lblTitulo.setToolTipText("");
        Contenido.add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 50, -1));
        Contenido.add(jSeparatorTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(291, 223, 190, 50));

        lbModalidad.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lbModalidad.setForeground(new java.awt.Color(0, 0, 0));
        lbModalidad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbModalidad.setText("Modalidad");
        Contenido.add(lbModalidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));

        lblDirector.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblDirector.setForeground(new java.awt.Color(0, 0, 0));
        lblDirector.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDirector.setText("Director de proyecto");
        Contenido.add(lblDirector, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, -1, -1));

        lblCodirector.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblCodirector.setForeground(new java.awt.Color(0, 0, 0));
        lblCodirector.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCodirector.setText("Codirector de proyecto");
        Contenido.add(lblCodirector, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, -1, 20));

        lblEstudiante1.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblEstudiante1.setForeground(new java.awt.Color(0, 0, 0));
        lblEstudiante1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstudiante1.setText("Estudiante #1");
        Contenido.add(lblEstudiante1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, -1, -1));

        lblEstudiante2.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblEstudiante2.setForeground(new java.awt.Color(0, 0, 0));
        lblEstudiante2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstudiante2.setText("Estudiante #2");
        Contenido.add(lblEstudiante2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 370, -1, -1));

        lbObjGen.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lbObjGen.setForeground(new java.awt.Color(0, 0, 0));
        lbObjGen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbObjGen.setText("Objetivo General");
        Contenido.add(lbObjGen, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, 125, -1));

        lblObjetivosEspecificos.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblObjetivosEspecificos.setForeground(new java.awt.Color(0, 0, 0));
        lblObjetivosEspecificos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblObjetivosEspecificos.setText("Objetivos especificos");
        Contenido.add(lblObjetivosEspecificos, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 100, -1, -1));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        Contenido.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 6, 18, 420));

        btCrear.setBackground(new java.awt.Color(51, 51, 255));
        btCrear.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        btCrear.setForeground(new java.awt.Color(255, 255, 255));
        btCrear.setText("CREAR");
        btCrear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btCrearMouseClicked(evt);
            }
        });
        Contenido.add(btCrear, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 390, 112, 37));
        Contenido.add(jSeparatorTitulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(44, 65, 192, 18));
        Contenido.add(jSeparatorTitulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 192, 18));
        Contenido.add(jSeparatorTitulo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, 192, 18));
        Contenido.add(jSeparatorTitulo4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 278, 192, 30));
        Contenido.add(jSeparatorTitulo5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 340, 192, 18));
        Contenido.add(jSeparatorTitulo6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 420, 192, 18));
        Contenido.add(jSeparatorTitulo7, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 80, 200, 18));

        lblUTitulo.setForeground(new java.awt.Color(51, 51, 51));
        lblUTitulo.setText("Titulo");
        Contenido.add(lblUTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 200, -1));

        lblUModalidad.setForeground(new java.awt.Color(51, 51, 51));
        lblUModalidad.setText("Titulo");
        Contenido.add(lblUModalidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 190, -1));

        lblUDirector.setForeground(new java.awt.Color(51, 51, 51));
        lblUDirector.setText("Titulo");
        Contenido.add(lblUDirector, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 190, 190, -1));

        lblUCodirector.setForeground(new java.awt.Color(51, 51, 51));
        lblUCodirector.setText("Titulo");
        Contenido.add(lblUCodirector, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 260, 190, -1));

        lblUEstudiante1.setForeground(new java.awt.Color(51, 51, 51));
        lblUEstudiante1.setText("Titulo");
        Contenido.add(lblUEstudiante1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 320, 190, -1));

        lblUEstudiante2.setForeground(new java.awt.Color(51, 51, 51));
        lblUEstudiante2.setText("Titulo");
        Contenido.add(lblUEstudiante2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 400, 190, -1));

        lblUObjGen.setForeground(new java.awt.Color(51, 51, 51));
        lblUObjGen.setText("Titulo");
        lblUObjGen.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        Contenido.add(lblUObjGen, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 30, 310, 70));

        lblUObjEspec.setForeground(new java.awt.Color(51, 51, 51));
        lblUObjEspec.setText("Titulo");
        lblUObjEspec.setToolTipText("");
        lblUObjEspec.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        Contenido.add(lblUObjEspec, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 120, 210, 91));

        lblRuta.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblRuta.setForeground(new java.awt.Color(0, 0, 0));
        lblRuta.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRuta.setText("Ruta");
        Contenido.add(lblRuta, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 240, -1, -1));

        btPDF.setBackground(new java.awt.Color(102, 102, 255));
        btPDF.setText("Adjunte un PDF");
        btPDF.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btPDF.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btPDFMousePressed(evt);
            }
        });
        Contenido.add(btPDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 270, -1, -1));

        txtRutaPDF.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        txtRutaPDF.setForeground(new java.awt.Color(51, 51, 255));
        txtRutaPDF.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtRutaPDF.setText("Ruta");
        txtRutaPDF.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Contenido.add(txtRutaPDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 300, 250, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Contenido, javax.swing.GroupLayout.PREFERRED_SIZE, 537, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Contenido, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btCrearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btCrearMouseClicked
        if (formatoActual == null) {
            JOptionPane.showMessageDialog(this, "No hay Formato A seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear el anteproyecto con los datos del Formato A
        Anteproyecto ante = new Anteproyecto();
        ante.setTitulo(formatoActual.getTitle());
        ante.setFecha(formatoActual.getDate());
        if (archivoPDFSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un archivo PDF antes de crear el anteproyecto.");
            return;
        }
        ante.setRutaPdf(archivoPDFSeleccionado.getName());
        String nombrePDF = copiarPDFaProyecto(archivoPDFSeleccionado);
        ante.setEstado("ENTREGADO");  // O el estado inicial que corresponda
        ante.setIdProyectoGrado(formatoActual.getId()); // Relación
        System.out.println("PDF enviado: " + ante.getRutaPdf());
        // Enviar al backend
        Anteproyecto creado = submissionService.subirAnteproyecto(ante);


        if (creado != null) {
            JOptionPane.showMessageDialog(this, "✅ Anteproyecto creado con ID: " + creado.getId());

            // 🔄 Buscar y eliminar la fila correspondiente en la tabla
            java.awt.Container parent = this.getParent();
            while (parent != null && !(parent instanceof co.unicauca.presentation.GUISubirAnteproyecto)) {
                parent = parent.getParent();
            }

            if (parent instanceof co.unicauca.presentation.GUISubirAnteproyecto gui) {
                gui.eliminarFilaTabla(formatoActual.getId());
            }

            // Cerrar este panel
            this.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al crear el Anteproyecto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btCrearMouseClicked

    private void btPDFMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btPDFMousePressed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo PDF");
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos PDF", "pdf");
        fileChooser.setFileFilter(filtro);

        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            archivoPDFSeleccionado = fileChooser.getSelectedFile();
            txtRutaPDF.setText(archivoPDFSeleccionado.getName());
        }
    }//GEN-LAST:event_btPDFMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Contenido;
    private javax.swing.JButton btCrear;
    private javax.swing.JButton btPDF;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparatorTitulo;
    private javax.swing.JSeparator jSeparatorTitulo1;
    private javax.swing.JSeparator jSeparatorTitulo2;
    private javax.swing.JSeparator jSeparatorTitulo3;
    private javax.swing.JSeparator jSeparatorTitulo4;
    private javax.swing.JSeparator jSeparatorTitulo5;
    private javax.swing.JSeparator jSeparatorTitulo6;
    private javax.swing.JSeparator jSeparatorTitulo7;
    private javax.swing.JLabel lbModalidad;
    private javax.swing.JLabel lbObjGen;
    private javax.swing.JLabel lblCodirector;
    private javax.swing.JLabel lblDirector;
    private javax.swing.JLabel lblEstudiante1;
    private javax.swing.JLabel lblEstudiante2;
    private javax.swing.JLabel lblObjetivosEspecificos;
    private javax.swing.JLabel lblRuta;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblUCodirector;
    private javax.swing.JLabel lblUDirector;
    private javax.swing.JLabel lblUEstudiante1;
    private javax.swing.JLabel lblUEstudiante2;
    private javax.swing.JLabel lblUModalidad;
    private javax.swing.JLabel lblUObjEspec;
    private javax.swing.JLabel lblUObjGen;
    private javax.swing.JLabel lblUTitulo;
    private javax.swing.JLabel txtRutaPDF;
    // End of variables declaration//GEN-END:variables
}
