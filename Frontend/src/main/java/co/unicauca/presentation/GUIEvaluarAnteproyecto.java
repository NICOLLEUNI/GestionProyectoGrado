/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package co.unicauca.workflow.presentation;

//agregar la funcionalidad en los 3 puntos de "volver a la pestaña anterior"
//añadir logica observer que se va a manejar con una capa de estadisticas 

import co.unicauca.workflow.access.Factory;
import co.unicauca.workflow.access.IFormatoARepository;
import co.unicauca.workflow.domain.entities.FormatoA;
import co.unicauca.workflow.domain.entities.Persona;
import co.unicauca.workflow.domain.entities.enumEstado;
import co.unicauca.workflow.domain.exceptions.ValidationException;
import co.unicauca.workflow.domain.service.FormatoAService;
import co.unicauca.workflow.presentation.views.GraficoBarras;
import co.unicauca.workflow.presentation.views.GraficoPastel;
import co.unicauca.workflow.presentation.views.Observaciones;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMTMaterialLighterIJTheme;
import java.awt.BorderLayout;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author User
 */
public class GUIEvaluarAnteproyecto extends javax.swing.JFrame {

    private FormatoAService formatoAService;
    private static Persona personaLogueado;
    private IFormatoARepository repoFormatoA = Factory.getInstance().getFormatoARepository("default");
    
    private JFrame frameBarras;
    private JFrame framePastel;
    
    //private List<FormatoA> listaFormateada = new ArrayList<>();
    
   public GUIEvaluarAnteproyecto(Persona logueado) throws ValidationException {
       this.formatoAService = new FormatoAService(repoFormatoA);
        this.personaLogueado=logueado;
        initComponents();
        initContent();
        cargarDatos();
        inicializarObservadores();
         }
   private void inicializarObservadores() {
    GraficoPastel graficoPastel = new GraficoPastel(formatoAService);
    GraficoBarras graficoBarras = new GraficoBarras(formatoAService);

    formatoAService.addObserver(graficoPastel);
    formatoAService.addObserver(graficoBarras);

    // Coordenadas de la ventana principal
    int xPrincipal = this.getX();
    int yPrincipal = this.getY();
    int anchoPrincipal = this.getWidth();

    // Frame Pastel
     framePastel = new JFrame("Gráfico Pastel");
    framePastel.getContentPane().add(graficoPastel);
    framePastel.pack();
    framePastel.setLocation(xPrincipal + anchoPrincipal + 10, yPrincipal); // a la derecha
    framePastel.setVisible(true);

    // Frame Barras (debajo o al lado)
     frameBarras = new JFrame("Gráfico Barras");
    frameBarras.getContentPane().add(graficoBarras);
    frameBarras.pack();
    frameBarras.setLocation(xPrincipal + anchoPrincipal + 10, 
                            yPrincipal + framePastel.getHeight() + 30);
    frameBarras.setVisible(true);
}
private void cargarDatos() {
    List<FormatoA> lista = formatoAService.listFormatoA();

    String[] columnas = {"ID", "Título", "Estado"};
    DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

    for (FormatoA f : lista) {
        // Si getState() devuelve enumEstado
        if (f.getState() == enumEstado.ENTREGADO) {
            Object[] fila = {
                f.getId(),
                f.getTitle(),
                f.getState().getDescripcion() // Mostramos la descripción legible
            };
            modelo.addRow(fila);
        }

    }

    jTable1.setModel(modelo);
}

    private void cerrarGraficas() {
    if (framePastel != null) {
        framePastel.dispose();
        framePastel = null;
    }
    if (frameBarras != null) {
        frameBarras.dispose();
        frameBarras = null;
    }
}

    private void initStyles(){ }
    
    private void showJPanel(JPanel pl){
     pl.setSize(533,456);
     pl.setLocation(0, 0);
     
     Contenido.removeAll();
     Contenido.add(pl,BorderLayout.CENTER);
     Contenido.revalidate();
     Contenido.repaint(); 
         
     }
    private void initContent(){

     jTable1.getSelectionModel().addListSelectionListener(e -> {
       if (!e.getValueIsAdjusting() && jTable1.getSelectedRow() != -1) {
        int fila = jTable1.getSelectedRow();
        int id = (int) jTable1.getValueAt(fila, 0); // ID está en la columna 0

        // Buscar el FormatoA desde repo
        FormatoA formato = formatoAService.findById(id); 

        if (formato != null) {
            Observaciones panelObs = new Observaciones(formatoAService);
            panelObs.setFormatoA(formato);
            showJPanel(panelObs);
        }}
         });
         }
     

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Menu = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btVolver = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        Contenido = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        Menu.setBackground(new java.awt.Color(26, 55, 171));
        Menu.setPreferredSize(new java.awt.Dimension(226, 510));
        Menu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Roboto Medium", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("PROYECTOS");
        Menu.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(73, 14, 206, 28));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Titulo", "Estado"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        Menu.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 54, 353, 450));

        btVolver.setBackground(new java.awt.Color(102, 102, 255));
        btVolver.setFont(new java.awt.Font("Wide Latin", 1, 24)); // NOI18N
        btVolver.setForeground(new java.awt.Color(255, 255, 255));
        btVolver.setText("<");
        btVolver.setFocusable(false);
        btVolver.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btVolver.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btVolverMouseClicked(evt);
            }
        });
        Menu.add(btVolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 50, -1));

        jLabel1.setFont(new java.awt.Font("Roboto Medium", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("EVALUAR ANTEPROYECTO");

        javax.swing.GroupLayout ContenidoLayout = new javax.swing.GroupLayout(Contenido);
        Contenido.setLayout(ContenidoLayout);
        ContenidoLayout.setHorizontalGroup(
            ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 533, Short.MAX_VALUE)
        );
        ContenidoLayout.setVerticalGroup(
            ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 456, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(Menu, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(109, 109, 109)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Contenido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Menu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(Contenido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btVolverMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btVolverMouseClicked
        cerrarGraficas();
        GUIMenuCoord ventanaCoord = new GUIMenuCoord(personaLogueado); // Opcional: mostrar un mensaje al usuario
        ventanaCoord.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btVolverMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        FlatMTMaterialLighterIJTheme.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new GUIEvaluarAnteproyecto(personaLogueado).setVisible(true);
                } catch (ValidationException ex) {
                    Logger.getLogger(GUIEvaluarAnteproyecto.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Contenido;
    private javax.swing.JPanel Menu;
    private javax.swing.JButton btVolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

   
}
