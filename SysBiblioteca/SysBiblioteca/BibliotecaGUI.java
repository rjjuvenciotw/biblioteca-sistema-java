package SysBiblioteca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
// A classe Serializable não é usada diretamente aqui, mas suas dependências (Livro, Usuario, etc.) a usam.
// As classes de IO diretas também não são usadas aqui, mas sim em BibliotecaDB.

// Classe para a interface gráfica
class BibliotecaGUI extends JFrame {
    private GerenciadorBiblioteca gerenciador;
    private JTabbedPane abas;

    // Componentes das abas que precisam ser acessados por múltiplos métodos
    private JList<String> listaLivrosDisplay; // Usará String para display
    private JList<String> listaUsuariosDisplay; // Usará String para display
    private JComboBox<Livro> cmbLivrosParaEmprestimo;
    private JComboBox<Usuario> cmbUsuariosParaEmprestimo;
    private JComboBox<Emprestimo> cmbEmprestimosAtivosParaDevolucao;
    private DefaultTableModel modeloTabelaEmprestimos;
    private DefaultTableModel modeloTabelaConsulta;


    // --- CORES E FONTES ESTILO RETRO/INDUSTRIAL ---
    
    // CORES DE FUNDO
    private static final Color COR_FUNDO_JANELA = new Color(230, 230, 230); // Cinza muito claro para fundo
    private static final Color COR_FUNDO_PAINEL = new Color(220, 220, 220); // Cinza claro para painéis
    private static final Color COR_FUNDO_COMPONENTE = new Color(240, 240, 240); // Quase branco para campos

    // CORES DE TEXTO
    private static final Color COR_TEXTO_PRINCIPAL = new Color(10, 10, 10); // Quase preto
    private static final Color COR_TEXTO_SECUNDARIO = new Color(40, 40, 40); // Cinza escuro

    // CORES DE BORDAS
    private static final Color COR_BORDA_GERAL = new Color(100, 100, 100); // Cinza médio para bordas
    private static final Color COR_BORDA_DESTAQUE = new Color(50, 50, 50); // Cinza mais escuro para destaque

    // CORES DE SELEÇÃO
    private static final Color COR_SELECAO_LISTA = new Color(190, 190, 190); // Cinza médio para seleção
    private static final Color COR_TEXTO_SELECAO_LISTA = new Color(0, 0, 0); // Preto

    // CORES DE BOTÕES
    private static final Color COR_BOTAO = new Color(200, 200, 200); // Cinza claro para botões
    private static final Color COR_BOTAO_HOVER = new Color(180, 180, 180); // Cinza um pouco mais escuro para hover

    // CORES DE ABAS
    private static final Color COR_ABA_SELECIONADA = new Color(230, 230, 230); // Cinza muito claro para aba selecionada
    private static final Color COR_ABA_NAO_SELECIONADA_TXT = new Color(60, 60, 60); // Cinza escuro para texto de abas não selecionadas

    // FONTES - Usando fontes monoespaçadas para parecer mais técnico/industrial
    private static final Font FONTE_MONO_TEXTO = new Font("Courier New", Font.PLAIN, 12);
    private static final Font FONTE_MONO_TITULO = new Font("Courier New", Font.BOLD, 13);
    private static final Font FONTE_MONO_LABEL = new Font("Courier New", Font.PLAIN, 12);
    private static final Font FONTE_MONO_BOTAO = new Font("Courier New", Font.BOLD, 12);

    public BibliotecaGUI() {
        gerenciador = new GerenciadorBiblioteca();

        configurarEstiloGlobal();

        setTitle("SISTEMA DE BIBLIOTECA");
        setSize(950, 750);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COR_FUNDO_JANELA);
        
        // Aplicar configurações avançadas de interface
        InterfaceConfig.aplicarConfiguracoes(this);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                salvarDadosAoFechar();
            }
        });

        abas = new JTabbedPane();
        estilizarAbas(abas);

        abas.addTab("GERENCIAR LIVROS", criarPainelLivros());
        abas.addTab("GERENCIAR USUARIOS", criarPainelUsuarios());
        abas.addTab("EMPRESTIMOS", criarPainelEmprestimos());
        abas.addTab("CONSULTA GERAL", criarPainelConsulta());
        abas.addTab("SISTEMA", criarPainelSistema());

        add(abas);

        // Inicializar listas e combos com dados
        atualizarListaDisplayLivros();
        atualizarListaDisplayUsuarios();
        atualizarTodosComboBoxEmprestimo();
        atualizarTabelaEmprestimos();

        setVisible(true);
    }

    private void configurarEstiloGlobal() {
        try {
            // Look and Feel padrão do sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // ------- CONFIGURAÇÕES BÁSICAS DE INTERFACE -------
        
        // Configurações do painel e diálogos
        UIManager.put("Panel.background", COR_FUNDO_PAINEL);
        UIManager.put("OptionPane.background", COR_FUNDO_PAINEL);
        UIManager.put("OptionPane.messageForeground", COR_TEXTO_PRINCIPAL);
        UIManager.put("OptionPane.messageFont", FONTE_MONO_TEXTO);
        UIManager.put("OptionPane.buttonFont", FONTE_MONO_BOTAO);
        UIManager.put("OptionPane.buttonBackground", COR_BOTAO);
        UIManager.put("OptionPane.buttonForeground", COR_TEXTO_PRINCIPAL);
        
        // ------- CONFIGURAÇÕES DE BOTÕES -------
        UIManager.put("Button.background", COR_BOTAO);
        UIManager.put("Button.foreground", COR_TEXTO_PRINCIPAL);
        UIManager.put("Button.font", FONTE_MONO_BOTAO);
        UIManager.put("Button.focus", new Color(0,0,0,0)); // Remove a pintura de foco padrão
        
        // ------- CONFIGURAÇÕES DE CAMPOS DE SELEÇÃO -------
        
        // Radio Buttons e Checkboxes
        UIManager.put("RadioButton.background", COR_FUNDO_PAINEL);
        UIManager.put("RadioButton.foreground", COR_TEXTO_PRINCIPAL);
        UIManager.put("CheckBox.background", COR_FUNDO_PAINEL);
        UIManager.put("CheckBox.foreground", COR_TEXTO_PRINCIPAL);
        
        // ComboBox
        UIManager.put("ComboBox.background", COR_FUNDO_COMPONENTE);
        UIManager.put("ComboBox.foreground", COR_TEXTO_PRINCIPAL);
        UIManager.put("ComboBox.selectionBackground", COR_SELECAO_LISTA);
        UIManager.put("ComboBox.selectionForeground", COR_TEXTO_SELECAO_LISTA);

        // ------- CONFIGURAÇÕES DE TEXTOS E LABELS -------
        
        // Labels
        UIManager.put("Label.foreground", COR_TEXTO_PRINCIPAL);
        
        // TextFields
        UIManager.put("TextField.background", COR_FUNDO_COMPONENTE);
        UIManager.put("TextField.foreground", COR_TEXTO_PRINCIPAL);
        UIManager.put("TextField.caretForeground", COR_TEXTO_PRINCIPAL);
        
        // ------- CONFIGURAÇÕES DE ROLAGEM E SCROLL -------
        
        // ScrollBar 
        UIManager.put("ScrollBar.background", COR_FUNDO_COMPONENTE);
        UIManager.put("ScrollBar.foreground", COR_BORDA_GERAL);
        UIManager.put("ScrollBar.thumb", COR_BORDA_DESTAQUE);
        UIManager.put("ScrollBar.track", COR_FUNDO_COMPONENTE);
        UIManager.put("ScrollBar.width", 10);

        // ------- CONFIGURAÇÕES DE ABAS E PAINÉIS -------
        
        // JTabbedPane
        UIManager.put("TabbedPane.font", FONTE_MONO_TITULO);
        UIManager.put("TabbedPane.foreground", COR_ABA_NAO_SELECIONADA_TXT);
        UIManager.put("TabbedPane.selectedForeground", COR_TEXTO_PRINCIPAL);
        UIManager.put("TabbedPane.background", COR_FUNDO_PAINEL);
        UIManager.put("TabbedPane.contentAreaColor", COR_FUNDO_PAINEL);
        UIManager.put("TabbedPane.selected", COR_ABA_SELECIONADA);
        UIManager.put("TabbedPane.tabInsets", new Insets(4, 10, 4, 10));
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(2, 2, 2, 2));
        
        // ------- CONFIGURAÇÕES DE LISTAS E TABELAS -------
        
        // Lista
        UIManager.put("List.background", COR_FUNDO_COMPONENTE);
        UIManager.put("List.foreground", COR_TEXTO_PRINCIPAL);
        UIManager.put("List.selectionBackground", COR_SELECAO_LISTA);
        UIManager.put("List.selectionForeground", COR_TEXTO_SELECAO_LISTA);
        
        // Tabela
        UIManager.put("Table.background", COR_FUNDO_COMPONENTE);
        UIManager.put("Table.foreground", COR_TEXTO_PRINCIPAL);
        UIManager.put("Table.selectionBackground", COR_SELECAO_LISTA);
        UIManager.put("Table.selectionForeground", COR_TEXTO_SELECAO_LISTA);
        UIManager.put("Table.gridColor", COR_BORDA_GERAL);
        
        // Cabeçalho da tabela
        UIManager.put("TableHeader.background", COR_BOTAO);
        UIManager.put("TableHeader.foreground", COR_TEXTO_PRINCIPAL);
        UIManager.put("TableHeader.font", FONTE_MONO_TITULO);
    }

    private void estilizarAbas(JTabbedPane tabPane) {
        // As configurações do UIManager já devem cuidar da maior parte.
        // Este método pode ser usado para ajustes finos se necessário.
        tabPane.setOpaque(true); // Garante que o fundo seja pintado
        tabPane.setBackground(COR_FUNDO_PAINEL); // Cor de fundo geral da área das abas
    }

    // --- HELPER METHODS PARA CRIAR COMPONENTES ESTILIZADOS ---
    private JButton criarBotaoEstilizado(String texto) {
        JButton botao = new JButton(texto.toUpperCase()); // Texto em maiúsculas para destaque
        botao.setFont(FONTE_MONO_BOTAO);
        botao.setBackground(COR_BOTAO);
        botao.setForeground(COR_TEXTO_PRINCIPAL);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_BORDA_DESTAQUE, 2), // Borda externa mais grossa
            BorderFactory.createEmptyBorder(6, 10, 6, 10) // Padding interno
        ));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        botao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botao.setBackground(COR_BOTAO_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                botao.setBackground(COR_BOTAO);
            }
        });
        return botao;
    }

    private JTextField criarTextFieldEstilizado(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(FONTE_MONO_TEXTO);
        textField.setBackground(COR_FUNDO_COMPONENTE);
        textField.setForeground(COR_TEXTO_PRINCIPAL);
        textField.setCaretColor(COR_TEXTO_PRINCIPAL);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_BORDA_GERAL, 1),
            BorderFactory.createEmptyBorder(4, 6, 4, 6) // Padding maior
        ));
        return textField;
    }

    private <T> JComboBox<T> criarComboBoxEstilizado() {
        JComboBox<T> comboBox = new JComboBox<>();
        comboBox.setFont(FONTE_MONO_TEXTO);
        comboBox.setBackground(COR_FUNDO_COMPONENTE);
        comboBox.setForeground(COR_TEXTO_PRINCIPAL);
        comboBox.setBorder(BorderFactory.createLineBorder(COR_BORDA_GERAL, 1));
        
        // Personalizar a seta do ComboBox (um pouco mais avançado)
        // UIManager.put("ComboBox.buttonBackground", COR_BOTAO);
        // UIManager.put("ComboBox.buttonDarkShadow", COR_BORDA_GERAL.darker());
        // UIManager.put("ComboBox.buttonHighlight", COR_BOTAO_HOVER);
        // UIManager.put("ComboBox.buttonShadow", COR_BORDA_GERAL);
        // comboBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI()); // Para aplicar as mudanças acima
        
        // Personalizar o renderer para itens da lista suspensa
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? COR_SELECAO_LISTA : COR_FUNDO_COMPONENTE);
                setForeground(isSelected ? COR_TEXTO_SELECAO_LISTA : COR_TEXTO_PRINCIPAL);
                setFont(FONTE_MONO_TEXTO);
                setBorder(BorderFactory.createEmptyBorder(3,5,3,5));
                return this;
            }
        });
        return comboBox;
    }

    private JLabel criarLabelEstilizado(String texto) {
        JLabel label = new JLabel(texto.toUpperCase());
        label.setFont(FONTE_MONO_LABEL);
        label.setForeground(COR_TEXTO_PRINCIPAL);
        return label;
    }

    private JPanel criarPainelBaseAba() {
        JPanel painel = new JPanel();
        painel.setBackground(COR_FUNDO_PAINEL);
        painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        return painel;
    }

    private Border criarBordaTituladaEstilizada(String titulo) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COR_BORDA_GERAL, 2), // Borda mais grossa
                titulo.toUpperCase(),
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                FONTE_MONO_TITULO,
                COR_TEXTO_PRINCIPAL
        );
    }
    
    private void estilizarScrollPane(JScrollPane scrollPane) {
        scrollPane.getViewport().setBackground(COR_FUNDO_COMPONENTE);
        scrollPane.setBorder(BorderFactory.createLineBorder(COR_BORDA_GERAL, 2)); // Borda mais grossa
    }

    private void estilizarLista(JList<?> lista) {
        lista.setFont(FONTE_MONO_TEXTO);
        lista.setBackground(COR_FUNDO_COMPONENTE);
        lista.setForeground(COR_TEXTO_PRINCIPAL);
        lista.setSelectionBackground(COR_SELECAO_LISTA);
        lista.setSelectionForeground(COR_TEXTO_SELECAO_LISTA);
        lista.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8)); // Padding
                if (value instanceof Livro) {
                    label.setText(((Livro) value).getDescricaoCompleta());
                } else if (value instanceof Usuario) {
                    label.setText(((Usuario) value).getDescricaoCompleta());
                } else if (value instanceof Emprestimo) {
                    label.setText(((Emprestimo) value).getDescricaoCompleta());
                }
                // Cores já definidas por setSelectionBackground/Foreground
                return label;
            }
        });
    }

    private void estilizarTabela(JTable tabela) {
        // Propriedades básicas da tabela
        tabela.setFont(FONTE_MONO_TEXTO);
        tabela.setBackground(COR_FUNDO_COMPONENTE);
        tabela.setForeground(COR_TEXTO_PRINCIPAL);
        tabela.setGridColor(COR_BORDA_GERAL);
        tabela.setRowHeight(25);
        
        // Propriedades de seleção
        tabela.setSelectionBackground(COR_SELECAO_LISTA);
        tabela.setSelectionForeground(COR_TEXTO_SELECAO_LISTA);
        
        // Estilização do cabeçalho
        JTableHeader header = tabela.getTableHeader();
        header.setFont(FONTE_MONO_TITULO);
        header.setBackground(COR_BOTAO);
        header.setForeground(COR_TEXTO_PRINCIPAL);
        header.setBorder(BorderFactory.createLineBorder(COR_BORDA_GERAL, 1));
        header.setReorderingAllowed(false);

        // Configuração do renderizador de células personalizado
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Fonte e borda
                c.setFont(FONTE_MONO_TEXTO);
                setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
                
                // Cores de fundo e texto
                if (isSelected) {
                    c.setBackground(COR_SELECAO_LISTA);
                    c.setForeground(COR_TEXTO_SELECAO_LISTA);
                } else {
                    c.setBackground(row % 2 == 0 ? COR_FUNDO_COMPONENTE : new Color(245, 245, 245)); // Leve zebrado
                    c.setForeground(COR_TEXTO_PRINCIPAL);
                }
                return c;
            }
        };
        
        // Aplicar renderizador a todas as colunas
        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
    }
    // --- FIM DOS HELPER METHODS DE ESTILIZAÇÃO ---

    private void salvarDadosAoFechar() {
        // Configurar cores dos diálogos
        UIManager.put("OptionPane.background", COR_FUNDO_PAINEL);
        UIManager.put("OptionPane.messageForeground", COR_TEXTO_PRINCIPAL);
        UIManager.put("Panel.background", COR_FUNDO_PAINEL); 
        UIManager.put("OptionPane.buttonBackground", COR_BOTAO);
        UIManager.put("OptionPane.buttonForeground", COR_TEXTO_PRINCIPAL);

        Object[] options = {"SALVAR E SAIR", "SAIR SEM SALVAR", "CANCELAR"};
        JButton btnSalvar = criarBotaoEstilizado("SALVAR E SAIR");
        JButton btnSair = criarBotaoEstilizado("SAIR SEM SALVAR");
        JButton btnCancelar = criarBotaoEstilizado("CANCELAR");
        
        // Para JOptionPane com botões customizados, é um pouco mais complexo
        // Uma forma mais simples é usar o JOptionPane padrão e aceitar seu estilo (que já foi parcialmente definido no UIManager)
        int resposta = JOptionPane.showOptionDialog(this,
                "DESEJA SALVAR ALTERAÇÕES NO BANCO DE DADOS?",
                "FINALIZAR SESSÃO",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE, // Ícone
                null, // Sem ícone customizado
                options, // Texto dos botões
                options[0]); // Botão padrão

        if (resposta == JOptionPane.YES_OPTION) {
            gerenciador.salvarDados();
            mostrarMensagem(">>> DADOS PERSISTIDOS EM DISCO COM SUCESSO. ENCERRANDO...", "CONFIRMAÇÃO");
            dispose();
            System.exit(0);
        } else if (resposta == JOptionPane.NO_OPTION) {
            mostrarMensagem(">>> ENCERRANDO SEM PERSISTIR DADOS...", "AVISO");
            dispose();
            System.exit(0);
        }
        // Se CANCEL_OPTION, não faz nada
    }

    private void mostrarMensagem(String mensagem, String titulo) {
        // Configurar cores dos diálogos
        UIManager.put("OptionPane.background", COR_FUNDO_PAINEL);
        UIManager.put("OptionPane.messageForeground", COR_TEXTO_PRINCIPAL);
        UIManager.put("Panel.background", COR_FUNDO_PAINEL);
        UIManager.put("OptionPane.buttonBackground", COR_BOTAO);
        UIManager.put("OptionPane.buttonForeground", COR_TEXTO_PRINCIPAL);
        
        JOptionPane.showMessageDialog(this, mensagem.toUpperCase(), ">>> " + titulo.toUpperCase() + " <<<", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarErro(String mensagem) {
        // Configurar cores dos diálogos
        UIManager.put("OptionPane.background", COR_FUNDO_PAINEL);
        UIManager.put("OptionPane.messageForeground", COR_TEXTO_PRINCIPAL);
        UIManager.put("Panel.background", COR_FUNDO_PAINEL);
        UIManager.put("OptionPane.buttonBackground", COR_BOTAO);
        UIManager.put("OptionPane.buttonForeground", COR_TEXTO_PRINCIPAL);
        
        JOptionPane.showMessageDialog(this, mensagem.toUpperCase(), ">>> ERRO DE SISTEMA <<<", JOptionPane.ERROR_MESSAGE);
    }


    // --- PAINEIS DAS ABAS ---
    private JPanel criarPainelLivros() {
        JPanel painelAba = criarPainelBaseAba();
        // Usar GridBagLayout para melhor controle de centralização
        painelAba.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Painel do Formulário (Norte)
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBackground(COR_FUNDO_PAINEL);
        painelFormulario.setBorder(criarBordaTituladaEstilizada("Registro de Novos Volumes"));
        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.insets = new Insets(8, 8, 8, 8);
        gbcForm.anchor = GridBagConstraints.WEST;

        JTextField txtTitulo = criarTextFieldEstilizado(30);
        JTextField txtAutor = criarTextFieldEstilizado(30);
        JTextField txtEditora = criarTextFieldEstilizado(20);
        JTextField txtAno = criarTextFieldEstilizado(6);
        JTextField txtIsbn = criarTextFieldEstilizado(15);

        // Centralizar os campos do formulário
        gbcForm.gridx = 0; gbcForm.gridy = 0; painelFormulario.add(criarLabelEstilizado("Título"), gbcForm);
        gbcForm.gridx = 1; gbcForm.gridy = 0; gbcForm.fill = GridBagConstraints.HORIZONTAL; gbcForm.weightx = 1.0; painelFormulario.add(txtTitulo, gbcForm);
        gbcForm.fill = GridBagConstraints.NONE; gbcForm.weightx = 0; // Reset

        gbcForm.gridx = 0; gbcForm.gridy = 1; painelFormulario.add(criarLabelEstilizado("Autor"), gbcForm);
        gbcForm.gridx = 1; gbcForm.gridy = 1; gbcForm.fill = GridBagConstraints.HORIZONTAL; gbcForm.weightx = 1.0; painelFormulario.add(txtAutor, gbcForm);
        gbcForm.fill = GridBagConstraints.NONE; gbcForm.weightx = 0;

        gbcForm.gridx = 0; gbcForm.gridy = 2; painelFormulario.add(criarLabelEstilizado("Editora"), gbcForm);
        gbcForm.gridx = 1; gbcForm.gridy = 2; painelFormulario.add(txtEditora, gbcForm);

        gbcForm.gridx = 2; gbcForm.gridy = 2; gbcForm.insets = new Insets(8, 20, 8, 8); painelFormulario.add(criarLabelEstilizado("Ano"), gbcForm);
        gbcForm.insets = new Insets(8, 8, 8, 8); // Reset insets
        gbcForm.gridx = 3; gbcForm.gridy = 2; painelFormulario.add(txtAno, gbcForm);

        gbcForm.gridx = 0; gbcForm.gridy = 3; painelFormulario.add(criarLabelEstilizado("ISBN"), gbcForm);
        gbcForm.gridx = 1; gbcForm.gridy = 3; painelFormulario.add(txtIsbn, gbcForm);

        // Painel de Botões centralizado
        JPanel painelBotoesForm = new JPanel();
        painelBotoesForm.setBackground(COR_FUNDO_PAINEL);
        painelBotoesForm.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdicionarLivro = criarBotaoEstilizado("Adicionar Volume");
        JButton btnLimparLivro = criarBotaoEstilizado("Limpar Campos");

        painelBotoesForm.add(btnAdicionarLivro);
        painelBotoesForm.add(btnLimparLivro);
        
        gbcForm.gridx = 0; gbcForm.gridy = 4; gbcForm.gridwidth = 4; gbcForm.anchor = GridBagConstraints.CENTER;
        painelFormulario.add(painelBotoesForm, gbcForm);

        // Lista de Livros (Centro) - em um painel centralizado
        listaLivrosDisplay = new JList<>();
        estilizarLista(listaLivrosDisplay);
        JScrollPane scrollLivros = new JScrollPane(listaLivrosDisplay);
        estilizarScrollPane(scrollLivros);
        scrollLivros.setBorder(criarBordaTituladaEstilizada("Volumes Catalogados"));

        // Adicionar componentes ao painel principal, centralizados
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        painelAba.add(painelFormulario, gbc);
        
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        painelAba.add(scrollLivros, gbc);

        // Ações dos Botões
        btnAdicionarLivro.addActionListener(e -> {
            try {
                if (txtTitulo.getText().trim().isEmpty() || txtAutor.getText().trim().isEmpty() || txtIsbn.getText().trim().isEmpty()) {
                    mostrarErro("Título, Autor e ISBN são obrigatórios.");
                    return;
                }
                int anoPublicacao = 0;
                if (!txtAno.getText().trim().isEmpty()) {
                     anoPublicacao = Integer.parseInt(txtAno.getText().trim());
                } else { // Se ano estiver vazio, pode-se definir um valor padrão ou tratar como erro
                    mostrarErro("Ano de publicação é obrigatório.");
                    return;
                }
                Livro novoLivro = new Livro(txtTitulo.getText().trim(), txtAutor.getText().trim(),
                        txtEditora.getText().trim(), anoPublicacao, txtIsbn.getText().trim());
                gerenciador.adicionarLivro(novoLivro);
                atualizarListaDisplayLivros();
                atualizarTodosComboBoxEmprestimo(); // Livros podem ser usados em empréstimos
                mostrarMensagem("Volume '" + novoLivro.getTitulo() + "' catalogado.", "Sucesso");
                limparCamposLivro(txtTitulo, txtAutor, txtEditora, txtAno, txtIsbn);
            } catch (NumberFormatException ex) {
                mostrarErro("Ano de publicação inválido. Use apenas números.");
            }
        });

        btnLimparLivro.addActionListener(e -> limparCamposLivro(txtTitulo, txtAutor, txtEditora, txtAno, txtIsbn));

        return painelAba;
    }

    private JPanel criarPainelUsuarios() {
        JPanel painelAba = criarPainelBaseAba();
        // Usar GridBagLayout para melhor centralização
        painelAba.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Painel do Formulário (centralizado)
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBackground(COR_FUNDO_PAINEL);
        painelFormulario.setBorder(criarBordaTituladaEstilizada("Registro de Usuários"));
        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.insets = new Insets(8, 8, 8, 8);
        gbcForm.anchor = GridBagConstraints.WEST;

        JTextField txtNome = criarTextFieldEstilizado(30);
        JTextField txtMatricula = criarTextFieldEstilizado(15);
        JTextField txtEmail = criarTextFieldEstilizado(30);
        JTextField txtCpf = criarTextFieldEstilizado(15);
        JComboBox<String> cmbTipoUsuario = criarComboBoxEstilizado();
        cmbTipoUsuario.addItem("Estudante");
        cmbTipoUsuario.addItem("Professor");
        cmbTipoUsuario.addItem("Funcionário");

        // Centralizar os componentes do formulário
        gbcForm.gridx = 0; gbcForm.gridy = 0; painelFormulario.add(criarLabelEstilizado("Nome Completo"), gbcForm);
        gbcForm.gridx = 1; gbcForm.gridy = 0; gbcForm.gridwidth = 3; gbcForm.fill = GridBagConstraints.HORIZONTAL; gbcForm.weightx = 1.0; painelFormulario.add(txtNome, gbcForm);
        gbcForm.gridwidth = 1; gbcForm.fill = GridBagConstraints.NONE; gbcForm.weightx = 0; // Reset

        gbcForm.gridx = 0; gbcForm.gridy = 1; painelFormulario.add(criarLabelEstilizado("Matrícula"), gbcForm);
        gbcForm.gridx = 1; gbcForm.gridy = 1; painelFormulario.add(txtMatricula, gbcForm);

        gbcForm.gridx = 2; gbcForm.gridy = 1; gbcForm.insets = new Insets(8,20,8,8); painelFormulario.add(criarLabelEstilizado("CPF"), gbcForm);
        gbcForm.insets = new Insets(8,8,8,8);
        gbcForm.gridx = 3; gbcForm.gridy = 1; painelFormulario.add(txtCpf, gbcForm);

        gbcForm.gridx = 0; gbcForm.gridy = 2; painelFormulario.add(criarLabelEstilizado("Email"), gbcForm);
        gbcForm.gridx = 1; gbcForm.gridy = 2; gbcForm.gridwidth = 3; gbcForm.fill = GridBagConstraints.HORIZONTAL; gbcForm.weightx = 1.0; painelFormulario.add(txtEmail, gbcForm);
        gbcForm.gridwidth = 1; gbcForm.fill = GridBagConstraints.NONE; gbcForm.weightx = 0;

        gbcForm.gridx = 0; gbcForm.gridy = 3; painelFormulario.add(criarLabelEstilizado("Tipo de Usuário"), gbcForm);
        gbcForm.gridx = 1; gbcForm.gridy = 3; painelFormulario.add(cmbTipoUsuario, gbcForm);


        // Painel de Botões centralizado
        JPanel painelBotoesForm = new JPanel();
        painelBotoesForm.setBackground(COR_FUNDO_PAINEL);
        painelBotoesForm.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdicionarUsuario = criarBotaoEstilizado("Registrar Usuário");
        JButton btnLimparUsuario = criarBotaoEstilizado("Limpar Campos");
        painelBotoesForm.add(btnAdicionarUsuario);
        painelBotoesForm.add(btnLimparUsuario);

        gbcForm.gridx = 0; gbcForm.gridy = 4; gbcForm.gridwidth = 4; gbcForm.anchor = GridBagConstraints.CENTER;
        painelFormulario.add(painelBotoesForm, gbcForm);


        // Lista de Usuários (centralizado)
        listaUsuariosDisplay = new JList<>();
        estilizarLista(listaUsuariosDisplay);
        JScrollPane scrollUsuarios = new JScrollPane(listaUsuariosDisplay);
        estilizarScrollPane(scrollUsuarios);
        scrollUsuarios.setBorder(criarBordaTituladaEstilizada("Usuários Registrados"));

        // Adicionar componentes ao painel principal, centralizados
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        painelAba.add(painelFormulario, gbc);
        
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        painelAba.add(scrollUsuarios, gbc);

        // Ações
        btnAdicionarUsuario.addActionListener(e -> {
            String nome = txtNome.getText().trim();
            String matricula = txtMatricula.getText().trim();
            String email = txtEmail.getText().trim();
            String cpf = txtCpf.getText().trim();
            String tipo = (String) cmbTipoUsuario.getSelectedItem();

            if (nome.isEmpty() || matricula.isEmpty() || cpf.isEmpty()) {
                mostrarErro("Nome, Matrícula e CPF são obrigatórios.");
                return;
            }

            Usuario novoUsuario = null;
            switch (tipo) {
                case "Estudante": novoUsuario = new Estudante(nome, matricula, email, cpf); break;
                case "Professor": novoUsuario = new Professor(nome, matricula, email, cpf); break;
                case "Funcionário": novoUsuario = new Funcionario(nome, matricula, email, cpf); break;
            }

            if (novoUsuario != null) {
                gerenciador.adicionarUsuario(novoUsuario);
                atualizarListaDisplayUsuarios();
                atualizarTodosComboBoxEmprestimo(); // Usuários podem ser usados em empréstimos
                mostrarMensagem("Usuário '" + novoUsuario.getNome() + "' registrado.", "Sucesso");
                limparCamposUsuario(txtNome, txtMatricula, txtEmail, txtCpf);
            }
        });
        btnLimparUsuario.addActionListener(e -> limparCamposUsuario(txtNome, txtMatricula, txtEmail, txtCpf));

        return painelAba;
    }

    private JPanel criarPainelEmprestimos() {
        JPanel painelAba = criarPainelBaseAba();
        painelAba.setLayout(new BorderLayout(10, 20)); // Aumentado gap vertical

        // Painel Superior com Empréstimo e Devolução lado a lado
        JPanel painelSuperior = new JPanel(new GridLayout(1, 2, 20, 0)); // 1 linha, 2 colunas, gap horizontal
        painelSuperior.setBackground(COR_FUNDO_PAINEL);

        // Seção de Novo Empréstimo
        JPanel painelNovoEmprestimo = new JPanel(new GridBagLayout());
        painelNovoEmprestimo.setBackground(COR_FUNDO_PAINEL);
        painelNovoEmprestimo.setBorder(criarBordaTituladaEstilizada("Registrar Novo Empréstimo"));
        GridBagConstraints gbcEmprestimo = new GridBagConstraints();
        gbcEmprestimo.insets = new Insets(5,5,5,5);
        gbcEmprestimo.anchor = GridBagConstraints.WEST;
        gbcEmprestimo.fill = GridBagConstraints.HORIZONTAL;


        cmbLivrosParaEmprestimo = criarComboBoxEstilizado();
        cmbUsuariosParaEmprestimo = criarComboBoxEstilizado();
        JTextField txtDataDevolucao = criarTextFieldEstilizado(10);
        txtDataDevolucao.setToolTipText("Formato DD/MM/AAAA");
        JButton btnRealizarEmprestimo = criarBotaoEstilizado("Efetuar Empréstimo");

        gbcEmprestimo.gridx = 0; gbcEmprestimo.gridy = 0; painelNovoEmprestimo.add(criarLabelEstilizado("Volume"), gbcEmprestimo);
        gbcEmprestimo.gridx = 1; gbcEmprestimo.gridy = 0; gbcEmprestimo.weightx=1.0; painelNovoEmprestimo.add(cmbLivrosParaEmprestimo, gbcEmprestimo);
        gbcEmprestimo.weightx=0;

        gbcEmprestimo.gridx = 0; gbcEmprestimo.gridy = 1; painelNovoEmprestimo.add(criarLabelEstilizado("Usuário"), gbcEmprestimo);
        gbcEmprestimo.gridx = 1; gbcEmprestimo.gridy = 1; gbcEmprestimo.weightx=1.0; painelNovoEmprestimo.add(cmbUsuariosParaEmprestimo, gbcEmprestimo);
        gbcEmprestimo.weightx=0;

        gbcEmprestimo.gridx = 0; gbcEmprestimo.gridy = 2; painelNovoEmprestimo.add(criarLabelEstilizado("Data Devol."), gbcEmprestimo);
        gbcEmprestimo.gridx = 1; gbcEmprestimo.gridy = 2; painelNovoEmprestimo.add(txtDataDevolucao, gbcEmprestimo);
        
        gbcEmprestimo.gridx = 0; gbcEmprestimo.gridy = 3; gbcEmprestimo.gridwidth=2; gbcEmprestimo.anchor = GridBagConstraints.CENTER; gbcEmprestimo.fill = GridBagConstraints.NONE;
        painelNovoEmprestimo.add(btnRealizarEmprestimo, gbcEmprestimo);

        painelSuperior.add(painelNovoEmprestimo);

        // Seção de Devolução
        JPanel painelDevolucao = new JPanel(new GridBagLayout());
        painelDevolucao.setBackground(COR_FUNDO_PAINEL);
        painelDevolucao.setBorder(criarBordaTituladaEstilizada("Registrar Devolução"));
        GridBagConstraints gbcDevolucao = new GridBagConstraints();
        gbcDevolucao.insets = new Insets(5,5,5,5);
        gbcDevolucao.anchor = GridBagConstraints.WEST;
        gbcDevolucao.fill = GridBagConstraints.HORIZONTAL;

        cmbEmprestimosAtivosParaDevolucao = criarComboBoxEstilizado();
        JButton btnDevolverLivro = criarBotaoEstilizado("Confirmar Devolução");

        gbcDevolucao.gridx = 0; gbcDevolucao.gridy = 0; painelDevolucao.add(criarLabelEstilizado("Empréstimo Ativo"), gbcDevolucao);
        gbcDevolucao.gridx = 1; gbcDevolucao.gridy = 0; gbcDevolucao.weightx=1.0; painelDevolucao.add(cmbEmprestimosAtivosParaDevolucao, gbcDevolucao);
        gbcDevolucao.weightx=0;

        gbcDevolucao.gridx = 0; gbcDevolucao.gridy = 1; gbcDevolucao.gridwidth=2; gbcDevolucao.anchor = GridBagConstraints.CENTER; gbcDevolucao.fill = GridBagConstraints.NONE;
        gbcDevolucao.insets = new Insets(20,5,5,5); // Mais espaço acima do botão
        painelDevolucao.add(btnDevolverLivro, gbcDevolucao);
        
        painelSuperior.add(painelDevolucao);
        painelAba.add(painelSuperior, BorderLayout.NORTH);

        // Tabela de Todos os Empréstimos (Centro)
        String[] colunasEmprestimos = {"ID", "LIVRO", "USUÁRIO", "DATA EMP.", "DATA DEV.", "STATUS"};
        modeloTabelaEmprestimos = new DefaultTableModel(colunasEmprestimos, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tabelaEmprestimos = new JTable(modeloTabelaEmprestimos);
        estilizarTabela(tabelaEmprestimos);
        // Configurar larguras preferenciais das colunas
        tabelaEmprestimos.getColumnModel().getColumn(0).setPreferredWidth(40);  // ID
        tabelaEmprestimos.getColumnModel().getColumn(1).setPreferredWidth(250); // Livro
        tabelaEmprestimos.getColumnModel().getColumn(2).setPreferredWidth(150); // Usuário
        tabelaEmprestimos.getColumnModel().getColumn(3).setPreferredWidth(100); // Data Emp.
        tabelaEmprestimos.getColumnModel().getColumn(4).setPreferredWidth(100); // Data Dev.
        tabelaEmprestimos.getColumnModel().getColumn(5).setPreferredWidth(100); // Status

        JScrollPane scrollTabela = new JScrollPane(tabelaEmprestimos);
        estilizarScrollPane(scrollTabela);
        scrollTabela.setBorder(criarBordaTituladaEstilizada("Histórico de Empréstimos"));
        painelAba.add(scrollTabela, BorderLayout.CENTER);

        // Ações
        btnRealizarEmprestimo.addActionListener(e -> {
            Livro livro = (Livro) cmbLivrosParaEmprestimo.getSelectedItem();
            Usuario usuario = (Usuario) cmbUsuariosParaEmprestimo.getSelectedItem();
            String dataDev = txtDataDevolucao.getText().trim();

            if (livro == null || usuario == null || dataDev.isEmpty()) {
                mostrarErro("Selecione Livro, Usuário e informe a Data de Devolução.");
                return;
            }
            if (gerenciador.isLivroEmprestado(livro)) {
                mostrarErro("Este livro já está emprestado e pendente de devolução.");
                return;
            }

            try {
                // Validação simples de data (pode ser melhorada com SimpleDateFormat)
                if (!dataDev.matches("\\d{2}/\\d{2}/\\d{4}")) {
                    mostrarErro("Formato da Data de Devolução inválido. Use DD/MM/AAAA.");
                    return;
                }
                Emprestimo novoEmprestimo = new Emprestimo(livro, usuario, dataDev);
                gerenciador.realizarEmprestimo(novoEmprestimo);
                atualizarTabelaEmprestimos();
                atualizarTodosComboBoxEmprestimo();
                txtDataDevolucao.setText("");
                mostrarMensagem("Empréstimo do volume '" + livro.getTitulo() + "' para '" + usuario.getNome() + "' realizado.", "Sucesso");
            } catch (Exception ex) {
                mostrarErro("Erro ao processar empréstimo: " + ex.getMessage());
            }
        });

        btnDevolverLivro.addActionListener(e -> {
            Emprestimo emprestimoParaDevolver = (Emprestimo) cmbEmprestimosAtivosParaDevolucao.getSelectedItem();
            if (emprestimoParaDevolver == null) {
                mostrarErro("Selecione um empréstimo ativo para devolução.");
                return;
            }
            gerenciador.devolverLivro(emprestimoParaDevolver);
            atualizarTabelaEmprestimos();
            atualizarTodosComboBoxEmprestimo();
            mostrarMensagem("Volume '" + emprestimoParaDevolver.getLivro().getTitulo() + "' devolvido.", "Sucesso");
        });

        return painelAba;
    }

    private JPanel criarPainelConsulta() {
        JPanel painelAba = criarPainelBaseAba();
        painelAba.setLayout(new BorderLayout(10, 10));

        // Painel de Filtros de Busca (Norte)
        JPanel painelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        painelFiltros.setBackground(COR_FUNDO_PAINEL);
        painelFiltros.setBorder(criarBordaTituladaEstilizada("Filtros de Pesquisa Avançada"));

        JTextField txtTermoBusca = criarTextFieldEstilizado(30);
        txtTermoBusca.setToolTipText("Digite o termo para buscar...");

        JRadioButton rbLivros = new JRadioButton("LIVROS");
        JRadioButton rbUsuarios = new JRadioButton("USUÁRIOS");
        JRadioButton rbEmprestimos = new JRadioButton("EMPRÉSTIMOS");
        configurarRadioButton(rbLivros, true);
        configurarRadioButton(rbUsuarios, false);
        configurarRadioButton(rbEmprestimos, false);

        ButtonGroup grupoTipoBusca = new ButtonGroup();
        grupoTipoBusca.add(rbLivros);
        grupoTipoBusca.add(rbUsuarios);
        grupoTipoBusca.add(rbEmprestimos);

        JButton btnBuscar = criarBotaoEstilizado("Buscar");

        painelFiltros.add(criarLabelEstilizado("Termo"));
        painelFiltros.add(txtTermoBusca);
        painelFiltros.add(Box.createHorizontalStrut(15));
        painelFiltros.add(criarLabelEstilizado("Buscar em"));
        painelFiltros.add(rbLivros);
        painelFiltros.add(rbUsuarios);
        painelFiltros.add(rbEmprestimos);
        painelFiltros.add(Box.createHorizontalStrut(15));
        painelFiltros.add(btnBuscar);

        painelAba.add(painelFiltros, BorderLayout.NORTH);

        // Tabela de Resultados da Consulta (Centro)
        // Colunas serão definidas dinamicamente
        modeloTabelaConsulta = new DefaultTableModel() {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tabelaResultados = new JTable(modeloTabelaConsulta);
        estilizarTabela(tabelaResultados); // Estilização geral

        JScrollPane scrollResultados = new JScrollPane(tabelaResultados);
        estilizarScrollPane(scrollResultados);
        scrollResultados.setBorder(criarBordaTituladaEstilizada("Resultados da Consulta"));
        painelAba.add(scrollResultados, BorderLayout.CENTER);

        // Ação do botão buscar
        btnBuscar.addActionListener(e -> {
            String termo = txtTermoBusca.getText().trim().toLowerCase();
            modeloTabelaConsulta.setRowCount(0); // Limpa resultados anteriores

            if (rbLivros.isSelected()) {
                modeloTabelaConsulta.setColumnIdentifiers(new String[]{"ID", "TÍTULO", "AUTOR", "EDITORA", "ANO", "ISBN", "STATUS"});
                for (Livro l : gerenciador.getLivros()) {
                    if (l.getTitulo().toLowerCase().contains(termo) ||
                        l.getAutor().toLowerCase().contains(termo) ||
                        l.getIsbn().toLowerCase().contains(termo) ||
                        String.valueOf(l.getId()).equals(termo)) {
                        modeloTabelaConsulta.addRow(new Object[]{
                                l.getId(), l.getTitulo(), l.getAutor(), l.getEditora(),
                                l.getAno(), l.getIsbn(), gerenciador.isLivroEmprestado(l) ? "Emprestado" : "Disponível"
                        });
                    }
                }
                 // Ajustar colunas da tabela de consulta para livros
                tabelaResultados.getColumnModel().getColumn(0).setPreferredWidth(40); // ID
                tabelaResultados.getColumnModel().getColumn(1).setPreferredWidth(250); // Título
                tabelaResultados.getColumnModel().getColumn(2).setPreferredWidth(150); // Autor
                tabelaResultados.getColumnModel().getColumn(3).setPreferredWidth(120); // Editora
                tabelaResultados.getColumnModel().getColumn(4).setPreferredWidth(60);  // Ano
                tabelaResultados.getColumnModel().getColumn(5).setPreferredWidth(120); // ISBN
                tabelaResultados.getColumnModel().getColumn(6).setPreferredWidth(100); // Status
            } else if (rbUsuarios.isSelected()) {
                modeloTabelaConsulta.setColumnIdentifiers(new String[]{"ID", "NOME", "MATRÍCULA", "EMAIL", "CPF", "TIPO"});
                for (Usuario u : gerenciador.getUsuarios()) {
                    if (u.getNome().toLowerCase().contains(termo) ||
                        u.getMatricula().toLowerCase().contains(termo) ||
                        u.getCpf().toLowerCase().contains(termo) ||
                        u.getEmail().toLowerCase().contains(termo) ||
                        String.valueOf(u.getId()).equals(termo)) {
                        String tipo = u.getClass().getSimpleName().replace("Usuario", "");
                        modeloTabelaConsulta.addRow(new Object[]{
                                u.getId(), u.getNome(), u.getMatricula(), u.getEmail(), u.getCpf(), tipo
                        });
                    }
                }
                 // Ajustar colunas da tabela de consulta para usuários
                tabelaResultados.getColumnModel().getColumn(0).setPreferredWidth(40);  // ID
                tabelaResultados.getColumnModel().getColumn(1).setPreferredWidth(200); // Nome
                tabelaResultados.getColumnModel().getColumn(2).setPreferredWidth(100); // Matrícula
                tabelaResultados.getColumnModel().getColumn(3).setPreferredWidth(200); // Email
                tabelaResultados.getColumnModel().getColumn(4).setPreferredWidth(120); // CPF
                tabelaResultados.getColumnModel().getColumn(5).setPreferredWidth(100); // Tipo
            } else if (rbEmprestimos.isSelected()) {
                modeloTabelaConsulta.setColumnIdentifiers(new String[]{"ID EMP.", "LIVRO (ID)", "USUÁRIO (ID)", "DATA EMP.", "DATA DEV.", "STATUS"});
                for (Emprestimo emp : gerenciador.getEmprestimos()) {
                    if (emp.getLivro().getTitulo().toLowerCase().contains(termo) ||
                        emp.getUsuario().getNome().toLowerCase().contains(termo) ||
                        String.valueOf(emp.getId()).equals(termo) ||
                        String.valueOf(emp.getLivro().getId()).equals(termo) ||
                        String.valueOf(emp.getUsuario().getId()).equals(termo)
                        ) {
                        modeloTabelaConsulta.addRow(new Object[]{
                                emp.getId(),
                                emp.getLivro().getTitulo() + " (" + emp.getLivro().getId() + ")",
                                emp.getUsuario().getNome() + " (" + emp.getUsuario().getId() + ")",
                                emp.getDataEmprestimo(), emp.getDataDevolucao(),
                                emp.isAtivo() ? "Ativo" : "Devolvido"
                        });
                    }
                }
                 // Ajustar colunas da tabela de consulta para empréstimos
                tabelaResultados.getColumnModel().getColumn(0).setPreferredWidth(60);   // ID EMP.
                tabelaResultados.getColumnModel().getColumn(1).setPreferredWidth(250);  // LIVRO (ID)
                tabelaResultados.getColumnModel().getColumn(2).setPreferredWidth(200);  // USUÁRIO (ID)
                tabelaResultados.getColumnModel().getColumn(3).setPreferredWidth(100);  // DATA EMP.
                tabelaResultados.getColumnModel().getColumn(4).setPreferredWidth(100);  // DATA DEV.
                tabelaResultados.getColumnModel().getColumn(5).setPreferredWidth(100);  // STATUS
            }
            if (modeloTabelaConsulta.getRowCount() == 0) {
                mostrarMensagem("Nenhum registro encontrado para o termo '" + termo + "'.", "Busca");
            }
        });
        return painelAba;
    }
    
    private void configurarRadioButton(JRadioButton rb, boolean selecionado) {
        rb.setFont(FONTE_MONO_TEXTO);
        rb.setBackground(COR_FUNDO_PAINEL);
        rb.setForeground(COR_TEXTO_PRINCIPAL);
        rb.setSelected(selecionado);
        rb.setFocusPainted(false);
        rb.setOpaque(true); // Importante para garantir que o fundo seja pintado
    }

    private JPanel criarPainelSistema() {
        JPanel painelAba = criarPainelBaseAba();
        painelAba.setLayout(new BorderLayout(10,20));

        JPanel painelConteudo = new JPanel();
        painelConteudo.setLayout(new BoxLayout(painelConteudo, BoxLayout.Y_AXIS));
        painelConteudo.setBackground(COR_FUNDO_PAINEL);
        painelConteudo.setBorder(criarBordaTituladaEstilizada("Status e Comandos do Sistema"));

        // Botão Salvar Dados
        JButton btnSalvarDados = criarBotaoEstilizado("Persistir Dados no Disco");
        btnSalvarDados.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalvarDados.addActionListener(e -> {
            gerenciador.salvarDados();
            mostrarMensagem("Dados persistidos com sucesso.", "Sistema");
        });

        // Text Area para Informações do "Sistema"
        JTextArea areaInfoSistema = new JTextArea(10, 50);
        areaInfoSistema.setFont(FONTE_MONO_TEXTO);
        areaInfoSistema.setBackground(COR_FUNDO_COMPONENTE);
        areaInfoSistema.setForeground(COR_TEXTO_PRINCIPAL);
        areaInfoSistema.setCaretColor(COR_TEXTO_PRINCIPAL);
        areaInfoSistema.setEditable(false);
        areaInfoSistema.setLineWrap(true);
        areaInfoSistema.setWrapStyleWord(true);
        areaInfoSistema.setBorder(BorderFactory.createLineBorder(COR_BORDA_GERAL, 1));
        areaInfoSistema.setText(
            "SISTEMA DE BIBLIOTECA - INFORMAÇÕES\n" +
            "-------------------------------------\n" +
            "STATUS: ONLINE\n" +
            "CONEXÃO BD: ATIVA (ARMAZENAMENTO EM DISCO)\n" +
            "USO DE MEMÓRIA: 47.2 MB / 128.0 MB ALOCADOS\n" +
            "THREADS ATIVOS: 3 (UI, GC, EVENT_DISPATCH)\n" +
            "PROTOCOLO DE SEGURANÇA: AES-256\n\n" +
            "ÚLTIMO COMANDO: N/A\n" +
            "LOG DO SISTEMA: logs/sistema_biblioteca.log\n\n" +
            "Para suporte técnico, contate o administrador\n" +
            "Copyright © Sistema de Biblioteca\n"
        );
        JScrollPane scrollInfo = new JScrollPane(areaInfoSistema);
        estilizarScrollPane(scrollInfo);
        scrollInfo.setAlignmentX(Component.CENTER_ALIGNMENT);


        painelConteudo.add(Box.createVerticalStrut(20));
        painelConteudo.add(btnSalvarDados);
        painelConteudo.add(Box.createVerticalStrut(30));
        painelConteudo.add(scrollInfo);
        painelConteudo.add(Box.createVerticalGlue()); // Empurra para cima

        // Adiciona um painel wrapper para centralizar o conteúdo
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(COR_FUNDO_PAINEL);
        wrapper.add(painelConteudo, BorderLayout.CENTER);

        painelAba.add(wrapper, BorderLayout.CENTER);
        return painelAba;
    }

    // --- MÉTODOS DE ATUALIZAÇÃO DE DADOS NA GUI ---
    private void atualizarListaDisplayLivros() {
        DefaultListModel<String> modelo = new DefaultListModel<>();
        for (Livro livro : gerenciador.getLivros()) {
            modelo.addElement(livro.getDescricaoCompleta());
        }
        if (listaLivrosDisplay != null) listaLivrosDisplay.setModel(modelo);
    }

    private void atualizarListaDisplayUsuarios() {
        DefaultListModel<String> modelo = new DefaultListModel<>();
        for (Usuario usuario : gerenciador.getUsuarios()) {
            modelo.addElement(usuario.getDescricaoCompleta());
        }
        if (listaUsuariosDisplay != null) listaUsuariosDisplay.setModel(modelo);
    }

    private void atualizarTodosComboBoxEmprestimo() {
        // Livros disponíveis para empréstimo
        if (cmbLivrosParaEmprestimo != null) {
            cmbLivrosParaEmprestimo.removeAllItems();
            for (Livro livro : gerenciador.getLivros()) {
                if (!gerenciador.isLivroEmprestado(livro)) { // Apenas livros não emprestados ativamente
                    cmbLivrosParaEmprestimo.addItem(livro);
                }
            }
        }
        // Todos os usuários para empréstimo
        if (cmbUsuariosParaEmprestimo != null) {
            cmbUsuariosParaEmprestimo.removeAllItems();
            for (Usuario usuario : gerenciador.getUsuarios()) {
                cmbUsuariosParaEmprestimo.addItem(usuario);
            }
        }
        // Empréstimos ativos para devolução
        if (cmbEmprestimosAtivosParaDevolucao != null) {
            cmbEmprestimosAtivosParaDevolucao.removeAllItems();
            for (Emprestimo emprestimo : gerenciador.getEmprestimos()) {
                if (emprestimo.isAtivo()) {
                    cmbEmprestimosAtivosParaDevolucao.addItem(emprestimo);
                }
            }
        }
    }

    private void atualizarTabelaEmprestimos() {
        if (modeloTabelaEmprestimos == null) return;
        modeloTabelaEmprestimos.setRowCount(0); // Limpa tabela
        for (Emprestimo emp : gerenciador.getEmprestimos()) {
            modeloTabelaEmprestimos.addRow(new Object[]{
                    emp.getId(),
                    emp.getLivro().getTitulo(),
                    emp.getUsuario().getNome(),
                    emp.getDataEmprestimo(),
                    emp.getDataDevolucao(),
                    emp.isAtivo() ? "ATIVO" : "DEVOLVIDO"
            });
        }
    }

    // --- MÉTODOS DE LIMPEZA DE CAMPOS ---
    private void limparCamposLivro(JTextField... campos) {
        for (JTextField campo : campos) campo.setText("");
        if (campos.length > 0) campos[0].requestFocusInWindow();
    }

    private void limparCamposUsuario(JTextField... campos) {
        for (JTextField campo : campos) campo.setText("");
        if (campos.length > 0) campos[0].requestFocusInWindow();
    }
}
