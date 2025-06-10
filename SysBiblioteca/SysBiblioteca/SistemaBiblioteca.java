package SysBiblioteca;

import javax.swing.UIManager;

public class SistemaBiblioteca {
    public static void main(String[] args) {
        try {
            // Configurações de Look and Feel para melhorar a aparência
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Iniciar a interface gráfica
        new BibliotecaGUI();
    }
} 