/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package co.unicauca.presentation.views;

import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.EnumModalidad;
import co.unicauca.entity.FormatoA;
import co.unicauca.entity.Persona;
import co.unicauca.service.SubmissionService;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


/**
 *
 * @author Usuario
 */
public class DetallesFormatoA extends javax.swing.JPanel {

    private final FormatoA formato;
    private final Persona personaLogueada;
    private final SubmissionService submissionService;

    public DetallesFormatoA(FormatoA formato, Persona personaLogueada) {
        initComponents();
        this.formato = formato;
        this.personaLogueada = personaLogueada;
        this.submissionService = new SubmissionService();
        cargarDatos();
        // No necesitas initListeners si usas los listeners generados por NetBeans
    }


    /**
     * Rellena los componentes con los datos del formato recibido.
     */
    private void cargarDatos() {
        if (formato == null) return;

        // Mostrar datos principales
        lbMostrarTitulo.setText(formato.getTitle() != null ? formato.getTitle() : "");
        lbMostrarModalidad.setText(formato.getMode() != null ? formato.getMode().toString() : "No definida");
        if (formato.getProjectManagerEmail() != null && !formato.getProjectManagerEmail().isEmpty()) {
            lbMostrarDirector.setText(formato.getProjectManagerEmail());
        } else {
            lbMostrarDirector.setText("Sin director asignado");
        }


        txObjGeneral.setText(formato.getGeneralObjetive() != null ? formato.getGeneralObjetive() : "");
        jTextArea1.setText(formato.getSpecificObjetives() != null ? formato.getSpecificObjetives() : "");

        txtMostrarRutaPDF.setText(formato.getArchivoPDF() != null ? formato.getArchivoPDF() : "Sin archivo");
        txtMostarRutaCarta.setText(formato.getCartaLaboral() != null ? formato.getCartaLaboral() : "Sin carta");

        lbMostrarComentarios.setText(formato.getObservations() != null ? formato.getObservations() : "");

        // Debug (puedes quitarlo luego)
        System.out.println("=== DEBUG DetallesFormatoA ===");
        System.out.println("DEBUG - Título: " + formato.getTitle());
        System.out.println("DEBUG - Estado: " + formato.getState());
        System.out.println("DEBUG - Observaciones: " + formato.getObservations());
        System.out.println("DEBUG - Obj General: " + formato.getGeneralObjetive());
        System.out.println("DEBUG - Counter: " + formato.getCounter());

        // Asegurar estado inicial de controles según estado actual
        configurarSegunEstado();
    }

    /**
     * Habilita/deshabilita controles según el estado y contador del formato.
     */
    private void configurarSegunEstado() {
        // Bloqueo por defecto
        txObjGeneral.setEditable(false);
        jTextArea1.setEditable(false);
        btPDF.setEnabled(false);
        btCarta.setEnabled(false);
        btActualizar.setEnabled(false);

        if (formato == null) return;

        EnumEstado estado = formato.getState();
        int counter = formato.getCounter();

        System.out.println("DEBUG - Estado: " + estado + ", Counter: " + counter + ", Modalidad: " + formato.getMode());

        // Permitir editar solo si está RECHAZADO y tiene menos de 3 rechazos
        if (estado == EnumEstado.RECHAZADO && counter < 3) {
            txObjGeneral.setEditable(true);
            jTextArea1.setEditable(true);
            btPDF.setEnabled(true);
            btActualizar.setEnabled(true);

            // la carta sólo si modalidad PRACTICA_PROFESIONAL
            if (formato.getMode() != null && formato.getMode() == EnumModalidad.PRACTICA_PROFESIONAL) {
                btCarta.setEnabled(true);
                System.out.println("DEBUG - Carta laboral HABILITADA");
            } else {
                btCarta.setEnabled(false);
                System.out.println("DEBUG - Carta laboral DESHABILITADA");
            }

            int rechazosRestantes = 3 - counter;
            System.out.println("DEBUG - Rechazos restantes: " + rechazosRestantes);
        } else {
            // Otros estados -> solo lectura
            txObjGeneral.setEditable(false);
            jTextArea1.setEditable(false);
            btPDF.setEnabled(false);
            btCarta.setEnabled(false);
            btActualizar.setEnabled(false);
            System.out.println("DEBUG - Todos los controles DESHABILITADOS");

            if (estado == EnumEstado.RECHAZADO && counter >= 3) {
                JOptionPane.showMessageDialog(this, "Este formato ha sido rechazado definitivamente después de 3 rechazos.");
            }
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
        lbTitulo = new javax.swing.JLabel();
        lbMostrarTitulo = new javax.swing.JLabel();
        lbModalida = new javax.swing.JLabel();
        lbMostrarModalidad = new javax.swing.JLabel();
        lbDirector = new javax.swing.JLabel();
        lbMostrarDirector = new javax.swing.JLabel();
        lbObjGeneral = new javax.swing.JLabel();
        txObjGeneral = new javax.swing.JTextField();
        lbObjEspecificos = new javax.swing.JLabel();
        txaObjEspecificos = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jSeparator1 = new javax.swing.JSeparator();
        lbPDF = new javax.swing.JLabel();
        txtMostrarRutaPDF = new javax.swing.JLabel();
        btPDF = new javax.swing.JButton();
        lbCarta = new javax.swing.JLabel();
        btCarta = new javax.swing.JButton();
        txtMostarRutaCarta = new javax.swing.JLabel();
        lbComentarios = new javax.swing.JLabel();
        btActualizar = new javax.swing.JButton();
        lbMostrarComentarios = new javax.swing.JLabel();
        btnVolver = new javax.swing.JButton();

        Contenido.setBackground(new java.awt.Color(255, 255, 255));
        Contenido.setPreferredSize(new java.awt.Dimension(646, 530));

        lbTitulo.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        lbTitulo.setForeground(new java.awt.Color(0, 0, 0));
        lbTitulo.setText("Titulo del proyecto");

        lbMostrarTitulo.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        lbMostrarTitulo.setForeground(new java.awt.Color(0, 0, 0));
        lbMostrarTitulo.setText("label");
        lbMostrarTitulo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lbModalida.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        lbModalida.setForeground(new java.awt.Color(0, 0, 0));
        lbModalida.setText("Modalidad");

        lbMostrarModalidad.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        lbMostrarModalidad.setForeground(new java.awt.Color(0, 0, 0));
        lbMostrarModalidad.setText("modalidad");
        lbMostrarModalidad.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lbDirector.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        lbDirector.setForeground(new java.awt.Color(0, 0, 0));
        lbDirector.setText("Director de proyecto");

        lbMostrarDirector.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        lbMostrarDirector.setForeground(new java.awt.Color(0, 0, 0));
        lbMostrarDirector.setText("director");
        lbMostrarDirector.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lbObjGeneral.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        lbObjGeneral.setForeground(new java.awt.Color(0, 0, 0));
        lbObjGeneral.setText("Objetivo general");

        txObjGeneral.setBackground(new java.awt.Color(255, 255, 255));

        lbObjEspecificos.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        lbObjEspecificos.setForeground(new java.awt.Color(0, 0, 0));
        lbObjEspecificos.setText("Objetivos especificos");

        txaObjEspecificos.setBackground(new java.awt.Color(255, 255, 255));
        txaObjEspecificos.setForeground(new java.awt.Color(255, 255, 255));

        jTextArea1.setBackground(new java.awt.Color(255, 255, 255));
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        txaObjEspecificos.setViewportView(jTextArea1);

        lbPDF.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        lbPDF.setForeground(new java.awt.Color(0, 0, 0));
        lbPDF.setText("PDF - Formato A");

        txtMostrarRutaPDF.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        txtMostrarRutaPDF.setForeground(new java.awt.Color(51, 51, 255));
        txtMostrarRutaPDF.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtMostrarRutaPDF.setText("Ruta");
        txtMostrarRutaPDF.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btPDF.setBackground(new java.awt.Color(102, 102, 255));
        btPDF.setText("Adjunte un PDF");
        btPDF.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btPDF.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btPDFMousePressed(evt);
            }
        });

        lbCarta.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        lbCarta.setForeground(new java.awt.Color(0, 0, 0));
        lbCarta.setText("Carta de aprobacion laboral");
        lbCarta.setMaximumSize(new java.awt.Dimension(112, 19));
        lbCarta.setMinimumSize(new java.awt.Dimension(112, 19));

        btCarta.setBackground(new java.awt.Color(102, 102, 255));
        btCarta.setText("Adjunte un PDF");
        btCarta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btCarta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btCartaMouseClicked(evt);
            }
        });

        txtMostarRutaCarta.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        txtMostarRutaCarta.setForeground(new java.awt.Color(51, 51, 255));
        txtMostarRutaCarta.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtMostarRutaCarta.setText("Ruta");
        txtMostarRutaCarta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lbComentarios.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        lbComentarios.setForeground(new java.awt.Color(0, 0, 0));
        lbComentarios.setText("Comentarios devueltos");

        btActualizar.setBackground(new java.awt.Color(102, 102, 255));
        btActualizar.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btActualizar.setText("Actualizar");
        btActualizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btActualizarMouseClicked(evt);
            }
        });

        lbMostrarComentarios.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        lbMostrarComentarios.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnVolver.setBackground(new java.awt.Color(51, 51, 255));
        btnVolver.setForeground(new java.awt.Color(51, 51, 255));
        btnVolver.setText("Regresar");
        btnVolver.setBorderPainted(false);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVolver.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnVolverMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout ContenidoLayout = new javax.swing.GroupLayout(Contenido);
        Contenido.setLayout(ContenidoLayout);
        ContenidoLayout.setHorizontalGroup(
            ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ContenidoLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lbTitulo)
                .addGap(69, 69, 69)
                .addComponent(lbModalida)
                .addGap(129, 129, 129)
                .addComponent(lbDirector))
            .addGroup(ContenidoLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lbMostrarTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(lbMostrarModalidad, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(lbMostrarDirector, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(ContenidoLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 618, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(ContenidoLayout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addComponent(lbPDF)
                .addGap(116, 116, 116)
                .addComponent(lbCarta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(ContenidoLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(btPDF, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(btCarta, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(ContenidoLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(txtMostrarRutaPDF, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(txtMostarRutaCarta, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(ContenidoLayout.createSequentialGroup()
                .addGap(250, 250, 250)
                .addComponent(lbObjGeneral))
            .addGroup(ContenidoLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(txObjGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(ContenidoLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(lbObjEspecificos, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(150, 150, 150)
                .addComponent(lbComentarios))
            .addGroup(ContenidoLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(txaObjEspecificos, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(lbMostrarComentarios, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(ContenidoLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(btnVolver)
                .addGap(334, 334, 334)
                .addComponent(btActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        ContenidoLayout.setVerticalGroup(
            ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ContenidoLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbTitulo)
                    .addComponent(lbModalida)
                    .addComponent(lbDirector))
                .addGap(1, 1, 1)
                .addGroup(ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbMostrarTitulo)
                    .addComponent(lbMostrarModalidad)
                    .addComponent(lbMostrarDirector))
                .addGap(19, 19, 19)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbPDF)
                    .addComponent(lbCarta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btPDF)
                    .addComponent(btCarta))
                .addGap(7, 7, 7)
                .addGroup(ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMostrarRutaPDF, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMostarRutaCarta, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(lbObjGeneral)
                .addGap(1, 1, 1)
                .addComponent(txObjGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbObjEspecificos)
                    .addComponent(lbComentarios))
                .addGap(1, 1, 1)
                .addGroup(ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txaObjEspecificos, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbMostrarComentarios, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnVolver)
                    .addComponent(btActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Contenido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Contenido, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btPDFMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btPDFMousePressed
        try {
            if (!esEditable()) {
                JOptionPane.showMessageDialog(this, "No puede modificar el formato en el estado actual.");
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccionar formato A (PDF)");
            FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos PDF", "pdf");
            fileChooser.setFileFilter(filtro);

            int resultado = fileChooser.showOpenDialog(this);

            if (resultado == JFileChooser.APPROVE_OPTION) {
                File archivoSeleccionado = fileChooser.getSelectedFile();

                // Subir directamente al microservicio -> ahora devuelve String (ruta o JSON) o null si falló
                String respuesta = submissionService.subirFormatoAPDF(formato.getId(), archivoSeleccionado);
                if (respuesta != null && !respuesta.isEmpty()) {
                    // Si el backend devuelve la ruta, usarla; si devuelve JSON, podrías parsearla
                    formato.setArchivoPDF(respuesta);
                    txtMostrarRutaPDF.setText(respuesta);
                    JOptionPane.showMessageDialog(this, "PDF subido correctamente: " + respuesta);
                } else {
                    // fallback: quizá el backend no devolvió ruta, mostrar nombre local
                    JOptionPane.showMessageDialog(this, "Error subiendo PDF al servidor.");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_btPDFMousePressed

    private void btCartaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btCartaMouseClicked
        try {
            if (!esEditable()) {
                JOptionPane.showMessageDialog(this, "No puede modificar el formato en el estado actual.");
                return;

            }

            if (formato.getMode() != EnumModalidad.PRACTICA_PROFESIONAL) {
                JOptionPane.showMessageDialog(this, "La carta laboral solo es requerida para práctica profesional.");
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccionar carta laboral (PDF)");
            FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos PDF", "pdf");
            fileChooser.setFileFilter(filtro);

            int resultado = fileChooser.showOpenDialog(this);

            if (resultado == JFileChooser.APPROVE_OPTION) {
                File archivoSeleccionado = fileChooser.getSelectedFile();

                // Subir al servidor -> devuelve String (ruta o JSON) o null si falla
                String respuesta = submissionService.subirCartaLaboral(formato.getId(), archivoSeleccionado);
                if (respuesta != null && !respuesta.isEmpty()) {
                    formato.setCartaLaboral(respuesta);
                    txtMostarRutaCarta.setText(respuesta);
                    JOptionPane.showMessageDialog(this, "Carta laboral subida correctamente: " + respuesta);
                } else {
                    JOptionPane.showMessageDialog(this, "Error subiendo la carta laboral al servidor.");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_btCartaMouseClicked

    private void btActualizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btActualizarMouseClicked
        try {

            if (!esEditable()) {
                JOptionPane.showMessageDialog(this, "No puede actualizar el formato en el estado actual.");
                return;
            }

            if (formato.getCounter() >= 3) {
                JOptionPane.showMessageDialog(this, "Ha alcanzado el límite máximo de 3 rechazos. No puede enviar más versiones.");
                configurarSegunEstado();
                return;
            }

            if (formato == null || formato.getId() == null || formato.getId() <= 0) {
                JOptionPane.showMessageDialog(this, "Formato no válido para actualizar.");
                return;
            }

            // Validar campos obligatorios
            if (txObjGeneral.getText().trim().isEmpty() || jTextArea1.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe completar todos los campos obligatorios.");
                return;
            }

            if (formato.getArchivoPDF() == null || formato.getArchivoPDF().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe adjuntar el PDF del formato A.");
                return;
            }

            if (formato.getMode() == EnumModalidad.PRACTICA_PROFESIONAL &&
                    (formato.getCartaLaboral() == null || formato.getCartaLaboral().trim().isEmpty())) {

                JOptionPane.showMessageDialog(this, "Para práctica profesional debe adjuntar la carta laboral.");
                return;
            }

            // Actualizar objeto local antes de reenviar
            formato.setGeneralObjetive(txObjGeneral.getText());
            formato.setSpecificObjetives(jTextArea1.getText());

            // Enviar al backend para reenvío
            FormatoA actualizado = submissionService.reenviarFormatoA(formato);
            if (actualizado != null) {
                formato.setArchivoPDF(actualizado.getArchivoPDF());
                formato.setCartaLaboral(actualizado.getCartaLaboral());
                formato.setGeneralObjetive(actualizado.getGeneralObjetive());
                formato.setSpecificObjetives(actualizado.getSpecificObjetives());
                formato.setCounter(actualizado.getCounter());
                formato.setState(actualizado.getState());


                cargarDatos();
                JOptionPane.showMessageDialog(this, "Formato reenviado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se recibió respuesta del servidor. Intente nuevamente.");
            }


        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage());
        }
    }//GEN-LAST:event_btActualizarMouseClicked
    
    private void showJPanel(JPanel pl){
        pl.setSize(641,498);
        pl.setLocation(0, 0);

        Contenido.removeAll();
        Contenido.add(pl, BorderLayout.CENTER);
        Contenido.revalidate();
        Contenido.repaint();
    }
    
    private void btnVolverMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVolverMouseClicked
        try {
            showJPanel(new ListaFormatosA(personaLogueada));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error volviendo a la lista: " + e.getMessage());
        }
    }//GEN-LAST:event_btnVolverMouseClicked
    
    // MÉTODO AUXILIAR PARA VALIDAR SI ES EDITABLE
    private boolean esEditable() {
        if (formato == null) return false;

        EnumEstado estado = formato.getState();
        int counter = formato.getCounter();

        return (estado == EnumEstado.RECHAZADO && counter < 3);

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Contenido;
    private javax.swing.JButton btActualizar;
    private javax.swing.JButton btCarta;
    private javax.swing.JButton btPDF;
    private javax.swing.JButton btnVolver;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lbCarta;
    private javax.swing.JLabel lbComentarios;
    private javax.swing.JLabel lbDirector;
    private javax.swing.JLabel lbModalida;
    private javax.swing.JLabel lbMostrarComentarios;
    private javax.swing.JLabel lbMostrarDirector;
    private javax.swing.JLabel lbMostrarModalidad;
    private javax.swing.JLabel lbMostrarTitulo;
    private javax.swing.JLabel lbObjEspecificos;
    private javax.swing.JLabel lbObjGeneral;
    private javax.swing.JLabel lbPDF;
    private javax.swing.JLabel lbTitulo;
    private javax.swing.JTextField txObjGeneral;
    private javax.swing.JScrollPane txaObjEspecificos;
    private javax.swing.JLabel txtMostarRutaCarta;
    private javax.swing.JLabel txtMostrarRutaPDF;
    // End of variables declaration//GEN-END:variables
}
