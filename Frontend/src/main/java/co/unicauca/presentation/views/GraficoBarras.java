/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package co.unicauca.presentation.views;


import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.FormatoA;
import co.unicauca.infra.Observer;
import co.unicauca.service.EvaluacionService;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMTMaterialLighterIJTheme;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author User
 */
public class GraficoBarras extends javax.swing.JPanel implements Observer {

    private DefaultCategoryDataset dataset;
    private JFreeChart chart;
    private ChartPanel chartPanel;
    private EvaluacionService evaluacionService;

    public GraficoBarras() {
        FlatMTMaterialLighterIJTheme.setup();
        initComponents();
        initGrafico();
        cargarDatosIniciales();
    }

    private void initGrafico() {
        setLayout(new BorderLayout());
        dataset = new DefaultCategoryDataset();

        chart = ChartFactory.createBarChart(
                "Estados de Proyectos", // título
                "Estado",              // eje X
                "Cantidad",            // eje Y
                dataset);

        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(300, 300)); // tamaño 300x300
        add(chartPanel, BorderLayout.CENTER);
    }

    private void cargarDatosIniciales() {
        List<FormatoA> lista =  evaluacionService.listFormatoA();
        actualizarDataset(lista);
    }

    private void actualizarDataset(List<FormatoA> lista) {
        int entregados = 0;
        int aprobados = 0;
        int rechazados = 0;

        for (FormatoA f : lista) {
            EnumEstado estado = f.getState();
            if (estado == null) {
                entregados++;
                continue;
            }

            switch (estado) {
                case APROBADO:
                    aprobados++;
                    break;
                case RECHAZADO:
                    rechazados++;
                    break;
                case ENTREGADO:
                    entregados++;
                    break;
                // Ignoramos RECHAZADO_DEFINITIVAMENTE
                default:
                    entregados++;
                    break;
            }
        }

        dataset.setValue(entregados, "Proyectos", "Entregados");
        dataset.setValue(aprobados, "Proyectos", "Aprobados");
        dataset.setValue(rechazados, "Proyectos", "Rechazados");
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Object o) {
        if (o instanceof List<?>) {
            List<?> lista = (List<?>) o;
            if (!lista.isEmpty() && lista.get(0) instanceof FormatoA) {
                actualizarDataset((List<FormatoA>) lista);
            }
        } else {
            List<FormatoA> lista =  evaluacionService.listFormatoA();
            actualizarDataset(lista);
        }
    }
}
