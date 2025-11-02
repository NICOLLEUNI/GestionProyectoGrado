/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package co.unicauca.workflow.presentation;


import co.unicauca.workflow.presentation.views.Principal;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import co.unicauca.workflow.domain.entities.Persona;
import co.unicauca.workflow.domain.exceptions.ValidationException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class GUIMenuCoord extends javax.swing.JFrame {

    /**
     * Creates new form GUIMenuCoord
     */
    private static Persona personaLogueado;
     
    public GUIMenuCoord(Persona logueado) {
        
     this.personaLogueado=logueado;
     
     initComponents();
     initStyles();
     initContent();
    }

     private void initStyles(){
     
     }

     private void showJPanel(JPanel pl){

     pl.setSize(641,498);
     pl.setLocation(0, 0);
     
     Contenido.removeAll();
     Contenido.add(pl,BorderLayout.CENTER);
     Contenido.revalidate();

     Contenido.repaint(); 
         
     }
     private void initContent(){
     showJPanel( new Principal(personaLogueado));
     }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Background = new javax.swing.JPanel();
        Menu = new javax.swing.JPanel();
        Icon = new javax.swing.JLabel();
        Titulo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btEvaluarFormatoA = new javax.swing.JButton();
        btRegresar = new javax.swing.JButton();
        jButtonCloseSesion = new javax.swing.JButton();
        Contenido = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);

        Background.setBackground(new java.awt.Color(26, 55, 171));

        Menu.setBackground(new java.awt.Color(26, 55, 171));
        Menu.setPreferredSize(new java.awt.Dimension(267, 510));

        Icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/co/unicauca/presentation/images/LogoPequeño.png"))); // NOI18N
        Icon.setText("jLabel1");

        Titulo.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Roboto ExtraBold", 0, 28)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(26, 55, 171));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("COORDINADOR");

        javax.swing.GroupLayout TituloLayout = new javax.swing.GroupLayout(Titulo);
        Titulo.setLayout(TituloLayout);
        TituloLayout.setHorizontalGroup(
            TituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        TituloLayout.setVerticalGroup(
            TituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TituloLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btEvaluarFormatoA.setBackground(new java.awt.Color(65, 55, 171));
        btEvaluarFormatoA.setFont(new java.awt.Font("Roboto Medium", 0, 24)); // NOI18N
        btEvaluarFormatoA.setForeground(new java.awt.Color(255, 255, 255));
        btEvaluarFormatoA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/co/unicauca/presentation/images/file-chart.png"))); // NOI18N
        btEvaluarFormatoA.setText("Evaluar FormatoA");
        btEvaluarFormatoA.setToolTipText("");
        btEvaluarFormatoA.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        btEvaluarFormatoA.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btEvaluarFormatoA.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btEvaluarFormatoA.setIconTextGap(7);
        btEvaluarFormatoA.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btEvaluarFormatoAMouseClicked(evt);
            }
        });

        btRegresar.setBackground(new java.awt.Color(65, 55, 171));
        btRegresar.setFont(new java.awt.Font("Roboto Medium", 0, 24)); // NOI18N
        btRegresar.setForeground(new java.awt.Color(255, 255, 255));
        btRegresar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/co/unicauca/presentation/images/file-chart.png"))); // NOI18N
        btRegresar.setText("Volver");
        btRegresar.setToolTipText("");
        btRegresar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        btRegresar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btRegresar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btRegresar.setIconTextGap(7);
        btRegresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btRegresarMouseClicked(evt);
            }
        });

        jButtonCloseSesion.setBackground(new java.awt.Color(65, 55, 171));
        jButtonCloseSesion.setForeground(new java.awt.Color(255, 255, 255));
        jButtonCloseSesion.setText("Cerrar Sesión");
        jButtonCloseSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseSesionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MenuLayout = new javax.swing.GroupLayout(Menu);
        Menu.setLayout(MenuLayout);
        MenuLayout.setHorizontalGroup(
            MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Titulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(MenuLayout.createSequentialGroup()
                .addGroup(MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MenuLayout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(Icon, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btEvaluarFormatoA, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(MenuLayout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addComponent(jButtonCloseSesion)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        MenuLayout.setVerticalGroup(
            MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(Icon)
                .addGap(45, 45, 45)
                .addComponent(Titulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(btEvaluarFormatoA, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                .addComponent(jButtonCloseSesion)
                .addGap(41, 41, 41))
        );

        Contenido.setBackground(new java.awt.Color(255, 255, 255));
        Contenido.setPreferredSize(new java.awt.Dimension(641, 498));

        javax.swing.GroupLayout ContenidoLayout = new javax.swing.GroupLayout(Contenido);
        Contenido.setLayout(ContenidoLayout);
        ContenidoLayout.setHorizontalGroup(
            ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 645, Short.MAX_VALUE)
        );
        ContenidoLayout.setVerticalGroup(
            ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout BackgroundLayout = new javax.swing.GroupLayout(Background);
        Background.setLayout(BackgroundLayout);
        BackgroundLayout.setHorizontalGroup(
            BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BackgroundLayout.createSequentialGroup()
                .addComponent(Menu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(Contenido, javax.swing.GroupLayout.DEFAULT_SIZE, 645, Short.MAX_VALUE)
                .addContainerGap())
        );
        BackgroundLayout.setVerticalGroup(
            BackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Menu, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE)
            .addGroup(BackgroundLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Contenido, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btEvaluarFormatoAMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btEvaluarFormatoAMouseClicked
     GUIEvaluarAnteproyecto ventanaEvaluar = null;
         try {
             ventanaEvaluar = new GUIEvaluarAnteproyecto(personaLogueado); 
         } catch (ValidationException ex) {
             Logger.getLogger(GUIMenuCoord.class.getName()).log(Level.SEVERE, null, ex);
         }
        ventanaEvaluar.setVisible(true);        
        this.dispose(); 
    }//GEN-LAST:event_btEvaluarFormatoAMouseClicked

    private void btRegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btRegresarMouseClicked
        GUIMenuPrincipal ventanaPrincipal = new GUIMenuPrincipal(personaLogueado);
        ventanaPrincipal.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btRegresarMouseClicked

    private void jButtonCloseSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseSesionActionPerformed
        GUILogin login = new GUILogin();
        login.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButtonCloseSesionActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        FlatLightLaf.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUIMenuCoord(personaLogueado).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Background;
    private javax.swing.JPanel Contenido;
    private javax.swing.JLabel Icon;
    private javax.swing.JPanel Menu;
    private javax.swing.JPanel Titulo;
    private javax.swing.JButton btEvaluarFormatoA;
    private javax.swing.JButton btRegresar;
    private javax.swing.JButton jButtonCloseSesion;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
