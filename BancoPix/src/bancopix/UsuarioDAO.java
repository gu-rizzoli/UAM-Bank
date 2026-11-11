package bancopix;

import java.sql.*;

public class UsuarioDAO {

    // Hash calculado uma vez, usado quando o e-mail não existe para o login levar o
    // mesmo tempo nos dois casos e não revelar quais e-mails estão cadastrados.
    private static final String HASH_FICTICIO = SenhaUtil.gerarHash("senha-ficticia");

    /** Cadastra o usuário e abre a conta dele na mesma transação. */
    public boolean cadastrar(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, cpf, email, senha) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, usuario.getNome());
                stmt.setString(2, usuario.getCpf());
                stmt.setString(3, usuario.getEmail());
                stmt.setString(4, SenhaUtil.gerarHash(usuario.getSenha()));
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }
                    usuario.setId(rs.getInt(1));
                }

                new ContaDAO().criarContaParaUsuario(conn, usuario.getId());
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                usuario.setSenha(null); // não manter a senha em memória depois do cadastro
            }
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
            return false;
        }
    }

    public Usuario login(String email, String senha) {
        String sql = "SELECT id, nome, cpf, email, senha FROM usuario WHERE email = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    SenhaUtil.conferir(senha, HASH_FICTICIO);
                    return null;
                }
                if (!SenhaUtil.conferir(senha, rs.getString("senha"))) {
                    return null;
                }
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setCpf(rs.getString("cpf"));
                usuario.setEmail(rs.getString("email"));
                return usuario; // o hash da senha não sai do DAO
            }
        } catch (SQLException e) {
            System.out.println("Erro ao fazer login: " + e.getMessage());
            return null;
        }
    }
}
