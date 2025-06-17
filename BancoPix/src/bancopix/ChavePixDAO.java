package bancopix;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChavePixDAO {

    public boolean chaveJaExiste(String chave) {
        String sql = "SELECT * FROM chave_pix WHERE chave = ?";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, chave);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true se já existe
        } catch (SQLException e) {
            System.out.println("Erro ao verificar chave Pix: " + e.getMessage());
            return true;
        }
    }

    public boolean cadastrarChave(ChavePix chave) {
        if (chaveJaExiste(chave.getChave())) {
            return false;
        }

        String sql = "INSERT INTO chave_pix (id_usuario, tipo, chave) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, chave.getUsuarioId());
            stmt.setString(2, chave.getTipo());
            stmt.setString(3, chave.getChave());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar chave Pix: " + e.getMessage());
            return false;
        }
    }

    public boolean removerChave(int usuarioId, String chave) {
        String sql = "DELETE FROM chave_pix WHERE id_usuario = ? AND chave = ?";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            stmt.setString(2, chave);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao remover chave Pix: " + e.getMessage());
            return false;
        }
    }
}
