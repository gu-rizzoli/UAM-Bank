package bancopix;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.*;

public class ContaDAO {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int TENTATIVAS_NUMERO_CONTA = 5;

    /**
     * Abre a conta do usuário usando a conexão (e a transação) de quem chamou.
     * Se o número sorteado já existir, sorteia outro.
     */
    public void criarContaParaUsuario(Connection conn, int idUsuario) throws SQLException {
        String sql = "INSERT INTO conta (id_usuario, numero_conta, saldo) VALUES (?, ?, 0.00)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int tentativa = 1; ; tentativa++) {
                stmt.setInt(1, idUsuario);
                stmt.setString(2, String.format("%08d", RANDOM.nextInt(100_000_000)));
                Savepoint antes = conn.setSavepoint();
                try {
                    stmt.executeUpdate();
                    return;
                } catch (SQLIntegrityConstraintViolationException e) {
                    conn.rollback(antes);
                    if (tentativa == TENTATIVAS_NUMERO_CONTA) {
                        throw e;
                    }
                }
            }
        }
    }

    public BigDecimal obterSaldo(int idUsuario) {
        String sql = "SELECT saldo FROM conta WHERE id_usuario = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("saldo");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter saldo: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }
}
