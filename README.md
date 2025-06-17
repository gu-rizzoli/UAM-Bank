# UAM Bank

Sistema bancário de desktop com transferências via Pix, feito em Java (Swing) com banco de dados MySQL. Projeto acadêmico do curso de Ciência da Computação da Universidade Anhembi Morumbi (2025), desenvolvido integralmente por mim.

## O que faz

- Cadastro de usuário com abertura automática de conta (agência e número)
- Login com e-mail e senha
- Consulta de saldo
- Cadastro e remoção de chaves Pix (CPF ou e-mail)
- Transferência via Pix entre contas, com validação de chave e de saldo e registro da transação

## Tecnologias

Java · Swing · MySQL · JDBC · NetBeans (Ant)

## Organização do código

O código fica em `BancoPix/src/bancopix` e separa interface de acesso a dados:

- **Telas (Swing):** `LoginFrame`, `CadastroFrame`, `TelaPrincipalFrame`, `CadastroChaveFrame`, `TransferenciaPixFrame`
- **Modelos:** `Usuario`, `ChavePix`
- **Acesso a dados (padrão DAO):** `UsuarioDAO`, `ContaDAO`, `ChavePixDAO`, `TransacaoPixDAO`, todos com `PreparedStatement`
- **Conexão:** `ConexaoBD`, que lê as credenciais de variáveis de ambiente

O script do banco está em [`uam_bank.sql`](uam_bank.sql), com as tabelas `usuario`, `conta`, `chave_pix`, `transacao_pix` e `denuncia_chave`.

## Como rodar

1. Crie o banco executando `uam_bank.sql` no MySQL.
2. Defina as variáveis de ambiente com os dados do seu MySQL:
   - `UAM_BANK_DB_URL` (padrão: `jdbc:mysql://localhost:3306/uam_bank`)
   - `UAM_BANK_DB_USER` (padrão: `root`)
   - `UAM_BANK_DB_PASSWORD`
3. Abra a pasta `BancoPix` no NetBeans, adicione o [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) às bibliotecas do projeto e execute `Main`.

## Limitações conhecidas e próximos passos

Sendo um projeto de estudo, ele tem pontos que eu faria diferente hoje:

- As senhas são guardadas em texto puro. O certo é armazenar um hash (por exemplo, BCrypt ou Argon2).
- A transferência atualiza os dois saldos em comandos separados. Ela deveria rodar numa única transação (`setAutoCommit(false)` e `commit`/`rollback`) para não deixar saldo inconsistente se algo falhar no meio.
- Os valores usam `double`. Para dinheiro, `BigDecimal` evita erros de arredondamento.
- A tabela `denuncia_chave` existe no banco, mas a denúncia de chave ainda não foi implementada na interface.
- Não há testes automatizados.
