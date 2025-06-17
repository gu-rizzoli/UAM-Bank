package bancopix;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TransferenciaPixFrame extends JFrame {

    private JTextField txtChavePixDestino, txtValor;
    private JButton btnTransferir, btnVoltar;
    private Usuario usuarioLogado;

    public TransferenciaPixFrame(Usuario usuario) {
        this.usuarioLogado = usuario;

        setTitle("Transferência PIX");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(new JLabel("Chave PIX destino (CPF ou email):"));
        txtChavePixDestino = new JTextField();
        add(txtChavePixDestino);

        add(new JLabel("Valor a transferir:"));
        txtValor = new JTextField();
        add(txtValor);

        btnTransferir = new JButton("Transferir");
        btnVoltar = new JButton("Voltar");

        btnTransferir.setBackground(new Color(0x28A745));
        btnTransferir.setForeground(Color.WHITE);
        btnTransferir.setFocusPainted(false);
        btnTransferir.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnVoltar.setBackground(new Color(0xFF6666));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFocusPainted(false);
        btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnVoltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new TelaPrincipalFrame(usuarioLogado).setVisible(true);
            }
        });

        add(btnTransferir);
        add(btnVoltar);

        btnTransferir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String chaveDestino = txtChavePixDestino.getText().trim();
                String valorStr = txtValor.getText().trim();

                if (chaveDestino.isEmpty() || valorStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
                    return;
                }

                double valor;
                try {
                    valor = Double.parseDouble(valorStr);
                    if (valor <= 0) {
                        JOptionPane.showMessageDialog(null, "Valor deve ser maior que zero.");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Digite um valor numérico válido.");
                    return;
                }

                TransacaoPixDAO dao = new TransacaoPixDAO();
                boolean sucesso = dao.realizarTransferenciaPix(usuarioLogado.getId(), chaveDestino, valor);

                if (sucesso) {
                    JOptionPane.showMessageDialog(null, "Transferência realizada com sucesso!");
                    txtChavePixDestino.setText("");
                    txtValor.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao realizar transferência.");
                }
            }
        });
    }
}
