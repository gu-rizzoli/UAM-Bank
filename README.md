# UAM Bank

Sistema bancário de desktop com transferências via Pix, feito em Java (Swing) com banco de dados MySQL. Projeto acadêmico do curso de Ciência da Computação da Universidade Anhembi Morumbi (2025), desenvolvido integralmente por mim.

## O que faz

- Cadastro de usuário com abertura automática de conta (agência e número)
- Login com e-mail e senha (guardada com hash)
- Consulta de saldo
- Cadastro e remoção de chaves Pix (CPF ou e-mail)
- Transferência via Pix entre contas, numa transação atômica, com validação de chave e de saldo e registro da transação

## Tecnologias

Java · Swing · MySQL · JDBC · NetBeans (Ant)

## Organização do código

O código fica em `BancoPix/src/bancopix` e separa interface de acesso a dados:

- **Telas (Swing):** `LoginFrame`, `CadastroFrame`, `TelaPrincipalFrame`, `CadastroChaveFrame`, `TransferenciaPixFrame`
- **Modelos:** `Usuario`, `ChavePix`
- **Acesso a dados (padrão DAO):** `UsuarioDAO`, `ContaDAO`, `ChavePixDAO`, `TransacaoPixDAO`, todos com `PreparedStatement`
- **Conexão:** `ConexaoBD`, que lê as credenciais de variáveis de ambiente
- **Segurança:** `SenhaUtil`, que gera e confere o hash das senhas

O script do banco está em [`uam_bank.sql`](uam_bank.sql), com as tabelas `usuario`, `conta`, `chave_pix`, `transacao_pix` e `denuncia_chave`.

## Como rodar

1. Crie o banco executando `uam_bank.sql` no MySQL.
2. Defina as variáveis de ambiente com os dados do seu MySQL:
   - `UAM_BANK_DB_URL` (padrão: `jdbc:mysql://localhost:3306/uam_bank`)
   - `UAM_BANK_DB_USER` (padrão: `root`)
   - `UAM_BANK_DB_PASSWORD`
3. Abra a pasta `BancoPix` no NetBeans, adicione o [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) às bibliotecas do projeto e execute `Main`.

## Revisão de segurança e consistência (nov/2026)

Depois da entrega, o projeto passou por uma revisão:

- **Senhas com hash:** as senhas são guardadas com PBKDF2-HMAC-SHA256 (210 mil iterações, salt aleatório por usuário), em `SenhaUtil`, sem dependências externas. O login compara em tempo constante e leva o mesmo tempo para e-mail inexistente e senha errada.
- **Transferência atômica:** débito, crédito e registro acontecem numa única transação, com as duas contas travadas (`SELECT ... FOR UPDATE`, sempre na mesma ordem para evitar deadlock). O débito só ocorre se o próprio `UPDATE` encontrar saldo suficiente, então duas transferências simultâneas não gastam o mesmo dinheiro.
- **Dinheiro em `BigDecimal`:** nada de `double` para valores; a tela aceita `10,50` ou `10.50` e no máximo duas casas.
- **Cadastro consistente:** usuário e conta são criados na mesma transação.
- **Banco protegido por regras:** `CHECK (saldo >= 0)`, `CHECK (valor > 0)` e uma conta por usuário.
- **Mensagens claras:** a transferência informa se a chave não existe, se o saldo é insuficiente ou se o destino é a própria conta.
- **Credenciais fora do código:** a conexão lê usuário e senha do MySQL de variáveis de ambiente.

## Próximos passos

- A tabela `denuncia_chave` existe no banco, mas a denúncia de chave ainda não foi implementada na interface.
- Não há testes automatizados no projeto.
- Validar formato de CPF e e-mail no cadastro.
