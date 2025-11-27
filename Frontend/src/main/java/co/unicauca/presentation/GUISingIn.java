/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package co.unicauca.presentation;
import co.unicauca.entity.EnumRol;

import co.unicauca.service.AuthService;
import co.unicauca.entity.Persona;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.EnumSet;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.*;
import java.util.stream.Collectors;

public class GUISingIn extends javax.swing.JFrame {

    private final AuthService authService;
    private final Gson gson;
    public GUISingIn() {

        this.authService = new AuthService();
        this.gson = new Gson();
        initComponents();
        configurarComponentesIniciales();
        configurarListeners();
        cargarOpcionesDesdeMicroservicio(); // ✅ NUEVO: Cargar desde microservicio

    }



/**
 * Configuración inicial de ComboBox y componentes
 */
private void configurarComponentesIniciales() {
    // Deshabilitar ComboBox inicialmente
    ComBoxPrograma1.setEnabled(false);
    ComBoxDepartamento.setEnabled(false);

    // Cargar programas en ComboBox
    cargarProgramasFallback();
    cargarDepartamentosFallback();
}

/**
 * Configurar listeners para CheckBoxes de roles
 */
private void configurarListeners() {
    // Listener para CheckBox Estudiante
    CBEstudiante1.addItemListener(new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            actualizarEstadoComboBoxes();
        }
    });

    // Listener para CheckBox Docente
    CBDocente1.addItemListener(new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            actualizarEstadoComboBoxes();
        }
    });

    // Listener para CheckBox Coordinador (ambos si existen)
    CBECoordinador1.addItemListener(new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            actualizarEstadoComboBoxes();
        }
    });

    if (CBECoordinador1 != null) {
        CBECoordinador1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                actualizarEstadoComboBoxes();
            }
        });
    }

    // Listener para CheckBox Jefe Departamento (si existe)
    if (CBEJefeDepartamento != null) {
        CBEJefeDepartamento.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                actualizarEstadoComboBoxes();
            }
        });
    }
}

/**
 * Actualiza el estado de los ComboBox según los roles seleccionados
 */
private void actualizarEstadoComboBoxes() {
    // ✅ CORREGIDO: Usar ambos checkboxes de coordinador si existen
    boolean requierePrograma = CBEstudiante1.isSelected() ||
            CBECoordinador1.isSelected() ||
            (CBECoordinador1 != null && CBECoordinador1.isSelected());

    // ✅ CORREGIDO: Verificar que CBEJefeDepartamento existe
    boolean requiereDepartamento = CBDocente1.isSelected() ||
            (CBEJefeDepartamento != null && CBEJefeDepartamento.isSelected());

    // Habilitar ComboBox Programa si requiere programa
    ComBoxPrograma1.setEnabled(requierePrograma);
    if (!requierePrograma) {
        ComBoxPrograma1.setSelectedIndex(0); // Reset a opción por defecto
    }

    // Habilitar ComboBox Departamento si requiere departamento
    ComBoxDepartamento.setEnabled(requiereDepartamento);
    if (!requiereDepartamento) {
        ComBoxDepartamento.setSelectedIndex(0); // Reset a opción por defecto
    }
}

@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BackGround = new javax.swing.JPanel();
        lblLogoR = new javax.swing.JLabel();
        pnlBack = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblCelular = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblApellidos = new javax.swing.JLabel();
        lblPrograma = new javax.swing.JLabel();
        lblContraseniaR = new javax.swing.JLabel();
        lblRol = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        pnlBttRegistrar = new javax.swing.JPanel();
        lblBttRegistrar = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        txtCelular = new javax.swing.JTextField();
        txtContrasenia = new javax.swing.JPasswordField();
        CBEJefeDepartamento = new javax.swing.JCheckBox();
        ComBoxDepartamento = new javax.swing.JComboBox<>();
        jSeparator8 = new javax.swing.JSeparator();
        txtNombre = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        jSeparator9 = new javax.swing.JSeparator();
        jSeparator10 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        txtApellidos = new javax.swing.JTextField();
        jSeparator12 = new javax.swing.JSeparator();
        jSeparator13 = new javax.swing.JSeparator();
        jSeparator14 = new javax.swing.JSeparator();
        jSeparator15 = new javax.swing.JSeparator();
        CBDocente1 = new javax.swing.JCheckBox();
        CBEstudiante1 = new javax.swing.JCheckBox();
        lblPrograma1 = new javax.swing.JLabel();
        ComBoxPrograma1 = new javax.swing.JComboBox<>();
        CBECoordinador1 = new javax.swing.JCheckBox();
        BgImage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        BackGround.setBackground(new java.awt.Color(0, 64, 128));
        BackGround.setPreferredSize(new java.awt.Dimension(910, 510));
        BackGround.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblLogoR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/co/unicauca/presentation/images/LogoPequeño.png"))); // NOI18N
        lblLogoR.setText("jLabel1");
        BackGround.add(lblLogoR, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 163, 80));

        pnlBack.setBackground(new java.awt.Color(255, 255, 255));
        pnlBack.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("Roboto SemiBold", 1, 36)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(0, 0, 0));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("REGISTRO");
        pnlBack.add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 310, 50));

        lblCelular.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblCelular.setForeground(new java.awt.Color(0, 0, 0));
        lblCelular.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCelular.setText("Celular");
        pnlBack.add(lblCelular, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, 60, -1));

        lblNombre.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblNombre.setForeground(new java.awt.Color(0, 0, 0));
        lblNombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNombre.setText("Nombres");
        pnlBack.add(lblNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, -1, -1));

        lblApellidos.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblApellidos.setForeground(new java.awt.Color(0, 0, 0));
        lblApellidos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblApellidos.setText("Apellidos");
        pnlBack.add(lblApellidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 110, -1, -1));

        lblPrograma.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblPrograma.setForeground(new java.awt.Color(0, 0, 0));
        lblPrograma.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPrograma.setText("Departamento Institucional");
        pnlBack.add(lblPrograma, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 270, -1, -1));

        lblContraseniaR.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblContraseniaR.setForeground(new java.awt.Color(0, 0, 0));
        lblContraseniaR.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblContraseniaR.setText("Contraseña");
        pnlBack.add(lblContraseniaR, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 380, -1, -1));

        lblRol.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblRol.setForeground(new java.awt.Color(0, 0, 0));
        lblRol.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRol.setText("Rol");
        pnlBack.add(lblRol, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 270, -1, -1));

        lblEmail.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblEmail.setForeground(new java.awt.Color(0, 0, 0));
        lblEmail.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEmail.setText("Email");
        pnlBack.add(lblEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 340, -1, -1));

        pnlBttRegistrar.setBackground(new java.awt.Color(0, 102, 204));
        pnlBttRegistrar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblBttRegistrar.setFont(new java.awt.Font("Roboto Medium", 1, 18)); // NOI18N
        lblBttRegistrar.setForeground(new java.awt.Color(255, 255, 255));
        lblBttRegistrar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBttRegistrar.setText("REGISTRARME");
        lblBttRegistrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblBttRegistrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBttRegistrarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblBttRegistrarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblBttRegistrarMouseExited(evt);
            }
        });

        javax.swing.GroupLayout pnlBttRegistrarLayout = new javax.swing.GroupLayout(pnlBttRegistrar);
        pnlBttRegistrar.setLayout(pnlBttRegistrarLayout);
        pnlBttRegistrarLayout.setHorizontalGroup(
            pnlBttRegistrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblBttRegistrar, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
        );
        pnlBttRegistrarLayout.setVerticalGroup(
            pnlBttRegistrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblBttRegistrar, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
        );

        pnlBack.add(pnlBttRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 400, 150, 40));
        pnlBack.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 170, 230, 20));

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        pnlBack.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 300, 30, 50));

        txtCelular.setBackground(new java.awt.Color(255, 255, 255));
        txtCelular.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        txtCelular.setForeground(new java.awt.Color(153, 153, 153));
        txtCelular.setText("Ingrese su celular");
        txtCelular.setBorder(null);
        txtCelular.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtCelularMousePressed(evt);
            }
        });
        pnlBack.add(txtCelular, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, 230, 20));

        txtContrasenia.setBackground(new java.awt.Color(255, 255, 255));
        txtContrasenia.setForeground(new java.awt.Color(153, 153, 153));
        txtContrasenia.setText("********");
        txtContrasenia.setBorder(null);
        txtContrasenia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtContraseniaMousePressed(evt);
            }
        });
        pnlBack.add(txtContrasenia, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 410, 200, -1));

        CBEJefeDepartamento.setBackground(new java.awt.Color(255, 255, 255));
        CBEJefeDepartamento.setFont(new java.awt.Font("Roboto Medium", 0, 12)); // NOI18N
        CBEJefeDepartamento.setForeground(new java.awt.Color(0, 0, 0));
        CBEJefeDepartamento.setText("JefeDepartamento");
        CBEJefeDepartamento.setToolTipText("");
        CBEJefeDepartamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBEJefeDepartamentoActionPerformed(evt);
            }
        });
        pnlBack.add(CBEJefeDepartamento, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 300, -1, -1));

        ComBoxDepartamento.setBackground(new java.awt.Color(255, 255, 255));
        ComBoxDepartamento.setFont(new java.awt.Font("Roboto Medium", 0, 12)); // NOI18N
        ComBoxDepartamento.setToolTipText("");
        ComBoxDepartamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComBoxDepartamentoActionPerformed(evt);
            }
        });
        pnlBack.add(ComBoxDepartamento, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 300, 230, 30));
        pnlBack.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 430, 230, 20));

        txtNombre.setBackground(new java.awt.Color(255, 255, 255));
        txtNombre.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        txtNombre.setForeground(new java.awt.Color(153, 153, 153));
        txtNombre.setText("Ingrese su Nombre");
        txtNombre.setBorder(null);
        txtNombre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtNombreMousePressed(evt);
            }
        });
        pnlBack.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, 230, 20));

        txtEmail.setBackground(new java.awt.Color(255, 255, 255));
        txtEmail.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        txtEmail.setForeground(new java.awt.Color(153, 153, 153));
        txtEmail.setText("Ingrese su Email");
        txtEmail.setBorder(null);
        txtEmail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtEmailMousePressed(evt);
            }
        });
        pnlBack.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 370, 230, 20));
        pnlBack.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 170, 230, 20));

        jSeparator10.setOrientation(javax.swing.SwingConstants.VERTICAL);
        pnlBack.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 110, 30, 50));
        pnlBack.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 260, 230, 20));

        txtApellidos.setBackground(new java.awt.Color(255, 255, 255));
        txtApellidos.setFont(new java.awt.Font("Roboto Medium", 1, 12)); // NOI18N
        txtApellidos.setForeground(new java.awt.Color(153, 153, 153));
        txtApellidos.setText("Ingrese sus Apellidos");
        txtApellidos.setBorder(null);
        txtApellidos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtApellidosMousePressed(evt);
            }
        });
        pnlBack.add(txtApellidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 150, 230, 20));
        pnlBack.add(jSeparator12, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 260, 230, 20));

        jSeparator13.setOrientation(javax.swing.SwingConstants.VERTICAL);
        pnlBack.add(jSeparator13, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 200, 30, 50));
        pnlBack.add(jSeparator14, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 390, 230, 20));
        pnlBack.add(jSeparator15, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 370, 230, 30));

        CBDocente1.setBackground(new java.awt.Color(255, 255, 255));
        CBDocente1.setFont(new java.awt.Font("Roboto Medium", 0, 12)); // NOI18N
        CBDocente1.setForeground(new java.awt.Color(0, 0, 0));
        CBDocente1.setText("Docente");
        pnlBack.add(CBDocente1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 330, -1, -1));

        CBEstudiante1.setBackground(new java.awt.Color(255, 255, 255));
        CBEstudiante1.setFont(new java.awt.Font("Roboto Medium", 0, 12)); // NOI18N
        CBEstudiante1.setForeground(new java.awt.Color(0, 0, 0));
        CBEstudiante1.setText("Estudiante");
        CBEstudiante1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBEstudiante1ActionPerformed(evt);
            }
        });
        pnlBack.add(CBEstudiante1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 300, -1, -1));

        lblPrograma1.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        lblPrograma1.setForeground(new java.awt.Color(0, 0, 0));
        lblPrograma1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPrograma1.setText("Programa Institucional");
        pnlBack.add(lblPrograma1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 200, -1, -1));

        ComBoxPrograma1.setBackground(new java.awt.Color(255, 255, 255));
        ComBoxPrograma1.setFont(new java.awt.Font("Roboto Medium", 0, 12)); // NOI18N
        ComBoxPrograma1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Programa", "Ingeniería de Sistemas", "Ingeniería Electrónica y Telecomunicaciones", "Automática industrial", "Tecnología en Telemática" }));
        pnlBack.add(ComBoxPrograma1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 220, 230, 30));

        CBECoordinador1.setBackground(new java.awt.Color(255, 255, 255));
        CBECoordinador1.setFont(new java.awt.Font("Roboto Medium", 0, 12)); // NOI18N
        CBECoordinador1.setForeground(new java.awt.Color(0, 0, 0));
        CBECoordinador1.setText("Coordinador");
        CBECoordinador1.setToolTipText("");
        CBECoordinador1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBECoordinador1ActionPerformed(evt);
            }
        });
        pnlBack.add(CBECoordinador1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 330, -1, -1));

        BackGround.add(pnlBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 20, -1, -1));

        BgImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        BgImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/co/unicauca/presentation/images/BackGroundSingIn.png"))); // NOI18N
        BackGround.add(BgImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 770, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BackGround, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BackGround, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CBEJefeDepartamentoActionPerformed(ActionEvent evt) {
    }

    private void txtNombreMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNombreMousePressed
    manejarPlaceHolder(txtNombre, "Ingrese su Nombre", txtEmail, txtCelular, txtApellidos);
    restaurarPlaceholderPassword(txtContrasenia);
}//GEN-LAST:event_txtNombreMousePressed

private void txtApellidosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtApellidosMousePressed
    manejarPlaceHolder(txtApellidos, "Ingrese sus Apellidos", txtEmail, txtCelular, txtNombre);
    restaurarPlaceholderPassword(txtContrasenia);
}//GEN-LAST:event_txtApellidosMousePressed


private void txtCelularMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCelularMousePressed
    manejarPlaceHolder(txtCelular, "Ingrese su celular", txtNombre, txtEmail, txtApellidos);
    restaurarPlaceholderPassword(txtContrasenia);
}//GEN-LAST:event_txtCelularMousePressed

private void txtEmailMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtEmailMousePressed
    manejarPlaceHolder(txtEmail, "Ingrese su Email", txtNombre, txtCelular, txtApellidos);
    restaurarPlaceholderPassword(txtContrasenia);
}//GEN-LAST:event_txtEmailMousePressed

private void txtContraseniaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtContraseniaMousePressed
    // Si el campo contraseña está en modo placeholder → lo limpio
    if (String.valueOf(txtContrasenia.getPassword()).equals("********")) {
        txtContrasenia.setText("");
        txtContrasenia.setForeground(Color.BLACK);
    }

    // Si los otros campos están vacíos → les devuelvo su placeholder
    if (txtNombre.getText().isEmpty()) {
        txtNombre.setText("Ingrese su Nombre");
        txtNombre.setForeground(Color.GRAY);
    }
    if (txtEmail.getText().isEmpty()) {
        txtEmail.setText("Ingrese su Email");
        txtEmail.setForeground(Color.GRAY);
    }
    if (txtCelular.getText().isEmpty()) {
        txtCelular.setText("Ingrese su celular");
        txtCelular.setForeground(Color.GRAY);
    }
    if (txtApellidos.getText().isEmpty()) {
        txtApellidos.setText("Ingrese sus Apellidos");
        txtApellidos.setForeground(Color.GRAY);
    }
}//GEN-LAST:event_txtContraseniaMousePressed

private void lblBttRegistrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBttRegistrarMouseEntered
    pnlBttRegistrar.setBackground(new Color(0, 64, 128));
}//GEN-LAST:event_lblBttRegistrarMouseEntered

private void lblBttRegistrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBttRegistrarMouseExited
    pnlBttRegistrar.setBackground(new Color(0, 102, 204));
}//GEN-LAST:event_lblBttRegistrarMouseExited

/**
 * ✅ Cargar programas y departamentos desde el microservicio
 */

private void cargarOpcionesDesdeMicroservicio() {
    try {
        JsonObject opciones = authService.getRegistrationOptions();

        // Cargar programas
        ComBoxPrograma1.removeAllItems();
        ComBoxPrograma1.addItem("Seleccione un programa");
        JsonArray programasArray = opciones.get("programas").getAsJsonArray();
        for (int i = 0; i < programasArray.size(); i++) {
            ComBoxPrograma1.addItem(programasArray.get(i).getAsString());
        }

        // Cargar departamentos
        ComBoxDepartamento.removeAllItems();
        ComBoxDepartamento.addItem("Seleccione un departamento");
        JsonArray departamentosArray = opciones.get("departamentos").getAsJsonArray();
        for (int i = 0; i < departamentosArray.size(); i++) {
            ComBoxDepartamento.addItem(departamentosArray.get(i).getAsString());
        }

    } catch (Exception e) {
        // ✅ CORREGIDO: Cargar valores por defecto SIN mostrar advertencia al usuario
        cargarProgramasFallback();
        cargarDepartamentosFallback();
        // ❌ ELIMINADO: El mensaje de advertencia que afecta la experiencia
    }
}

/**
 * ✅ Fallback para programas
 */
private void cargarProgramasFallback() {
    ComBoxPrograma1.removeAllItems();
    ComBoxPrograma1.addItem("Seleccione un programa");
    ComBoxPrograma1.addItem("INGENIERIA_DE_SISTEMAS");
    ComBoxPrograma1.addItem("INGENIERIA_ELECTRONICA_Y_TELECOMUNICACIONES");
    ComBoxPrograma1.addItem("AUTOMATICA_INDUSTRIAL");
    ComBoxPrograma1.addItem("TECNOLOGIA_EN_TELEMATICA");
}

/**
 * ✅ Fallback para departamentos
 */
private void cargarDepartamentosFallback() {
    ComBoxDepartamento.removeAllItems();
    ComBoxDepartamento.addItem("Seleccione un departamento");
    ComBoxDepartamento.addItem("SISTEMAS");
    ComBoxDepartamento.addItem("ELECTRONICA");
    ComBoxDepartamento.addItem("TELECOMUNICACIONES");
    ComBoxDepartamento.addItem("TELEMATICA");
    ComBoxDepartamento.addItem("AUTOMATICA_INDUSTRIAL");
}

    private void lblBttRegistrarMouseClicked(java.awt.event.MouseEvent evt) {
        // ✅ SOLO validar campos vacíos en el frontend
        String nombre = txtNombre.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String celular = txtCelular.getText().trim();
        String email = txtEmail.getText().trim();
        String password = String.valueOf(txtContrasenia.getPassword()).trim();

        List<String> erroresFrontend = new ArrayList<>();

        // Validar campos obligatorios
        if (nombre.isEmpty() || nombre.equals("Ingrese su Nombre")) {
            erroresFrontend.add("• El nombre es obligatorio");
        }
        if (apellidos.isEmpty() || apellidos.equals("Ingrese sus Apellidos")) {
            erroresFrontend.add("• Los apellidos son obligatorios");
        }
        if (email.isEmpty() || email.equals("Ingrese su Email")) {
            erroresFrontend.add("• El email es obligatorio");
        }
        if (password.isEmpty() || password.equals("")) {
            erroresFrontend.add("• La contraseña es obligatoria");
        }

        // Validar nombre - solo caracteres y mínimo 2
        if (!nombre.isEmpty() && !nombre.equals("Ingrese su Nombre")) {
            if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{2,}$")) {
                erroresFrontend.add("• El nombre debe contener solo letras y tener al menos 2 caracteres");
            }
        }

        // Validar apellidos - solo caracteres y mínimo 2
        if (!apellidos.isEmpty() && !apellidos.equals("Ingrese sus Apellidos")) {
            if (!apellidos.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{2,}$")) {
                erroresFrontend.add("• Los apellidos deben contener solo letras y tener al menos 2 caracteres");
            }
        }


        // ✅ AGREGADO: Validar email institucional
        if (!email.isEmpty() && !email.equals("Ingrese su Email") && !email.toLowerCase().endsWith("@unicauca.edu.co")) {
            erroresFrontend.add("• El email debe ser institucional (@unicauca.edu.co)");
        }

        // ✅ AGREGADO: Validar contraseña fuerte
        if (!password.isEmpty() && !password.equals("")) {
            String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{6,}$";
            if (!password.matches(passwordRegex)) {
                erroresFrontend.add("• La contraseña debe tener al menos 6 caracteres, 1 mayúscula, 1 número y 1 carácter especial");
            }
        }

        // ✅ AGREGADO: Validar teléfono (10 dígitos)
        if (!celular.isEmpty() && !celular.equals("Ingrese su celular") && !celular.matches("^[0-9]{10}$")) {
            erroresFrontend.add("• El celular debe tener 10 dígitos numéricos");
        }

        // Validar roles seleccionados
        Set<EnumRol> roles = obtenerRolesSeleccionados();
        if (roles.isEmpty()) {
            erroresFrontend.add("• Debe seleccionar al menos un rol");
        }

        // ✅ CORREGIDO: Validar programa para Estudiante Y Coordinador
        if (roles.contains(EnumRol.ESTUDIANTE) || roles.contains(EnumRol.COORDINADOR)) {
            String programa = (String) ComBoxPrograma1.getSelectedItem();
            if (programa == null || programa.equals("Seleccione un programa")) {
                erroresFrontend.add("• Debe seleccionar un programa para los roles Estudiante o Coordinador");
            }
        }

        // ✅ CORREGIDO: Validar departamento para Docente Y Jefe Departamento
        boolean requiereDepto = roles.contains(EnumRol.DOCENTE) || roles.contains(EnumRol.JEFE_DEPARTAMENTO);
        if (requiereDepto) {
            String departamento = (String) ComBoxDepartamento.getSelectedItem();
            if (departamento == null || departamento.equals("Seleccione un departamento")) {
                erroresFrontend.add("• Debe seleccionar un departamento para los roles Docente o Jefe de Departamento");
            }
        }

        // Mostrar errores de frontend
        if (!erroresFrontend.isEmpty()) {
            StringBuilder mensaje = new StringBuilder("Por favor complete:\n\n");
            for (String error : erroresFrontend) {
                mensaje.append(error).append("\n");
            }
            JOptionPane.showMessageDialog(this, mensaje.toString(),
                    "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // ✅ AQUÍ VA EL CÓDIGO - JUSTO ANTES DE LLAMAR AL SERVICIO
            String programa = (roles.contains(EnumRol.ESTUDIANTE) || roles.contains(EnumRol.COORDINADOR)) ?
                    (String) ComBoxPrograma1.getSelectedItem() : null;

            String departamento = (roles.contains(EnumRol.DOCENTE) || roles.contains(EnumRol.JEFE_DEPARTAMENTO)) ?
                    (String) ComBoxDepartamento.getSelectedItem() : null;

            // Procesar celular (opcional)
            String celularProcesado = (celular.isEmpty() || celular.equals("Ingrese su celular")) ?
                    null : celular;

            // Llamar al microservicio
            Persona personaRegistrada = authService.register(
                    nombre, apellidos, celularProcesado, email, password,
                    roles, programa, departamento
            );

            // ✅ Éxito - microservicio aprobó todas las validaciones
            JOptionPane.showMessageDialog(this,
                    "¡Registro exitoso!\nBienvenido/a " + personaRegistrada.getName(),
                    "Registro Completado",
                    JOptionPane.INFORMATION_MESSAGE);

            irALogin();

        } catch (Exception e) {
            // ✅ MOSTRAR ERRORES DEL MICROSERVICIO
            JOptionPane.showMessageDialog(this,
                    e.getMessage(), // Mensaje del microservicio
                    "Error en Registro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }



/**
 * Validar campos básicos
 */
private String validarCamposBasicos(String nombre, String apellidos, String email, String password) {
    if (nombre.isEmpty() || nombre.equals("Ingrese su Nombre") ||
            apellidos.isEmpty() || apellidos.equals("Ingrese sus Apellidos ") ||
            email.isEmpty() || email.equals("Ingrese su Email ") ||
            password.isEmpty() || password.equals("********")) {
        return "Por favor completa todos los campos obligatorios.";
    }
    return null; // Sin errores
}


/**
 * Obtener roles seleccionados
 */
    /**
     * ✅ Obtener roles seleccionados - ACTUALIZADO para nuevo flujo
     */
    private Set<EnumRol> obtenerRolesSeleccionados() {
        Set<EnumRol> roles = EnumSet.noneOf(EnumRol.class);

        if (CBEstudiante1.isSelected()) {
            roles.add(EnumRol.ESTUDIANTE);
        }
        if (CBDocente1.isSelected()) {
            roles.add(EnumRol.DOCENTE);
        }
        if (CBECoordinador1.isSelected() || (CBECoordinador1 != null && CBECoordinador1.isSelected())) {
            roles.add(EnumRol.COORDINADOR);
        }
        if (CBEJefeDepartamento != null && CBEJefeDepartamento.isSelected()) {
            roles.add(EnumRol.JEFE_DEPARTAMENTO);
        }

        return roles;
    }

/**
 * Procesar celular (opcional)
 */
private String procesarCelular(String celularStr) {
    if (celularStr.isEmpty() || celularStr.equals("Ingrese su celular")) {
        return null; // Celular opcional
    }

    if (!celularStr.matches("\\d+")) {
        return null; // Inválido
    }

    return celularStr;
}

private void CBJefeDepartamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBJefeDepartamentoActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_CBJefeDepartamentoActionPerformed

private void CBEstudiante1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBEstudiante1ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_CBEstudiante1ActionPerformed

private void ComBoxDepartamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComBoxDepartamentoActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_ComBoxDepartamentoActionPerformed

private void CBECoordinador1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBECoordinador1ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_CBECoordinador1ActionPerformed

private void CBECoordinador2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBECoordinador2ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_CBECoordinador2ActionPerformed

public void irALogin() {
    GUILogin ventanaLogin = new GUILogin();
    ventanaLogin.setVisible(true);
    this.dispose();
}

// MANTENER TUS MÉTODOS DE PLACEHOLDER EXISTENTES
private void manejarPlaceHolder(JTextField campo, String placeholder, JTextField... otros) {
    if (campo.getText().equals(placeholder)) {
        campo.setText("");
        campo.setForeground(Color.BLACK);
    }
    for (JTextField otro : otros) {
        if (otro.getText().isEmpty()) {
            if (otro == txtNombre) otro.setText("Ingrese su Nombre");
            if (otro == txtEmail) otro.setText("Ingrese su Email");
            if (otro == txtCelular) otro.setText("Ingrese su celular");
            if (otro == txtApellidos) otro.setText("Ingrese sus Apellidos");
            otro.setForeground(Color.GRAY);
        }
    }
}


private void restaurarPlaceholderPassword(JPasswordField campo) {
    if (String.valueOf(campo.getPassword()).isEmpty()) {
        campo.setText("********");
        campo.setForeground(Color.GRAY);
    }
}


/**
 * @param args the command line arguments
 */
public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
     * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
     */
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (ClassNotFoundException ex) {
        java.util.logging.Logger.getLogger(GUISingIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
        java.util.logging.Logger.getLogger(GUISingIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
        java.util.logging.Logger.getLogger(GUISingIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
        java.util.logging.Logger.getLogger(GUISingIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            new GUISingIn().setVisible(true);
        }
    });
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BackGround;
    private javax.swing.JLabel BgImage;
    private javax.swing.JCheckBox CBDocente1;
    private javax.swing.JCheckBox CBECoordinador1;
    private javax.swing.JCheckBox CBEJefeDepartamento;
    private javax.swing.JCheckBox CBEstudiante1;
    private javax.swing.JComboBox<String> ComBoxDepartamento;
    private javax.swing.JComboBox<String> ComBoxPrograma1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JLabel lblApellidos;
    private javax.swing.JLabel lblBttRegistrar;
    private javax.swing.JLabel lblCelular;
    private javax.swing.JLabel lblContraseniaR;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblLogoR;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblPrograma;
    private javax.swing.JLabel lblPrograma1;
    private javax.swing.JLabel lblRol;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlBack;
    private javax.swing.JPanel pnlBttRegistrar;
    private javax.swing.JTextField txtApellidos;
    private javax.swing.JTextField txtCelular;
    private javax.swing.JPasswordField txtContrasenia;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables

}




