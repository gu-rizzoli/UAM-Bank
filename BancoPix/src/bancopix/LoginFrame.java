package bancopix;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JButton btnLogin, btnCadastrar;

    public LoginFrame() {
        setTitle("UAM Bank - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(); painel.setBackground(Color.WHITE);
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        Font fonte = new Font("SansSerif", Font.PLAIN, 14);

        txtEmail = criarCampoComRotulo("Email:", painel, fonte);
        txtSenha = new JPasswordField();
        adicionarComRotulo("Senha:", txtSenha, painel, fonte);

        btnLogin = new JButton("Entrar");
        btnLogin.setBackground(new Color(0, 123, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setFont(fonte);
        btnLogin.setBackground(new Color(0, 120, 215));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);

        btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBackground(new Color(0x17FF0F));
        btnCadastrar.setForeground(Color.WHITE);
        btnCadastrar.setFocusPainted(false);
        btnCadastrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCadastrar.setBackground(new Color(40, 167, 69));
        btnCadastrar.setForeground(Color.WHITE);
        btnCadastrar.setFocusPainted(false);
        btnCadastrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCadastrar.setFont(fonte);
        btnCadastrar.setBackground(Color.GRAY);
        btnCadastrar.setForeground(Color.WHITE);
        btnCadastrar.setFocusPainted(false);

        painel.add(Box.createVerticalStrut(10));
        painel.add(btnLogin);
        painel.add(Box.createVerticalStrut(5));
        painel.add(btnCadastrar);

        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnCadastrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        add(painel);

        btnLogin.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String senha = new String(txtSenha.getPassword()).trim();

            if (email.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha email e senha.");
                return;
            }

            UsuarioDAO dao = new UsuarioDAO();
            Usuario usuario = dao.login(email, senha);

            if (usuario != null) {
                JOptionPane.showMessageDialog(this, "Login realizado com sucesso! Bem-vindo, " + usuario.getNome());
                new TelaPrincipalFrame(usuario).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Email ou senha incorretos.");
            }
        });

        btnCadastrar.addActionListener(e -> {
            new CadastroFrame().setVisible(true);
            this.dispose();
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
