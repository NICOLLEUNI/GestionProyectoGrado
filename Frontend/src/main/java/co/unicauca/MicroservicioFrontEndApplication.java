package co.unicauca;

import co.unicauca.presentation.GUILogin;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class MicroservicioFrontEndApplication {

    public static void main(String[] args) {
        // Configurar Spring Boot para que no inicie la interfaz web automáticamente
        SpringApplicationBuilder builder = new SpringApplicationBuilder(MicroservicioFrontEndApplication.class);
        builder.headless(false); // ✅ IMPORTANTE: Permitir componentes AWT/Swing
        ConfigurableApplicationContext context = builder.run(args);

        // Iniciar la interfaz Swing en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Establecer el look and feel de Nimbus
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ex) {
                System.err.println("Error estableciendo look and feel: " + ex.getMessage());
            }

            // Crear y mostrar la ventana de login
            GUILogin loginWindow = new GUILogin();
            loginWindow.setVisible(true);
        });
    }
}