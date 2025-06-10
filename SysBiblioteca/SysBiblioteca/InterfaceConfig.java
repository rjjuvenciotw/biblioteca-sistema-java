package SysBiblioteca;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.net.URL;

/**
 * Classe para gerenciar configurações de interface e estilização avançada
 * para o Sistema de Biblioteca
 */
public class InterfaceConfig {
    
    // Constantes para configurações de UI
    private static final Color COR_BARRA_TITULO = new Color(20, 20, 20); // Quase preto para barra de título
    private static final int SOMBRA_TAMANHO = 12; // Tamanho da sombra em pixels
    private static final Color COR_SOMBRA = new Color(0, 0, 0, 160); // Preto com transparência
    
    /**
     * Aplica o ícone ao JFrame da aplicação
     * @param frame O JFrame que terá o ícone aplicado
     */
    public static void aplicarIconeAplicacao(JFrame frame) {
        try {
            // Caminho simples para o ícone PNG
            ImageIcon icone = new ImageIcon("icon.png");
            frame.setIconImage(icone.getImage());
            System.out.println(">>> Ícone aplicado com sucesso");
        } catch (Exception e) {
            System.err.println("Erro ao aplicar ícone: " + e.getMessage());
        }
    }
    
    /**
     * Aplica estilo à barra de título (Windows) - tenta deixar preta
     * Obs: Isso funciona apenas para alguns sistemas operacionais/ambientes
     * @param frame O JFrame que terá o estilo aplicado
     */
    public static void aplicarEstiloBarraTitulo(JFrame frame) {
        // No Windows, tentamos usar a API de UXTheme do Windows 10+
        try {
            // Verificar se estamos no Windows
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("win")) {
                // Método para Windows 10+
                frame.getRootPane().putClientProperty("JRootPane.titleBarBackground", COR_BARRA_TITULO);
                frame.getRootPane().putClientProperty("JRootPane.titleBarForeground", Color.WHITE);
                
                // Método alternativo FlatLaf (biblioteca comum para moderna UI em Java)
                try {
                    UIManager.put("TitlePane.background", COR_BARRA_TITULO);
                    UIManager.put("TitlePane.foreground", Color.WHITE);
                    UIManager.put("TitlePane.buttonHoverBackground", new Color(60, 60, 60));
                    UIManager.put("TitlePane.closeHoverBackground", new Color(232, 17, 35));
                } catch (Exception ignored) {}
                
                // Tentar outro método proprietário se disponível em versões específicas
                try {
                    Class<?> awtUtilitiesClass = Class.forName("com.sun.awt.AWTUtilities");
                    if (awtUtilitiesClass != null) {
                        System.out.println(">>> Suporte a AWTUtilities detectado");
                    }
                } catch (ClassNotFoundException ignored) {
                    // Classe não disponível nesta JVM
                }
            }
            
            System.out.println(">>> Estilo de barra de título aplicado");
        } catch (Exception e) {
            System.err.println("Não foi possível personalizar a barra de título: " + e.getMessage());
        }
    }
    
    /**
     * Aplica sombra à borda da janela
     * @param frame O JFrame que terá a sombra aplicada
     */
    public static void aplicarSombraJanela(JFrame frame) {
        try {
            // Adicionando sombra via decoração de janela personalizada
            JPanel contentPane = (JPanel) frame.getContentPane();
            contentPane.setBorder(criarBordaComSombra());
            
            // Tentativa de abordagem alternativa para sombras
            frame.getRootPane().putClientProperty("Window.shadow", Boolean.TRUE);
            frame.getRootPane().putClientProperty("Window.shadowWidth", SOMBRA_TAMANHO);
            
            // Opção mais simples de sombra: borda composta escura
            contentPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0, 160), 3),
                contentPane.getBorder()
            ));
            
            // Usar painel com sombra personalizada - comentado pois pode causar problemas em alguns sistemas
            // aplicarPainelComSombra(frame);
            
            System.out.println(">>> Sombra aplicada à janela");
        } catch (Exception e) {
            System.err.println("Erro ao aplicar sombra: " + e.getMessage());
        }
    }
    
    /**
     * Cria uma borda com efeito de sombra
     * @return Uma borda com sombra
     */
    private static Border criarBordaComSombra() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_SOMBRA, 1),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0, 100), 2),
                BorderFactory.createEmptyBorder(SOMBRA_TAMANHO/2, SOMBRA_TAMANHO/2, SOMBRA_TAMANHO/2, SOMBRA_TAMANHO/2)
            )
        );
    }
    
    /**
     * Aplica um painel com sombra personalizada
     * Este método usa técnicas avançadas de pintura para criar um efeito de sombra
     * mais proeminente e realista
     * @param frame O JFrame que receberá o painel com sombra
     */
    private static void aplicarPainelComSombra(JFrame frame) {
        // Criar um painel personalizado que desenha uma sombra
        JPanel painelComSombra = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                if (g instanceof Graphics2D) {
                    Graphics2D g2d = (Graphics2D) g;
                    
                    // Salvar as configurações originais
                    Paint paintOriginal = g2d.getPaint();
                    Composite compositeOriginal = g2d.getComposite();
                    
                    // Desenhar a sombra usando gradiente radial
                    int largura = getWidth();
                    int altura = getHeight();
                    
                    // Configurar o gradiente de sombra (do centro para as bordas)
                    RadialGradientPaint gradiente = new RadialGradientPaint(
                        largura / 2, altura / 2, 
                        Math.max(largura, altura) / 2, 
                        new float[] { 0.0f,.9f },
                        new Color[] { 
                            new Color(0, 0, 0, 0),  // Centro transparente
                            new Color(0, 0, 0, 80)   // Bordas com sombra
                        }
                    );
                    
                    // Aplicar o gradiente
                    g2d.setPaint(gradiente);
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                    
                    // Desenhar a sombra nas bordas
                    g2d.fillRect(0, 0, largura, SOMBRA_TAMANHO*2); // Superior
                    g2d.fillRect(0, altura - SOMBRA_TAMANHO*2, largura, SOMBRA_TAMANHO*2); // Inferior
                    g2d.fillRect(0, 0, SOMBRA_TAMANHO*2, altura); // Esquerda
                    g2d.fillRect(largura - SOMBRA_TAMANHO*2, 0, SOMBRA_TAMANHO*2, altura); // Direita
                    
                    // Restaurar as configurações originais
                    g2d.setPaint(paintOriginal);
                    g2d.setComposite(compositeOriginal);
                }
            }
        };
        
        // Configurar o painel para ser transparente (para mostrar apenas a sombra)
        painelComSombra.setOpaque(false);
        
        // Substituir o contentPane existente
        Container contentPaneOriginal = frame.getContentPane();
        frame.setContentPane(painelComSombra);
        
        // Adicionar o contentPane original de volta como filho
        painelComSombra.setLayout(new BorderLayout());
        painelComSombra.add(contentPaneOriginal, BorderLayout.CENTER);
        
        // Garantir que o painel com sombra ocupe toda a janela
        frame.pack();
        frame.setSize(frame.getWidth() + SOMBRA_TAMANHO*2, frame.getHeight() + SOMBRA_TAMANHO*2);
    }
    
    /**
     * Aplica todas as configurações de interface ao JFrame
     * @param frame O JFrame que terá todas as configurações aplicadas
     */
    public static void aplicarConfiguracoes(JFrame frame) {
        // Define o visual como moderno quando disponível (antes de tudo)
        try {
            // Tentar o Look and Feel do sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.out.println(">>> Look and Feel do sistema aplicado");
        } catch (Exception ignored) {
            // Se falhar, tenta o look and feel tradicional do Java
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                System.out.println(">>> Look and Feel padrão aplicado");
            } catch (Exception e) {
                System.err.println("Erro ao definir Look and Feel: " + e.getMessage());
            }
        }
        
        // Opção para tornar a barra de título do Windows 10 escura
        JRootPane rootPane = frame.getRootPane();
        rootPane.putClientProperty("JRootPane.titleBarBackground", Color.BLACK);
        rootPane.putClientProperty("JRootPane.titleBarForeground", Color.WHITE);
        
        aplicarIconeAplicacao(frame);
        aplicarEstiloBarraTitulo(frame);
        aplicarSombraJanela(frame);
        
        // Atualiza a renderização do frame para aplicar as mudanças
        SwingUtilities.updateComponentTreeUI(frame);
    }
} 