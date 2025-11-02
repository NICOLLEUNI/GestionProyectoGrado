/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package co.unicauca.workflow.presentation.views;

//al dar click en ruta pdf debe exportar el formato A en la ruta 

import co.unicauca.workflow.access.Factory;
import co.unicauca.workflow.domain.entities.FormatoA;
import co.unicauca.workflow.domain.entities.enumEstado;
import co.unicauca.workflow.domain.service.FormatoAService;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMTMaterialLighterIJTheme;
import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;

import co.unicauca.workflow.access.FormatoAVersionRepository;
import co.unicauca.workflow.access.IFormatoAVersionRepository;
import co.unicauca.workflow.domain.entities.FormatoAVersion;
import co.unicauca.workflow.domain.entities.enumEstado;

/**
 * 
 *
 * @author User
 */
public class Observaciones extends javax.swing.JPanel {
    
    IFormatoAVersionRepository versionRepo = Factory.getInstance().getFormatoAVersionRepository("default");

    private FormatoA formatoActual;
    private final FormatoAService formatoAService;

    // Constructor recibe la misma instancia del service
    public Observaciones(FormatoAService formatoAService) {
        this.formatoAService = formatoAService;
        FlatMTMaterialLighterIJTheme.setup();
        initComponents();
        initStyles();
        initEvents();
    }
    
   private void initEvents() {
    lblPDF.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            abrirPDF();
        }
    });
}
   private void initStyles() {
    // estilos del lblPDF
    lblPDF.setForeground(new java.awt.Color(33, 150, 243)); 
    lblPDF.setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 13));
    lblPDF.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); 
    lblPDF.setToolTipText("Abrir PDF"); 
        }
   
   private void abrirPDF() {
    try {
        String ruta = lblPDF.getText();  
        File file = new File(ruta);

        if (file.exists()) {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);  // abre con el programa por defecto
            } else {
                JOptionPane.showMessageDialog(this, "La función Desktop no está soportada en este sistema.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "El archivo no existe: " + ruta);
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al abrir el archivo: " + e.getMessage());
    }
}

   public void setFormatoA(FormatoA formato) {
       
    if (formato == null) return;
    this.formatoActual = formato;

    // Asignar datos a los labels
    lblUTitulo.setText(formato.getTitle() != null ? formato.getTitle() : "Sin título");
    lblUModalidad.setText(formato.getMode() != null ? formato.getMode().toString() : "N/A");
    lblUDirector.setText(formato.getProjectManager() != null
            ? formato.getProjectManager().getName() + " " + formato.getProjectManager().getLastname()
            : "Sin director");

    // Estudiantes
    if (formato.getEstudiantes() != null && !formato.getEstudiantes().isEmpty()) {
        lblUEstudiante.setText(formato.getEstudiantes().get(0).getName() + " " +
                               formato.getEstudiantes().get(0).getLastname());

        if (formato.getEstudiantes().size() > 1) {
            lblUEstudiante2.setText(formato.getEstudiantes().get(1).getName() + " " +
                                    formato.getEstudiantes().get(1).getLastname());
        } else {
            lblUEstudiante2.setText("Sin segundo estudiante");
        }
    } else {
        lblUEstudiante.setText("Sin estudiantes");
        lblUEstudiante2.setText("Sin segundo estudiante");
    }

    // PDF
    String rutaPDF = formato.getArchivoPDF();
    if (rutaPDF != null && !rutaPDF.trim().isEmpty()) {
        if (!rutaPDF.toLowerCase().endsWith(".pdf")) {
            rutaPDF += ".pdf";
        }
        lblPDF.setText(rutaPDF);
    } else {
        lblPDF.setText("Sin archivo");
    }

    // Observaciones previas (si las tiene en BD)
    if (formato.getObservations() != null) {
        txtObservaciones.setText(formato.getObservations());
    }
//para cambiar
    // Estado previo (si ya fue evaluado)
    if (formato.getState() != null) {
        if (formato.getState() == enumEstado.APROBADO) {
            CBXAprobado.setSelected(true);
            CBXRechazado.setSelected(false);
        } else if (formato.getState() == enumEstado.RECHAZADO) {
            CBXRechazado.setSelected(true);
            CBXAprobado.setSelected(false);
        } else {
            CBXAprobado.setSelected(false);
            CBXRechazado.setSelected(false);
        }
    } else {
        CBXAprobado.setSelected(false);
        CBXRechazado.setSelected(false);
      }     }  

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Contenido = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        lblUTitulo = new javax.swing.JLabel();
        lblEstudiante = new javax.swing.JLabel();
        lblUEstudiante = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        lblDirector = new javax.swing.JLabel();
        lblUDirector = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();
        lblModalidad = new javax.swing.JLabel();
        lblUModalidad = new javax.swing.JLabel();
        jSeparator12 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        lblPDF = new javax.swing.JLabel();
        lblObservaciones = new javax.swing.JLabel();
        CBXAprobado = new javax.swing.JCheckBox();
        CBXRechazado = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtObservaciones = new javax.swing.JTextArea();
        lblEstudiante2 = new javax.swing.JLabel();
        lblUEstudiante2 = new javax.swing.JLabel();
        jSeparator13 = new javax.swing.JSeparator();
        Icon = new javax.swing.JLabel();
        btEvaluar = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(533, 456));
        setPreferredSize(new java.awt.Dimension(533, 456));

        Contenido.setBackground(new java.awt.Color(255, 255, 255));
        Contenido.setPreferredSize(new java.awt.Dimension(533, 456));
        Contenido.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(0, 0, 0));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTitulo.setText("Titulo");
        Contenido.add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, 61, -1));
        Contenido.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(122, 32, 230, -1));

        lblUTitulo.setForeground(new java.awt.Color(51, 51, 51));
        lblUTitulo.setText("Titulo");
        Contenido.add(lblUTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(119, 12, 233, -1));

        lblEstudiante.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblEstudiante.setForeground(new java.awt.Color(0, 0, 0));
        lblEstudiante.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEstudiante.setText("Estudiante");
        Contenido.add(lblEstudiante, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 41, 92, -1));

        lblUEstudiante.setForeground(new java.awt.Color(51, 51, 51));
        lblUEstudiante.setText("Estudiante");
        Contenido.add(lblUEstudiante, new org.netbeans.lib.awtextra.AbsoluteConstraints(119, 41, 233, -1));
        Contenido.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(122, 61, 230, -1));

        lblDirector.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblDirector.setForeground(new java.awt.Color(0, 0, 0));
        lblDirector.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblDirector.setText("Dir.Proyecto");
        Contenido.add(lblDirector, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        lblUDirector.setForeground(new java.awt.Color(51, 51, 51));
        lblUDirector.setText("Director");
        Contenido.add(lblUDirector, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 233, -1));
        Contenido.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 230, -1));

        lblModalidad.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblModalidad.setForeground(new java.awt.Color(0, 0, 0));
        lblModalidad.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblModalidad.setText("Modalidad");
        Contenido.add(lblModalidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));

        lblUModalidad.setForeground(new java.awt.Color(51, 51, 51));
        lblUModalidad.setText("Modalidad");
        Contenido.add(lblUModalidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 140, 233, -1));
        Contenido.add(jSeparator12, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 160, 230, -1));

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        lblPDF.setFont(new java.awt.Font("Roboto Light", 0, 14)); // NOI18N
        lblPDF.setForeground(new java.awt.Color(102, 102, 255));
        lblPDF.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPDF.setText("RUTA PDF");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblPDF, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblPDF, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
        );

        Contenido.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, -1, -1));

        lblObservaciones.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblObservaciones.setForeground(new java.awt.Color(0, 0, 0));
        lblObservaciones.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblObservaciones.setText("Observaciones");
        Contenido.add(lblObservaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 230, 125, -1));

        CBXAprobado.setText("Aprobado");
        CBXAprobado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CBXAprobadoMouseClicked(evt);
            }
        });
        Contenido.add(CBXAprobado, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 330, -1, -1));

        CBXRechazado.setText("Rechazado");
        CBXRechazado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CBXRechazadoMouseClicked(evt);
            }
        });
        Contenido.add(CBXRechazado, new org.netbeans.lib.awtextra.AbsoluteConstraints(321, 330, -1, -1));

        txtObservaciones.setBackground(new java.awt.Color(255, 255, 255));
        txtObservaciones.setColumns(20);
        txtObservaciones.setRows(5);
        txtObservaciones.setText("   Ingrese sus Observaciones");
        txtObservaciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtObservacionesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(txtObservaciones);

        Contenido.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 230, 294, -1));

        lblEstudiante2.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblEstudiante2.setForeground(new java.awt.Color(0, 0, 0));
        lblEstudiante2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEstudiante2.setText("Estudiante");
        Contenido.add(lblEstudiante2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 92, -1));

        lblUEstudiante2.setForeground(new java.awt.Color(51, 51, 51));
        lblUEstudiante2.setText("Estudiante");
        Contenido.add(lblUEstudiante2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, 233, -1));
        Contenido.add(jSeparator13, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 90, 230, -1));

        Icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/co/unicauca/workflow/presentation/images/LogoPequeño.png"))); // NOI18N
        Contenido.add(Icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, -1, -1));

        btEvaluar.setBackground(new java.awt.Color(65, 55, 171));
        btEvaluar.setFont(new java.awt.Font("Roboto Medium", 1, 24)); // NOI18N
        btEvaluar.setForeground(new java.awt.Color(255, 255, 255));
        btEvaluar.setText("EVALUAR");
        btEvaluar.setBorderPainted(false);
        btEvaluar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btEvaluar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btEvaluarMouseClicked(evt);
            }
        });
        Contenido.add(btEvaluar, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 390, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Contenido, javax.swing.GroupLayout.PREFERRED_SIZE, 532, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Contenido, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtObservacionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtObservacionesMouseClicked
       if( txtObservaciones.getText().equals("   Ingrese sus Observaciones")){
       txtObservaciones.setText("");
      txtObservaciones.setForeground(Color.BLACK);
      }
    }//GEN-LAST:event_txtObservacionesMouseClicked

    private void CBXAprobadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CBXAprobadoMouseClicked
       if (CBXAprobado.isSelected()) {
        CBXRechazado.setSelected(false);
    }
    }//GEN-LAST:event_CBXAprobadoMouseClicked

    private void CBXRechazadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CBXRechazadoMouseClicked
        if (CBXRechazado.isSelected()) {
        CBXAprobado.setSelected(false);
    }
    }//GEN-LAST:event_CBXRechazadoMouseClicked

    private void btEvaluarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btEvaluarMouseClicked
try {
        // Obtener el estado
        String estado = null;
        if (CBXAprobado.isSelected()) {
            estado = "APROBADO";
        } else if (CBXRechazado.isSelected()) {
            estado = "RECHAZADO";
        }

        if (estado == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar Aprobado o Rechazado.");
            return;
        }

        // Obtener observaciones
        String observaciones = txtObservaciones.getText().trim();

        // Validar si está rechazado y no hay observaciones
        if (estado.equals("RECHAZADO") && (observaciones.isEmpty() || observaciones.equals("Ingrese sus Observaciones"))) {
            JOptionPane.showMessageDialog(this, "Las observaciones son obligatorias si se rechaza.");
            return;
        }

        //Obtener el formato actual
        int idFormato = formatoActual.getId();
        
        //PASO 1: Actualizar el FormatoA principal
        // Solo incrementar contador si es RECHAZADO
        int nuevoContador = formatoActual.getCounter();
        if (estado.equals("RECHAZADO")) {
            nuevoContador = formatoActual.getCounter() + 1;
            formatoActual.setCounter(nuevoContador);
        }

        // Actualizar estado y observaciones del FormatoA principal
        formatoActual.setState(enumEstado.valueOf(estado));
        formatoActual.setObservations(observaciones);

        // Llamar al servicio para actualizar el FormatoA principal
        boolean actualizado = formatoAService.updateEstadoObservacionesYContador(
           idFormato, estado, observaciones, nuevoContador);

        // 🔹 PASO 2: Actualizar la última versión con los mismos datos
        if (actualizado) {
            try {
                // Obtener el repositorio de versiones
                co.unicauca.workflow.access.FormatoAVersionRepository versionRepo = 
                    new co.unicauca.workflow.access.FormatoAVersionRepository();
                
                // Obtener todas las versiones de este formato
                java.util.List<co.unicauca.workflow.domain.entities.FormatoAVersion> versiones = 
                    versionRepo.listByFormatoA(idFormato);
                
                if (versiones != null && !versiones.isEmpty()) {
                    // Obtener la última versión (la más reciente)
                    co.unicauca.workflow.domain.entities.FormatoAVersion ultimaVersion = 
                        versiones.get(versiones.size() - 1);
                    
                    // Actualizar la última versión con el estado y observaciones de la evaluación
                    ultimaVersion.setState(co.unicauca.workflow.domain.entities.enumEstado.valueOf(estado));
                    ultimaVersion.setObservations(observaciones);
                    
                    // Guardar los cambios en la versión
                    boolean versionActualizada = versionRepo.update(ultimaVersion);
                    
                    if (versionActualizada) {
                        System.out.println("DEBUG - Última versión actualizada correctamente");
                    } else {
                        System.out.println("DEBUG - Error al actualizar la última versión");
                    }
                } else {
                    System.out.println("DEBUG - No se encontraron versiones para actualizar");
                }
                
            } catch (Exception e) {
                System.out.println("DEBUG - Error al actualizar versión: " + e.getMessage());
                e.printStackTrace();
                // No mostrar error al usuario para no confundirlo, solo log
            }
            
            JOptionPane.showMessageDialog(this, "Formato evaluado correctamente.");
            this.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el formato.");
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error al evaluar: " + ex.getMessage());
        ex.printStackTrace();
    }
    }//GEN-LAST:event_btEvaluarMouseClicked
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox CBXAprobado;
    private javax.swing.JCheckBox CBXRechazado;
    private javax.swing.JPanel Contenido;
    private javax.swing.JLabel Icon;
    private javax.swing.JButton btEvaluar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JLabel lblDirector;
    private javax.swing.JLabel lblEstudiante;
    private javax.swing.JLabel lblEstudiante2;
    private javax.swing.JLabel lblModalidad;
    private javax.swing.JLabel lblObservaciones;
    private javax.swing.JLabel lblPDF;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblUDirector;
    private javax.swing.JLabel lblUEstudiante;
    private javax.swing.JLabel lblUEstudiante2;
    private javax.swing.JLabel lblUModalidad;
    private javax.swing.JLabel lblUTitulo;
    private javax.swing.JTextArea txtObservaciones;
    // End of variables declaration//GEN-END:variables
}
