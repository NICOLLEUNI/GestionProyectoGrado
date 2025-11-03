/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package co.unicauca.presentation.views;


import co.unicauca.entity.FormatoA;
import co.unicauca.entity.Persona;

import java.awt.BorderLayout;
import java.util.List;
import java.util.ArrayList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
/**
 *
 * @author Usuario
 */
public class ListaFormatosA extends javax.swing.JPanel {

    private Persona personaLogueada;
    private List<FormatoA> formatosDocente; // lista en memoria para mapear filas con objetos

        
    public ListaFormatosA(Persona personaLogueada) {
        initComponents();
        this.personaLogueada = personaLogueada;
        initStyles();
        //cargarDatos();
        initListeners();
    } 
    
    
    /**
     * Carga solo los FormatoA subidos por el docente logueado.
     */
    /*
    private void cargarDatos() {


        // Traemos todos los formatos desde la BD
        List<FormatoA> todos = repo.list();
        this.formatosDocente = new ArrayList<>();

        // Filtramos solo los que pertenecen al docente logueado
        for (FormatoA f : todos) {
            if (f.getProjectManager() != null &&
                f.getProjectManager().getIdUsuario() == personaLogueada.getIdUsuario()) {
                formatosDocente.add(f);
            }
        }

        // Definimos columnas
        String[] columnas = {"Título", "Modalidad", "Estado actual", "Observaciones", "Versión", "Contador"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacemos la tabla de solo lectura
            }
        };

        // Rellenamos filas - SOLO DATOS DEL FORMATOA PRINCIPAL
        for (FormatoA f : formatosDocente) {
            // DEBUG
            System.out.println("DEBUG - Formato: " + f.getTitle() +
                              ", Estado: " + f.getState() +
                              ", Observaciones: " + f.getObservations() +
                              ", Counter: " + f.getCounter());

            // Calcular número de versión para mostrar (última versión + 1)
            int numeroVersion = 1; // Por defecto
            if (f.getVersiones() != null && !f.getVersiones().isEmpty()) {
                FormatoAVersion ultima = f.getVersiones().get(f.getVersiones().size() - 1);
                numeroVersion = ultima.getNumeroVersion();
            }

            Object[] fila = {
                f.getTitle() != null ? f.getTitle() : "",           // ✅ Del FormatoA principal
                f.getMode() != null ? f.getMode().name() : "N/A",   // ✅ Del FormatoA principal
                f.getState() != null ? f.getState().name() : "N/A", // ✅ Estado actual del principal
                f.getObservations() != null ? f.getObservations() : "", // ✅ Observaciones del coordinador
                numeroVersion,                                      // ✅ Número de la última versión
                f.getCounter()                                      // ✅ Contador de reenvíos
            };
            modelo.addRow(fila);
        }

        jTable1.setModel(modelo);

        // Ajustar el ancho de las columnas
        if (modelo.getRowCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(150);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(200);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(60);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(60);
        }
    }*/
    /**
     * Inicializa listeners para acciones en la tabla.
     * Ahora se abre DetallesFormatoA cuando se selecciona una fila (clic simple).
     */
    private void initListeners() {
        // aseguramos selección simple y single selection
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                // ignorar eventos intermedios
                if (evt.getValueIsAdjusting()) {
                    return;
                }
                int fila = jTable1.getSelectedRow();
                if (fila >= 0 && formatosDocente != null && fila < formatosDocente.size()) {
                    FormatoA seleccionado = formatosDocente.get(fila);
                    abrirDetalleConSeguridad(seleccionado);
                }
            }
        });
    }
    
    
     /**
     * Abre el panel DetallesFormatoA asegurando que tengamos un objeto Docente válido.
     * Si personaLogueada no es Docente, intentamos obtenerlo del repositorio.
     */
  private void abrirDetalleConSeguridad(FormatoA seleccionado) {
/*    try {
        // 🔹 Primero revisamos el contador del formato
        if (seleccionado.getCounter() >= 3) { // o el número que quieras
            int opcion = JOptionPane.showConfirmDialog(
                this,
                "Este formato ya se ha reenviado 3 veces.\n" +
                "¿Desea eliminarlo junto con sus estudiantes?",
                "Aviso",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (opcion == JOptionPane.YES_OPTION) {
                // 🔹 Aquí llamamos al Service para eliminar el formato y sus estudiantes
               
                boolean eliminado = serviceFormato.eliminarFormatoAConVersiones(seleccionado.getId());
                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "Formato eliminado correctamente.");
                    cargarDatos(); // 🔹 refrescamos la tabla
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el formato.");
                }
                return; // 🔹 salir sin abrir detalle
            } else {
                // El usuario dijo que no, entonces no abrimos el detalle
                return;
            }
        }

        // 🔹 Si el contador <3, seguimos flujo normal
        Docente docente = null;

        if (personaLogueada instanceof Docente) {
            docente = (Docente) personaLogueada;
        } else {
            // Intentamos recuperar el docente por id
            IDocenteRepository repoDoc = Factory.getInstance().getDocenteRepository("default");
            docente = repoDoc.findById(personaLogueada.getIdUsuario());
        }

        if (docente == null) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo determinar el docente logueado. Asegúrese de estar con un usuario con rol DOCENTE.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Abrimos la vista de detalle
        DetallesFormatoA detalle = new DetallesFormatoA(seleccionado, docente, personaLogueada);

        // Cambiamos la vista dentro de este panel
        Contenido.removeAll();
        Contenido.setLayout(new BorderLayout());
        Contenido.add(detalle, BorderLayout.CENTER);
        Contenido.revalidate();
        Contenido.repaint();

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error abriendo detalle: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }*/
}
    
    private void initStyles() {
        jTable1.setRowHeight(30);
        jTable1.setShowGrid(false);
        jTable1.setIntercellSpacing(new java.awt.Dimension(0, 0));
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.getTableHeader().setResizingAllowed(false);
        jTable1.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        jTable1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        jTable1.setSelectionBackground(new java.awt.Color(33, 150, 243));
        jTable1.setSelectionForeground(java.awt.Color.WHITE);
        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder());
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        Contenido.setBackground(new java.awt.Color(255, 255, 255));
        Contenido.setPreferredSize(new java.awt.Dimension(646, 530));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Titulo ", "Modalidad", "Estado actual", "Comentarios", "Version"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout ContenidoLayout = new javax.swing.GroupLayout(Contenido);
        Contenido.setLayout(ContenidoLayout);
        ContenidoLayout.setHorizontalGroup(
            ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ContenidoLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 606, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        ContenidoLayout.setVerticalGroup(
            ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ContenidoLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(281, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Contenido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Contenido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Contenido;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
