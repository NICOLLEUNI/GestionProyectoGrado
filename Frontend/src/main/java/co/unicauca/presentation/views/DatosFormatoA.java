/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package co.unicauca.presentation.views;

import co.unicauca.entity.EnumModalidad;
import co.unicauca.entity.FormatoA;
import co.unicauca.entity.Persona;
import co.unicauca.service.SubmissionService;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMTMaterialLighterIJTheme;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;



/**
 *
 * @author Usuario
 */
public class DatosFormatoA extends javax.swing.JPanel {


    // ✅ Ahora solo dependemos del microservicio
    private final SubmissionService submissionService = new SubmissionService();
    private Persona persona; // usuario logueado

    public DatosFormatoA(Persona persona) {
        initComponents();
        this.persona = persona;

        boxModalidad.setModel(new javax.swing.DefaultComboBoxModel<>(EnumModalidad.values()));
        boxModalidad.setSelectedIndex(-1);


        initStyles();
        configurarModalidadListener();

        // Cargar datos desde el microservicio
        cargarDocentesEnCombos();
        cargarEstudiantesEnCombos();
    }
    
    /**
     * Carga los docentes desde el microservicio (rol DOCENTE)
     */
    private void cargarDocentesEnCombos() {
        try {
            List<Persona> docentes = submissionService.listPersonasByRol("DOCENTE");
            
            boxDirector.removeAllItems();
            boxCodirector.removeAllItems();

            if (docentes != null && !docentes.isEmpty()) {
                for (Persona d : docentes) {
                    boxDirector.addItem(d);
                    boxCodirector.addItem(d);
                }
            }
            
            boxDirector.setSelectedIndex(-1);
            boxCodirector.setSelectedIndex(-1);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar docentes desde el microservicio.");
        }
    }
    
    /**
     * Carga los estudiantes que no tienen FormatoA asociado.
     */
    private void cargarEstudiantesEnCombos() {
        try {
            // 1️⃣ Obtener todos los estudiantes
            List<Persona> estudiantes = submissionService.listPersonasByRol("ESTUDIANTE");

            // 2️⃣ Obtener todos los formatos existentes
            List<FormatoA> formatos = submissionService.listFormatoA();

            // 3️⃣ Reunir los correos de estudiantes que ya tienen formato
            List<String> ocupadosEmails = new ArrayList<>();
            for (FormatoA f : formatos) {
                if (f.getEstudianteEmails() != null) {
                    ocupadosEmails.addAll(f.getEstudianteEmails());
                }
            }

            // 4️⃣ Filtrar estudiantes libres
            List<Persona> libres = new ArrayList<>();
            for (Persona e : estudiantes) {
                boolean ocupado = ocupadosEmails.contains(e.getEmail());
                if (!ocupado) {
                    libres.add(e);
                }
            }

            // 5️⃣ Poblar los combos
            boxEstudiante1.removeAllItems();
            boxEstudiante2.removeAllItems();

            for (Persona e : libres) {
                boxEstudiante1.addItem(e);
                boxEstudiante2.addItem(e);
            }

            boxEstudiante1.setSelectedIndex(-1);
            boxEstudiante2.setSelectedIndex(-1);
            boxEstudiante2.setEnabled(false);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar estudiantes desde el microservicio.");

        }
    }
    
    /**
     * Activa/desactiva el segundo estudiante según la modalidad
     */
    private void configurarModalidadListener() {
        boxModalidad.addActionListener(e -> {
            EnumModalidad modalidad = (EnumModalidad) boxModalidad.getSelectedItem();
            if (modalidad != null && modalidad == EnumModalidad.INVESTIGACION) {
                boxEstudiante2.setEnabled(true);
                boxEstudiante2.setSelectedIndex(-1);
            } else {
                boxEstudiante2.setEnabled(false);
                boxEstudiante2.setSelectedIndex(-1);
            }

        });
    }
    
    private void initStyles() {
    // Tema Material Lighter
    FlatMTMaterialLighterIJTheme.setup();
  // 🔹 Control del grosor del borde azul (focus)
    UIManager.put("Component.focusWidth", 1); // 0 = sin borde, 1 = finito
    UIManager.put("Component.innerFocusWidth", 0); // para controles que tengan inner focus
    // TextFields y TextArea → bordes redondeados
    UIManager.put("Component.arc", 12);       // radio del borde
    UIManager.put("TextComponent.arc", 12);
    UIManager.put("ComboBox.arc", 20); // esquinas moderadas
    // Colores para TextFields y ComboBox
    UIManager.put("TextComponent.background", Color.white);
    UIManager.put("TextComponent.foreground", Color.black);
    UIManager.put("TextComponent.placeholderForeground", new Color(150, 150, 150));
    UIManager.put("ComboBox.background", Color.white);
    UIManager.put("ComboBox.foreground", Color.black);

    // Botones estilo Material
    UIManager.put("Button.arc", 12);
    UIManager.put("Button.background", new Color(51, 51, 255));
    UIManager.put("Button.foreground", Color.white);

    // Puedes cambiar colores del scroll también
    UIManager.put("ScrollBar.thumbArc", 999);
    UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
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
        txtTitulo = new javax.swing.JTextField();
        jSeparatorTitulo = new javax.swing.JSeparator();
        lbModalidad = new javax.swing.JLabel();
        boxModalidad = new javax.swing.JComboBox<>();
        lblDirector = new javax.swing.JLabel();
        boxDirector = new javax.swing.JComboBox<>();
        lblCodirector = new javax.swing.JLabel();
        boxCodirector = new javax.swing.JComboBox<>();
        lblEstudiante1 = new javax.swing.JLabel();
        boxEstudiante1 = new javax.swing.JComboBox<>();
        lblEstudiante2 = new javax.swing.JLabel();
        boxEstudiante2 = new javax.swing.JComboBox<>();
        lbObjGen = new javax.swing.JLabel();
        txtObjGen = new javax.swing.JTextField();
        lblObjetivosEspecificos = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtObjEspecificos = new javax.swing.JTextArea();
        jSeparator1 = new javax.swing.JSeparator();
        btContinuar = new javax.swing.JButton();

        Contenido.setBackground(new java.awt.Color(255, 255, 255));
        Contenido.setPreferredSize(new java.awt.Dimension(646, 530));

        lblTitulo.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        lblTitulo.setText("Titulo");
        lblTitulo.setToolTipText("");

        txtTitulo.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        txtTitulo.setForeground(new java.awt.Color(153, 153, 153));
        txtTitulo.setText("Ingrese el Titulo del Proyecto");
        txtTitulo.setBorder(null);
        txtTitulo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTituloMouseClicked(evt);
            }
        });

        lbModalidad.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lbModalidad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbModalidad.setText("Modalidad");

        boxModalidad.setToolTipText("");
        boxModalidad.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lblDirector.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblDirector.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDirector.setText("Director de proyecto");

        lblCodirector.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblCodirector.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCodirector.setText("Codirector de proyecto");

        lblEstudiante1.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblEstudiante1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstudiante1.setText("Estudiante #1");

        lblEstudiante2.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblEstudiante2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstudiante2.setText("Estudiante #2");

        lbObjGen.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lbObjGen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbObjGen.setText("Objetivo General");

        txtObjGen.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        txtObjGen.setForeground(new java.awt.Color(153, 153, 153));
        txtObjGen.setText("   Añade el objetivo general");
        txtObjGen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtObjGenMouseClicked(evt);
            }
        });

        lblObjetivosEspecificos.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblObjetivosEspecificos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblObjetivosEspecificos.setText("Objetivos especificos");

        txtObjEspecificos.setColumns(20);
        txtObjEspecificos.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        txtObjEspecificos.setForeground(new java.awt.Color(153, 153, 153));
        txtObjEspecificos.setRows(5);
        txtObjEspecificos.setText("   Añade los objetivos especificos");
        txtObjEspecificos.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        txtObjEspecificos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtObjEspecificosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(txtObjEspecificos);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        btContinuar.setBackground(new java.awt.Color(51, 51, 255));
        btContinuar.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        btContinuar.setText("Continuar");
        btContinuar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btContinuarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout ContenidoLayout = new javax.swing.GroupLayout(Contenido);
        Contenido.setLayout(ContenidoLayout);
        ContenidoLayout.setHorizontalGroup(
            ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ContenidoLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbModalidad)
                    .addComponent(boxModalidad, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparatorTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boxEstudiante1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boxEstudiante2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEstudiante2)
                    .addComponent(lblEstudiante1)
                    .addComponent(boxDirector, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCodirector)
                    .addComponent(boxCodirector, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDirector))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtObjGen)
                    .addComponent(lbObjGen, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblObjetivosEspecificos)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ContenidoLayout.createSequentialGroup()
                        .addComponent(btContinuar, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGap(29, 29, 29))
        );
        ContenidoLayout.setVerticalGroup(
            ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ContenidoLayout.createSequentialGroup()
                .addContainerGap(50, Short.MAX_VALUE)
                .addGroup(ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ContenidoLayout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ContenidoLayout.createSequentialGroup()
                        .addGroup(ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbObjGen)
                            .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ContenidoLayout.createSequentialGroup()
                                .addComponent(txtObjGen, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)
                                .addComponent(lblObjetivosEspecificos))
                            .addGroup(ContenidoLayout.createSequentialGroup()
                                .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparatorTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lbModalidad)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(boxModalidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ContenidoLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(lblDirector, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(boxDirector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblCodirector)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(boxCodirector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblEstudiante1)
                                .addGap(7, 7, 7)
                                .addComponent(boxEstudiante1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblEstudiante2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(boxEstudiante2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(ContenidoLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btContinuar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(38, 38, 38))))
        );

        lblTitulo.getAccessibleContext().setAccessibleName(" Titulo");
        lblEstudiante1.getAccessibleContext().setAccessibleName("");
        lblEstudiante2.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Contenido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Contenido, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btContinuarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btContinuarMouseClicked
        try {
            if (!validarCampos()) return;

            FormatoA formato = new FormatoA();

            // Campos básicos
            formato.setTitle(txtTitulo.getText());
            formato.setMode((EnumModalidad) boxModalidad.getSelectedItem());
            formato.setDate(LocalDate.now());
            formato.setGeneralObjetive(txtObjGen.getText());
            formato.setSpecificObjetives(txtObjEspecificos.getText());
            formato.setArchivoPDF("pendiente.pdf");
            formato.setCartaLaboral("pendiente.pdf");

            // Director y codirector (guardar su email)
            Persona director = (Persona) boxDirector.getSelectedItem();
            Persona codirector = (Persona) boxCodirector.getSelectedItem();

            if (director != null)
                formato.setProjectManagerEmail(director.getEmail());
            if (codirector != null)
                formato.setProjectCoManagerEmail(codirector.getEmail());

            // Estudiantes: se guardan los correos, no los objetos Persona
            List<String> estudiantes = new ArrayList<>();
            if (boxEstudiante1.getSelectedItem() != null)
                estudiantes.add(((Persona) boxEstudiante1.getSelectedItem()).getEmail());
            if (boxEstudiante2.isEnabled() && boxEstudiante2.getSelectedItem() != null)
                estudiantes.add(((Persona) boxEstudiante2.getSelectedItem()).getEmail());
            formato.setEstudianteEmails(estudiantes);


            

            if (formato != null) {
                JOptionPane.showMessageDialog(this, "FormatoA registrado correctamente.");
                AdjuntarDocumentos panelDocs = new AdjuntarDocumentos(formato, persona);
                showJPanel(panelDocs);
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar el formato en el microservicio.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al continuar: " + e.getMessage());
        }

    }//GEN-LAST:event_btContinuarMouseClicked

    private void txtTituloMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTituloMouseClicked
       manejarPlaceHolder(txtTitulo, "Ingrese el Titulo del Proyecto", 
                         txtObjGen, txtObjEspecificos);
    }//GEN-LAST:event_txtTituloMouseClicked

    private void txtObjGenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtObjGenMouseClicked
       manejarPlaceHolder(txtObjGen, "   Añade el objetivo general", 
                         txtTitulo, txtObjEspecificos);                                     
    }//GEN-LAST:event_txtObjGenMouseClicked

    private void txtObjEspecificosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtObjEspecificosMouseClicked
       manejarPlaceHolder(txtObjEspecificos, "   Añade los objetivos especificos", 
                         txtTitulo, txtObjGen); 
    }//GEN-LAST:event_txtObjEspecificosMouseClicked
 
    
    private void showJPanel(JPanel pl){
        pl.setSize(641,498);
        pl.setLocation(0, 0);

        Contenido.removeAll();
        Contenido.add(pl, BorderLayout.CENTER);
        Contenido.revalidate();
        Contenido.repaint();
    }
    
    private void manejarPlaceHolder(JTextComponent campo, String placeholder, JTextComponent... otros) {
       // Si hago clic en este campo y está en modo placeholder → lo limpio
       if (campo.getText().equals(placeholder)) {
           campo.setText("");
           campo.setForeground(Color.BLACK);
       }

       // Si los otros campos están vacíos → les devuelvo su placeholder
       for (JTextComponent otro : otros) {
           if (otro.getText().isEmpty()) {
               if (otro == txtTitulo) {
                   otro.setText("Ingrese el Título del Proyecto");
               } else if (otro == txtObjGen) {
                   otro.setText("   Añade el objetivo general");
               } else if (otro == txtObjEspecificos) {
                   // Para el JTextArea
                   otro.setText("   Añade los objetivos especificos");
               }
               otro.setForeground(Color.GRAY);
           }
       }
    }
    
    private boolean validarCampos() {
        // 1️⃣ Textos vacíos o con placeholders
        if (txtTitulo.getText().equals("Ingrese el Titulo del Proyecto") || txtTitulo.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Ingrese el Título del Proyecto");
            return false;
        }

        if (txtObjGen.getText().equals("   Añade el objetivo general") || txtObjGen.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Ingrese el Objetivo General");
            return false;
        }

        if (txtObjEspecificos.getText().equals("   Añade los objetivos especificos") || txtObjEspecificos.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Ingrese los Objetivos Específicos");
            return false;
        }

        // 2️⃣ Combos sin selección
        if (boxModalidad.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione la Modalidad");
            return false;
        }

        if (boxDirector.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un Director de Proyecto");
            return false;
        }

        // Codirector opcional: solo validar si no quieres que quede vacío
        // if (boxCodirector.getSelectedIndex() == -1) { ... }

        if (boxEstudiante1.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione el Estudiante #1");
            return false;
        }


        return true; // ✅ Todo bien
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Contenido;
    private javax.swing.JComboBox<Persona> boxCodirector;
    private javax.swing.JComboBox<Persona> boxDirector;
    private javax.swing.JComboBox<Persona> boxEstudiante1;
    private javax.swing.JComboBox<Persona> boxEstudiante2;
    private javax.swing.JComboBox<EnumModalidad> boxModalidad;
    private javax.swing.JButton btContinuar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparatorTitulo;
    private javax.swing.JLabel lbModalidad;
    private javax.swing.JLabel lbObjGen;
    private javax.swing.JLabel lblCodirector;
    private javax.swing.JLabel lblDirector;
    private javax.swing.JLabel lblEstudiante1;
    private javax.swing.JLabel lblEstudiante2;
    private javax.swing.JLabel lblObjetivosEspecificos;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTextArea txtObjEspecificos;
    private javax.swing.JTextField txtObjGen;
    private javax.swing.JTextField txtTitulo;
    // End of variables declaration//GEN-END:variables
}
