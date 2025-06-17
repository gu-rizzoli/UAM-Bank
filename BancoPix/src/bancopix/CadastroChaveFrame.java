package bancopix;

import javax.swing.*;
import java.awt.*;

public class CadastroChaveFrame extends JFrame {

    private JComboBox<String> comboTipoChave;
    private JTextField txtValorChave;
    private JButton btnCadastrar, btnVoltar, btnRemover;
    private Usuario usuarioLogado;

    public CadastroChaveFrame(Usuario usuario) {
        this.usuarioLogado = usuario;

        // Título fixo
        setTitle("UAM Bank - Cadastro de Chave PIX");
        setSize(450, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Fontes
        Font fonteTitulo = new Font("Segoe UI", Font.BOLD, 22);
        Font fontePadrao = new Font("Segoe UI", Font.PLAIN, 14);

        // Cores
        Color corFundo = Color.WHITE;
        Color corBotaoPrimario = new Color(0, 123, 255);
        Color corBotaoPerigo = new Color(220, 53, 69);
        Color corBotaoSecundario = new Color(108, 117, 125);

        getContentPane().setBackground(corFundo);

        // Título
        JLabel titulo = new JLabel("Cadastrar Chave PIX", SwingConstants.CENTER);
        titulo.setFont(fonteTitulo);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        // Painel central
        JPanel painelCentral = new JPanel(new GridBagLayout());
        painelCentral.setBackground(corFundo);
        painelCentral.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tipo da chave
        gbc.gridx = 0;
        gbc.gridy = 0;
        painelCentral.add(new JLabel("Tipo da chave:"), gbc);

        gbc.gridx = 1;
        comboTipoChave = new JComboBox<>(new String[]{"cpf", "email"});
        comboTipoChave.setFont(fontePadrao);
        painelCentral.add(comboTipoChave, gbc);

        // Valor da chave
        gbc.gridx = 0;
        gbc.gridy = 1;
        painelCentral.add(new JLabel("Valor da chave:"), gbc);

        gbc.gridx = 1;
        txtValorChave = new JTextField();
        txtValorChave.setFont(fontePadrao);
        painelCentral.add(txtValorChave, gbc);

        add(painelCentral, BorderLayout.CENTER);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        painelBotoes.setBackground(corFundo);

        btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBackground(new Color(0x17FF0F));
        btnCadastrar.setForeground(Color.WHITE);
        btnCadastrar.setFocusPainted(false);
        btnCadastrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCadastrar.setBackground(new Color(0, 123, 255));
        btnCadastrar.setForeground(Color.WHITE);
        btnCadastrar.setFocusPainted(false);
        btnCadastrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRemover = new JButton("Remover");
        btnRemover.setBackground(new Color(0xDC3545));
        btnRemover.setForeground(Color.WHITE);
        btnRemover.setFocusPainted(false);
        btnRemover.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRemover.setBackground(new Color(220, 53, 69));
        btnRemover.setForeground(Color.WHITE);
        btnRemover.setFocusPainted(false);
        btnRemover.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(new Color(0x6c757d));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFocusPainted(false);
        btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVoltar.setBackground(new Color(108, 117, 125));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFocusPainted(false);
        btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        estilizarBotao(btnCadastrar, corBotaoPrimario, fontePadrao);
        estilizarBotao(btnRemover, corBotaoPerigo, fontePadrao);
        estilizarBotao(btnVoltar, corBotaoSecundario, fontePadrao);

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnVoltar);

        add(painelBotoes, BorderLayout.SOUTH);

        // Ações
        btnCadastrar.addActionListener(e -> {
            String tipo = (String) comboTipoChave.getSelectedItem();
            String valor = txtValorChave.getText().trim();

            if (valor.isEmpty()) {
                mostrarErro("Preencha o valor da chave.");
                return;
            }

            if (!(
                ("cpf".equals(tipo) && valor.equals(usuarioLogado.getCpf())) ||
                ("email".equals(tipo) && valor.equalsIgnoreCase(usuarioLogado.getEmail()))
            )) {
                mostrarErro("Você só pode cadastrar sua própria chave (CPF ou email).");
                return;
            }

            ChavePix chave = new ChavePix();
            chave.setUsuarioId(usuarioLogado.getId());
            chave.setTipo(tipo);
            chave.setChave(valor);

            ChavePixDAO chaveDao = new ChavePixDAO();
            boolean sucesso = chaveDao.cadastrarChave(chave);

            if (sucesso) {
                mostrarSucesso("Chave PIX cadastrada com sucesso!");
                new TelaPrincipalFrame(usuarioLogado).setVisible(true);
                dispose();
            } else {
                mostrarErro("Erro: a chave já existe ou houve falha na conexão.");
            }
        });

        btnRemover.addActionListener(e -> {
            String valor = txtValorChave.getText().trim();
            if (valor.isEmpty()) {
                mostrarErro("Digite o valor da chave que deseja remover.");
                return;
            }

            ChavePixDAO chaveDao = new ChavePixDAO();
            boolean removido = chaveDao.removerChave(usuarioLogado.getId(), valor);

            if (removido) {
                mostrarSucesso("Chave removida com sucesso.");
            } else {
                mostrarErro("Chave não encontrada ou erro ao remover.");
            }
        });

        btnVoltar.addActionListener(e -> {
            new TelaPrincipalFrame(usuarioLogado).setVisible(true);
            dispose();
        });
    }

    private void estilizarBotao(JButton botao, Color corFundo, Font fonte) {
        botao.setFont(fonte);
        botao.setFocusPainted(false);
        botao.setBackground(corFundo);
        botao.setForeground(Color.WHITE);
        botao.setPreferredSize(new Dimension(110, 30));
    }

    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarSucesso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
}
