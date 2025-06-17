package bancopix;

import javax.swing.*;
import java.awt.*;

public class CadastroFrame extends JFrame {

    private JTextField txtNome, txtCpf, txtEmail;
    private JPasswordField txtSenha;
    private JButton btnCadastrar;

    public CadastroFrame() {
        setTitle("UAM Bank - Cadastro de Usuário");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Painel principal com borda e layout vertical
        JPanel painel = new JPanel(); painel.setBackground(Color.WHITE);
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        Font fontePadrao = new Font("SansSerif", Font.PLAIN, 14);

        txtNome = criarCampoComRotulo("Nome:", painel, fontePadrao);
        txtCpf = criarCampoComRotulo("CPF:", painel, fontePadrao);
        txtEmail = criarCampoComRotulo("Email:", painel, fontePadrao);
        txtSenha = new JPasswordField();
        adicionarComRotulo("Senha:", txtSenha, painel, fontePadrao);

        btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBackground(new Color(0x17FF0F));
        btnCadastrar.setForeground(Color.WHITE);
        btnCadastrar.setFocusPainted(false);
        btnCadastrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCadastrar.setFont(fontePadrao);
        btnCadastrar.setBackground(new Color(0, 120, 215));
        btnCadastrar.setForeground(Color.WHITE);
        btnCadastrar.setFocusPainted(false);

        btnCadastrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCadastrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        painel.add(Box.createVerticalStrut(10));
        painel.add(btnCadastrar);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(new Color(0x6c757d));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFocusPainted(false);
        btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVoltar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVoltar.setBackground(new Color(220, 53, 69));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFocusPainted(false);
        btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVoltar.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        painel.add(Box.createRigidArea(new Dimension(0, 10)));
        painel.add(btnVoltar);


        add(painel);

        btnCadastrar.addActionListener(e -> {
            try {
                String nome = txtNome.getText().trim();
                String cpf = txtCpf.getText().trim();
                String email = txtEmail.getText().trim();
                String senha = new String(txtSenha.getPassword()).trim();

                if (nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
                    return;
                }

                Usuario usuario = new Usuario();
                usuario.setNome(nome);
                usuario.setCpf(cpf);
                usuario.setEmail(email);
                usuario.setSenha(senha);

                UsuarioDAO dao = new UsuarioDAO();
                boolean sucesso = dao.cadastrar(usuario);

                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");

                    new LoginFrame().setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário. CPF ou email já estão cadastrados.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro inesperado: " + ex.getMessage());
            }
        });
    }

    private JTextField criarCampoComRotulo(String rotulo, JPanel painel, Font fonte) {
        JTextField campo = new JTextField();
        adicionarComRotulo(rotulo, campo, painel, fonte);
        return campo;
    }

    private void adicionarComRotulo(String rotulo, JComponent campo, JPanel painel, Font fonte) {
        JLabel label = new JLabel(rotulo);
        label.setFont(fonte);
        campo.setFont(fonte);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        painel.add(label);
        painel.add(campo);
        painel.add(Box.createVerticalStrut(5));
    }
}
