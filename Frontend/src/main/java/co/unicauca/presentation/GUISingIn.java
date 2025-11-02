/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package co.unicauca.presentation;

//Arreglar el combobox de programa intitucional ya que con algunas carreras no permite el registro
//la funcionalidad de programa solo se abilita para estudiante

//Añadir el rol del coordinador
//ñadir el campo donde se elije el departamento   
//Para docente y coordinador se debe habilitar el campo de departamento al que pertenece 

//Debemos usar la instancia de factory no crear un nuevo repositorio

//DEBES IMPLEMENTAR TODA ESTA LOGICA CON LA CLASE PERSONA

// IMPORTS NECESARIOS - AGREGAR AL INICIO DE TU ARCHIVO


import co.unicauca.workflow.domain.service.PersonaService;
import co.unicauca.workflow.domain.entities.enumRol;
import co.unicauca.workflow.domain.entities.Programa;
import co.unicauca.workflow.domain.entities.Departamento;
import co.unicauca.workflow.domain.entities.Facultad;
import co.unicauca.workflow.domain.exceptions.ValidationException;
import co.unicauca.workflow.access.Factory;
import co.unicauca.workflow.access.IPersonaRepository;
import co.unicauca.workflow.access.IEstudianteRepository;
import co.unicauca.workflow.access.IDocenteRepository;
import co.unicauca.workflow.access.ICoordinadorRepository;
import co.unicauca.workflow.access.IProgramaRepository;
import co.unicauca.workflow.access.IDepartamentoRepository;
import java.util.EnumSet;
import java.util.List;
import java.util.ArrayList;
import java.awt.Color;
import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


/**
 *
 * @author User
 */
public class GUISingIn extends javax.swing.JFrame {
    
private final PersonaService personaService;
    private final IProgramaRepository programaRepository;
    private final IDepartamentoRepository departamentoRepository;
    

    public GUISingIn() {
       
       initComponents();
        
        // ← Usar Factory correctamente como se requiere
        Factory factory = Factory.getInstance();
        IPersonaRepository personaRepo = Factory.getPersonaRepository("default");
        IEstudianteRepository estudianteRepo = Factory.getEstudianteRepository("default");
        IDocenteRepository docenteRepo = Factory.getDocenteRepository("default");
        ICoordinadorRepository coordinadorRepo = Factory.getCoordinadorRepository("default");
        
        // Repositorios para guardar solo Programa y Departamento
        this.programaRepository = Factory.getProgramaRepository("default");
        this.departamentoRepository = Factory.getDepartamentoRepository("default");
        
        this.personaService = new PersonaService(personaRepo, estudianteRepo, docenteRepo, coordinadorRepo);
        
        // Configurar componentes iniciales
        configurarComponentesIniciales();
        configurarListeners(); 
        
        
    }
       /**
     * Configuración inicial de ComboBox y componentes
     */
    private void configurarComponentesIniciales() {
        // Deshabilitar ComboBox inicialmente
        ComBoxPrograma1.setEnabled(false);
        ComBoxDepartamento.setEnabled(false);
        
        // Cargar programas en ComboBox
        cargarProgramas();
        cargarDepartamentos();
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
     */
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
    }
        
    /**
     * Cargar departamentos desde la base de datos
     */
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setResizable(false);

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
        pnlBack.add(CBECoordinador1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 300, -1, -1));

        CBECoordinador2.setBackground(new java.awt.Color(255, 255, 255));
        CBECoordinador2.setFont(new java.awt.Font("Roboto Medium", 0, 12)); // NOI18N
        CBECoordinador2.setForeground(new java.awt.Color(0, 0, 0));
        CBECoordinador2.setText("Coordinador");
        CBECoordinador2.setToolTipText("");
        CBECoordinador2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBECoordinador2ActionPerformed(evt);
            }
        });
        pnlBack.add(CBECoordinador2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 300, -1, -1));

        BackGround.add(pnlBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 20, 640, 470));

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
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
      pnlBttRegistrar.setBackground(new Color(0,64,128));
    }//GEN-LAST:event_lblBttRegistrarMouseEntered

    private void lblBttRegistrarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBttRegistrarMouseExited
     pnlBttRegistrar.setBackground(new Color(0,102,204));
    }//GEN-LAST:event_lblBttRegistrarMouseExited

    private void lblBttRegistrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBttRegistrarMouseClicked

       // Obtener todos los datos del formulario
        String nombre = txtNombre.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String celularStr = txtCelular.getText().trim();
        String email = txtEmail.getText().trim();
        String password = String.valueOf(txtContrasenia.getPassword()).trim();
        
        // Validar todos los campos y recopilar errores
        List<String> errores = new ArrayList<>();
        
        // 1. Validar campos básicos obligatorios
        if (nombre.isEmpty() || nombre.equals("Ingrese su Nombre")) {
            errores.add("• El nombre es obligatorio");
        }
        
        if (apellidos.isEmpty() || apellidos.equals("Ingrese sus Apellidos ")) {
            errores.add("• Los apellidos son obligatorios");
        }
        
        if (email.isEmpty() || email.equals("Ingrese su Email ")) {
            errores.add("• El correo electrónico es obligatorio");
        }
        
        if (password.isEmpty() || password.equals("********")) {
            errores.add("• La contraseña es obligatoria");
        }
        
        // 2. Validar roles seleccionados
        EnumSet<enumRol> roles = obtenerRolesSeleccionados();
        if (roles.isEmpty()) {
            errores.add("• Debe seleccionar al menos un rol (Estudiante, Docente, Coordinador, Jefe Departamento )");
        }
        
        // 3. Validar programa si es estudiante
        if (roles.contains(enumRol.ESTUDIANTE)) {
            String programaSeleccionado = (String) ComBoxPrograma1.getSelectedItem();
            if (programaSeleccionado == null || programaSeleccionado.equals("Seleccione un programa")) {
                errores.add("• Debe seleccionar un programa académico (requerido para estudiantes)");
            }
        }
        
        // 4. Validar departamento si es docente o coordinador
        if (roles.contains(enumRol.DOCENTE) || roles.contains(enumRol.COORDINADOR)) {
            String deptSeleccionado = (String) ComBoxDepartamento.getSelectedItem();
            if (deptSeleccionado == null || deptSeleccionado.equals("Seleccione un departamento")) {
                errores.add("• Debe seleccionar un departamento (requerido para docentes/coordinadores/ Jefe Departamento)");
            }
        }
        
        // 5. Validar formato de correo si no está vacío
        if (!email.isEmpty() && !email.equals("Ingrese su Email ")) {
            if (!personaService.validateEmail(email)) {
                errores.add("• El correo debe ser institucional (ejemplo: usuario@unicauca.edu.co)");
            }
        }
        
        // 6. Validar fortaleza de contraseña si no está vacía
        if (!password.isEmpty() && !password.equals("********")) {
            if (!personaService.validatePassword(password)) {
                errores.add("• La contraseña debe tener mínimo 6 caracteres, al menos una mayúscula, un dígito y un carácter especial");
            }
        }
        
        // 7. Validar celular si se ingresó algo
        if (!celularStr.isEmpty() && !celularStr.equals("Ingrese su celular")) {
            if (!celularStr.matches("\\d+")) {
                errores.add("• El celular debe contener solo números");
            }
        }
        
        // 8. Validar si ya existe el correo
        if (!email.isEmpty() && !email.equals("Ingrese su Email ") && personaService.validateEmail(email)) {
            if (personaService.personExists(email)) {
                errores.add("• Ya existe una persona registrada con este correo electrónico");
            }
        }
        
        // Mostrar todos los errores si los hay
        if (!errores.isEmpty()) {
            StringBuilder mensajeError = new StringBuilder("Por favor corrige los siguientes errores:\n\n");
            for (String error : errores) {
                mensajeError.append(error).append("\n");
            }
            JOptionPane.showMessageDialog(this, mensajeError.toString(), "Errores de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Si llegamos aquí, todos los datos son válidos
        // Procesar celular opcional
        String celular = null;
        if (!celularStr.isEmpty() && !celularStr.equals("Ingrese su celular")) {
            celular = celularStr;
        }
        
        // PASO 1: Obtener nombres seleccionados por el usuario
        String nombrePrograma = null;
        String nombreDepartamento = null ;
        
        if (roles.contains(enumRol.ESTUDIANTE)) {
            nombrePrograma = (String) ComBoxPrograma1.getSelectedItem();
        }
        
        if (roles.contains(enumRol.DOCENTE) || roles.contains(enumRol.COORDINADOR)) {
            nombreDepartamento = (String) ComBoxDepartamento.getSelectedItem();
        }
        
        // PASO 2: PersonaService maneja todo el guardado internamente
        // En lblBttRegistrarMouseClicked, envuelve la llamada al PersonaService:
try {
    boolean registrado = personaService.savePerson(nombre, apellidos, celular, email, password, roles, nombrePrograma, nombreDepartamento);
    
    if (registrado) {
        JOptionPane.showMessageDialog(this, 
            "¡Registro exitoso!\n\nLa persona ha sido registrada correctamente en el sistema.", 
            "Registro Completado", 
            JOptionPane.INFORMATION_MESSAGE);
        irALogin();
    } else {
        JOptionPane.showMessageDialog(this, 
            "Error interno del sistema.\n\nNo se pudo completar el registro. Por favor intente nuevamente.", 
            "Error del Sistema", 
            JOptionPane.ERROR_MESSAGE);
    }
} catch (Exception e) {
    System.err.println("Excepción en registro GUI: " + e.getMessage());
    e.printStackTrace();
    JOptionPane.showMessageDialog(this, 
        "Error inesperado: " + e.getMessage(), 
        "Error del Sistema", 
        JOptionPane.ERROR_MESSAGE);
}
    }//GEN-LAST:event_lblBttRegistrarMouseClicked

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
     private EnumSet<enumRol> obtenerRolesSeleccionados() {
        EnumSet<enumRol> roles = EnumSet.noneOf(enumRol.class);
        
        if (CBEstudiante1.isSelected()) {
            roles.add(enumRol.ESTUDIANTE);
        }
        if (CBDocente1.isSelected()) {
            roles.add(enumRol.DOCENTE);
        }
        if (CBJefeDepartamento.isSelected()) {
            roles.add(enumRol.COORDINADOR);
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

    
/**
     * Obtener programa seleccionado
     */
    private Programa obtenerProgramaSeleccionado() {
        String programaSeleccionado = (String) ComBoxPrograma1.getSelectedItem();
        if (programaSeleccionado == null || programaSeleccionado.equals("Seleccione un programa")) {
            return null;
        }
        
        try {
            // Intentar buscar en la base de datos primero
            List<Programa> programas = programaRepository.list();
            for (Programa programa : programas) {
                if (programa.getNombrePrograma().equals(programaSeleccionado)) {
                    return programa;
                }
            }
            
            // Si no se encuentra en BD, crear un programa temporal con los datos básicos
            // Esto asume que tienes una Facultad y Departamento por defecto
            return crearProgramaTemporal(programaSeleccionado);
            
        } catch (Exception e) {
            System.err.println("Error buscando programa: " + e.getMessage());
            // Crear programa temporal como fallback
            return crearProgramaTemporal(programaSeleccionado);
        }
    }
    
    /**
     * Obtener departamento seleccionado
     */
    private Departamento obtenerDepartamentoSeleccionado() {
        String deptSeleccionado = (String) ComBoxDepartamento.getSelectedItem();
        if (deptSeleccionado == null || deptSeleccionado.equals("Seleccione un departamento")) {
            return null;
        }
        
        try {
            // Intentar buscar en la base de datos primero
            List<Departamento> departamentos = departamentoRepository.list();
            for (Departamento dept : departamentos) {
                if (dept.getNombre().equals(deptSeleccionado)) {
                    return dept;
                }
            }
            
            // Si no se encuentra en BD, crear departamento temporal
            return crearDepartamentoTemporal(deptSeleccionado);
            
        } catch (Exception e) {
            System.err.println("Error buscando departamento: " + e.getMessage());
            // Crear departamento temporal como fallback
            return crearDepartamentoTemporal(deptSeleccionado);
        }
    }
    
    /**
     * Crear programa temporal cuando no existe en BD
     */
    private Programa crearProgramaTemporal(String nombrePrograma) {
        try {
            // Crear facultad FIET por defecto
            Facultad facultad = new Facultad("Facultad de Ingeniería Electrónica y Telecomunicaciones");
            facultad.setCodFacultad(1); // ID temporal
            
            // Crear departamento de Sistemas por defecto
            Departamento departamento = new Departamento("Sistemas", facultad);
            departamento.setCodDepartamento(1); // ID temporal
            
            // Crear programa
            Programa programa = new Programa(nombrePrograma, departamento);
            programa.setCodPrograma(1); // ID temporal
            
            return programa;
            
        } catch (ValidationException e) {
            System.err.println("Error creando programa temporal: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Crear departamento temporal cuando no existe en BD
     */
    private Departamento crearDepartamentoTemporal(String nombreDepartamento) {
        try {
            // Crear facultad FIET por defecto
            Facultad facultad = new Facultad("Facultad de Ingeniería Electrónica y Telecomunicaciones");
            facultad.setCodFacultad(1); // ID temporal
            
            // Crear departamento
            Departamento departamento = new Departamento(nombreDepartamento, facultad);
            departamento.setCodDepartamento(1); // ID temporal
            
            return departamento;
            
        } catch (ValidationException e) {
            System.err.println("Error creando departamento temporal: " + e.getMessage());
            return null;
        }
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
   
public void irALogin(){
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
        }}


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
    private javax.swing.JCheckBox CBECoordinador2;
    private javax.swing.JCheckBox CBEstudiante1;
    private javax.swing.JCheckBox CBJefeDepartamento;
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
