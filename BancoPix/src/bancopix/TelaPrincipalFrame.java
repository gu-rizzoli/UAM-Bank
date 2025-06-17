package bancopix;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipalFrame extends JFrame {

    private Usuario usuario;
    private JLabel lblBemVindo, lblSaldo;
    private JButton btnCadastrarChave, btnTransferirPix, btnSair;

    public TelaPrincipalFrame(Usuario usuario) {
        this.usuario = usuario;

        setTitle("Banco Pix - Tela Principal");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 5, 5));

        lblBemVindo = new JLabel("Bem-vindo, " + usuario.getNome(), SwingConstants.CENTER);
        add(lblBemVindo);

        lblSaldo = new JLabel("Saldo: R$ " + String.format("%.2f", buscarSaldo()), SwingConstants.CENTER);
        add(lblSaldo);

        btnCadastrarChave = new JButton("Cadastrar Chave Pix");
        btnCadastrarChave.setBackground(new Color(0x17FF0F));
        btnCadastrarChave.setForeground(Color.WHITE);
        btnCadastrarChave.setFocusPainted(false);
        btnCadastrarChave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCadastrarChave.setBackground(new Color(0, 123, 255));
        btnCadastrarChave.setForeground(Color.WHITE);
        btnCadastrarChave.setFocusPainted(false);
        btnCadastrarChave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTransferirPix = new JButton("Transferir via Pix");
        btnTransferirPix.setBackground(new Color(40, 167, 69));
        btnTransferirPix.setForeground(Color.WHITE);
        btnTransferirPix.setFocusPainted(false);
        btnTransferirPix.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSair = new JButton("Sair");
        btnSair.setBackground(new Color(0xDC3545));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFocusPainted(false);
        btnSair.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSair.setBackground(new Color(220, 53, 69));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFocusPainted(false);
        btnSair.setCursor(new Cursor(Cursor.HAND_CURSOR));

        add(btnCadastrarChave);
        add(btnTransferirPix);
        add(btnSair);

        btnCadastrarChave.addActionListener(e -> {
            new CadastroChaveFrame(usuario).setVisible(true);
            dispose();
        });

        btnTransferirPix.addActionListener(e -> {
            new TransferenciaPixFrame(usuario).setVisible(true);
            dispose();
        });

        btnSair.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }

    private double buscarSaldo() {
        ContaDAO dao = new ContaDAO();
        return dao.obterSaldo(usuario.getId());
    }
}
