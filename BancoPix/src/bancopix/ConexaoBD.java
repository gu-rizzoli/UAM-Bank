package bancopix;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {
    // Credenciais lidas de variáveis de ambiente (nunca versionar senhas).
    private static final String URL = env("UAM_BANK_DB_URL", "jdbc:mysql://localhost:3306/uam_bank");
    private static final String USUARIO = env("UAM_BANK_DB_USER", "root");
    private static final String SENHA = env("UAM_BANK_DB_PASSWORD", "");

    private static String env(String nome, String padrao) {
        String valor = System.getenv(nome);
        return valor != null ? valor : padrao;
    }

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
