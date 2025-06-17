package bancopix;

import java.sql.*;

public class TransacaoPixDAO {

    public boolean realizarTransferenciaPix(int idRemetente, String chaveDestino, double valor) {
        ContaDAO contaDAO = new ContaDAO();
        int idDestinatario = contaDAO.buscarIdUsuarioPorChavePix(chaveDestino);

        if (idDestinatario == -1 || idRemetente == idDestinatario) {
            return false;
        }

        double saldoRemetente = contaDAO.obterSaldo(idRemetente);
        if (saldoRemetente < valor) {
            return false;
        }

        double saldoDestinatario = contaDAO.obterSaldo(idDestinatario);

        // Atualizar saldos
        contaDAO.atualizarSaldo(idRemetente, saldoRemetente - valor);
        contaDAO.atualizarSaldo(idDestinatario, saldoDestinatario + valor);

        // Registrar transferência
        return registrarTransferencia(idRemetente, idDestinatario, valor);
    }

    public boolean registrarTransferencia(int idRemetente, int idDestinatario, double valor) {
        String sql = "INSERT INTO transacao_pix (remetente_id, destinatario_id, valor) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRemetente);
            stmt.setInt(2, idDestinatario);
            stmt.setDouble(3, valor);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao registrar transferência PIX: " + e.getMessage());
            return false;
        }
    }
}
