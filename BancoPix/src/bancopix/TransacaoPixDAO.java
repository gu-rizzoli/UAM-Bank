package bancopix;

import java.math.BigDecimal;
import java.sql.*;

public class TransacaoPixDAO {

    public enum Resultado {
        SUCESSO,
        CHAVE_NAO_ENCONTRADA,
        MESMA_CONTA,
        SALDO_INSUFICIENTE,
        ERRO
    }

    /**
     * Transfere via Pix numa única transação: ou débito, crédito e registro acontecem
     * juntos, ou nada acontece. As duas contas ficam travadas (SELECT ... FOR UPDATE)
     * até o commit, para duas transferências simultâneas não usarem o mesmo saldo.
     */
    public Resultado realizarTransferenciaPix(int idRemetente, String chaveDestino, BigDecimal valor) {
        if (valor == null || valor.signum() <= 0) {
            return Resultado.ERRO;
        }

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false);
            try {
                Integer idDestinatario = buscarDonoDaChave(conn, chaveDestino);
                if (idDestinatario == null) {
                    conn.rollback();
                    return Resultado.CHAVE_NAO_ENCONTRADA;
                }
                if (idDestinatario == idRemetente) {
                    conn.rollback();
                    return Resultado.MESMA_CONTA;
                }

                // Trava sempre na mesma ordem (menor id primeiro) para evitar deadlock.
                travarConta(conn, Math.min(idRemetente, idDestinatario));
                travarConta(conn, Math.max(idRemetente, idDestinatario));

                // O débito só acontece se houver saldo: a condição fica no próprio UPDATE.
                if (!debitar(conn, idRemetente, valor)) {
                    conn.rollback();
                    return Resultado.SALDO_INSUFICIENTE;
                }
                creditar(conn, idDestinatario, valor);
                registrarTransferencia(conn, idRemetente, idDestinatario, valor);

                conn.commit();
                return Resultado.SUCESSO;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao realizar transferência Pix: " + e.getMessage());
            return Resultado.ERRO;
        }
    }

    private Integer buscarDonoDaChave(Connection conn, String chave) throws SQLException {
        String sql = "SELECT id_usuario FROM chave_pix WHERE chave = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, chave);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt("id_usuario") : null;
            }
        }
    }

    private void travarConta(Connection conn, int idUsuario) throws SQLException {
        String sql = "SELECT id FROM conta WHERE id_usuario = ? FOR UPDATE";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Conta não encontrada para o usuário " + idUsuario);
                }
            }
        }
    }

    private boolean debitar(Connection conn, int idUsuario, BigDecimal valor) throws SQLException {
        String sql = "UPDATE conta SET saldo = saldo - ? WHERE id_usuario = ? AND saldo >= ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, valor);
            stmt.setInt(2, idUsuario);
            stmt.setBigDecimal(3, valor);
            return stmt.executeUpdate() == 1;
        }
    }

    private void creditar(Connection conn, int idUsuario, BigDecimal valor) throws SQLException {
        String sql = "UPDATE conta SET saldo = saldo + ? WHERE id_usuario = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, valor);
            stmt.setInt(2, idUsuario);
            if (stmt.executeUpdate() != 1) {
                throw new SQLException("Conta de destino não encontrada");
            }
        }
    }

    private void registrarTransferencia(Connection conn, int idRemetente, int idDestinatario, BigDecimal valor)
            throws SQLException {
        String sql = "INSERT INTO transacao_pix (remetente_id, destinatario_id, valor) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idRemetente);
            stmt.setInt(2, idDestinatario);
            stmt.setBigDecimal(3, valor);
            stmt.executeUpdate();
        }
    }
}
