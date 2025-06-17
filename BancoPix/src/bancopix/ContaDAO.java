package bancopix;

import java.sql.*;

public class ContaDAO {

    public void criarContaParaUsuario(int idUsuario) {
        String sql = "INSERT INTO conta (id_usuario, numero_conta, saldo) VALUES (?, ?, 0.00)";
        String numeroConta = String.format("%08d", (int)(Math.random() * 100_000_000));

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setString(2, numeroConta);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao criar conta: " + e.getMessage());
        }
    }

    public double obterSaldo(int idUsuario) {
        String sql = "SELECT saldo FROM conta WHERE id_usuario = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("saldo");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter saldo: " + e.getMessage());
        }
        return 0.0;
    }

    public void atualizarSaldo(int idUsuario, double novoSaldo) {
        String sql = "UPDATE conta SET saldo = ? WHERE id_usuario = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, novoSaldo);
            stmt.setInt(2, idUsuario);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar saldo: " + e.getMessage());
        }
    }

    public int buscarIdUsuarioPorChavePix(String chavePix) {
        String sql = "SELECT u.id FROM usuario u JOIN chave_pix c ON u.id = c.id_usuario WHERE c.chave = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, chavePix);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuário pela chave PIX: " + e.getMessage());
        }

        return -1;
    }
}
