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
import java.util.*;
import java.util.EnumSet;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.*;

public class GUISingIn extends javax.swing.JFrame {

    private final AuthService authService;
    private final Gson gson;
    private javax.swing.JPanel BackGround;
    public GUISingIn() {

        this.authService = new AuthService();
        this.gson = new Gson();
        initComponents();
        configurarComponentesIniciales();
        configurarListeners();
        cargarOpcionesDesdeMicroservicio(); // ✅ NUEVO: Cargar desde microservicio

    }

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

    // Listener para CheckBox Coordinador
    CBJefeDepartamento.addItemListener(new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            actualizarEstadoComboBoxes();
        }
    });
}

/**
 * Actualiza el estado de los ComboBox según los roles seleccionados
 */
private void actualizarEstadoComboBoxes() {
    boolean esEstudiante = CBEstudiante1.isSelected();
    boolean esDocenteOCoordinador = CBDocente1.isSelected() || CBJefeDepartamento.isSelected();

    // Habilitar ComboBox Programa solo si es estudiante
    ComBoxPrograma1.setEnabled(esEstudiante);
    if (!esEstudiante) {
        ComBoxPrograma1.setSelectedIndex(0); // Reset a opción por defecto
    }

    // Habilitar ComboBox Departamento solo si es docente o coordinador
    ComBoxDepartamento.setEnabled(esDocenteOCoordinador);
    if (!esDocenteOCoordinador) {
        ComBoxDepartamento.setSelectedIndex(0); // Reset a opción por defecto
    }
}

/**
 * Cargar programas desde la base de datos

 private void cargarProgramas() {
 ComBoxPrograma1.removeAllItems();
 ComBoxPrograma1.addItem("Seleccione un programa"); // Opción por defecto

 // Agregar programas hardcoded según tus especificaciones
 ComBoxPrograma1.addItem("Ingeniería de Sistemas");
 ComBoxPrograma1.addItem("Ingeniería Electrónica y Telecomunicaciones");
 ComBoxPrograma1.addItem("Automática industrial");
 ComBoxPrograma1.addItem("Tecnología en Telemática");

 // Intentar cargar desde BD como respaldo (opcional)
 try {
 List<Programa> programas = programaRepository.list();
 if (!programas.isEmpty()) {
 // Si hay programas en BD, agregar los que no están hardcoded
 for (Programa programa : programas) {
 String nombrePrograma = programa.getNombrePrograma();
 boolean yaExiste = false;
 for (int i = 0; i < ComBoxPrograma1.getItemCount(); i++) {
 if (ComBoxPrograma1.getItemAt(i).equals(nombrePrograma)) {
 yaExiste = true;
 break;
 }
 }
 if (!yaExiste) {
 ComBoxPrograma1.addItem(nombrePrograma);
 }
 }
 }
 } catch (Exception e) {
 System.err.println("Error cargando programas desde BD: " + e.getMessage());
 // Los hardcoded ya están cargados, continuar
 }
 }*/

/**
 * Cargar departamentos desde la base de datos

 private void cargarDepartamentos() {
 ComBoxPrograma1.removeAllItems();
 ComBoxPrograma1.addItem("Seleccione un programa"); // Opción por defecto

 // Agregar programas hardcoded según tus especificaciones
 ComBoxPrograma1.addItem("Ingeniería de Sistemas");
 ComBoxPrograma1.addItem("Ingeniería Electrónica y Telecomunicaciones");
 ComBoxPrograma1.addItem("Automática industrial");
 ComBoxPrograma1.addItem("Tecnología en Telemática");


 // ComboBox Departamentos (para docentes/coordinadores)
 ComBoxDepartamento.addItem("Seleccione un departamento");
 ComBoxDepartamento.addItem("Sistemas");
 ComBoxDepartamento.addItem("Electrónica");                 // ← NUEVO
 ComBoxDepartamento.addItem("Instrumentación y Control");    // ← NUEVO
 ComBoxDepartamento.addItem("Telemática");                  // ← NUEVO

 // Intentar cargar desde BD como respaldo (opcional)
 try {
 List<Programa> programas = programaRepository.list();
 if (!programas.isEmpty()) {
 // Si hay programas en BD, agregar los que no están hardcoded
 for (Programa programa : programas) {
 String nombrePrograma = programa.getNombrePrograma();
 boolean yaExiste = false;
 for (int i = 0; i < ComBoxPrograma1.getItemCount(); i++) {
 if (ComBoxPrograma1.getItemAt(i).equals(nombrePrograma)) {
 yaExiste = true;
 break;
 }
 }
 if (!yaExiste) {
 ComBoxPrograma1.addItem(nombrePrograma);
 }
 }
 }
 } catch (Exception e) {
 System.err.println("Error cargando programas desde BD: " + e.getMessage());
 // Los hardcoded ya están cargados, continuar
 }
 }
 */


/**
 * This method is called from within the constructor to initialize the form.
 * WARNING: Do NOT modify this code. The content of this method is always
 * regenerated by the Form Editor.
 */
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
    CBJefeDepartamento = new javax.swing.JCheckBox();
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
    CBECoordinador2 = new javax.swing.JCheckBox();
    BgImage = new javax.swing.JLabel();

    this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    this.setLocationByPlatform(true);
    this.setResizable(false);

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
                    .addComponent(lblBttRegistrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    CBJefeDepartamento.setBackground(new java.awt.Color(255, 255, 255));
    CBJefeDepartamento.setFont(new java.awt.Font("Roboto Medium", 0, 12)); // NOI18N
    CBJefeDepartamento.setForeground(new java.awt.Color(0, 0, 0));
    CBJefeDepartamento.setText("Jefe Departamento");
    CBJefeDepartamento.setToolTipText("");
    CBJefeDepartamento.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            CBJefeDepartamentoActionPerformed(evt);
        }
    });
    pnlBack.add(CBJefeDepartamento, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 330, -1, -1));

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


    