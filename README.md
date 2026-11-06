# cronoanalise

API REST de **cronoanálise industrial**, criada no curso de Análise e Desenvolvimento de Sistemas do SENAI e modernizada depois. A partir dos tempos cronometrados de cada operação de um produto, a API calcula os indicadores usados para medir a capacidade de produção.

O front-end que consome esta API é o [cronoanalise-client](https://github.com/Jean8791/cronoanalise-client).

> Projeto acadêmico, para fins de estudo. A versão 2.0 reescreve a versão original (Spring Boot 2.0 e Java 8, de 2019) mantendo o domínio e corrigindo os problemas de segurança e de cálculo listados em [O que mudou na versão 2.0](#o-que-mudou-na-versão-20).

## O cálculo

Para cada cronoanálise, com **n** ciclos observados em cada operação:

| Indicador | Fórmula |
|---|---|
| Tempo médio da operação | média dos tempos observados da operação |
| Tempo de cada ciclo | soma das operações naquele ciclo |
| Tempo médio do ciclo (x̄) | média dos tempos de ciclo |
| Amplitude (R) | maior tempo de ciclo − menor tempo de ciclo |
| Ciclos necessários (N) | N = ( z · R / (Er · d2 · x̄) )², arredondado para cima |
| Tempo normal (TN) | TN = x̄ · fator de velocidade |
| Fator de tolerância (FT) | FT = turno / (turno − paradas) |
| Tempo padrão (TP) | TP = TN · FT |
| Peças por hora | 3600 / TP |
| Peças por turno | turno em segundos / TP |

- **z** vem do nível de confiança da empresa pela distribuição normal (bilateral): 90% → 1,64; 95% → 1,96; 99% → 2,58.
- **d2** é o coeficiente da amplitude para n observações (tabela padrão de 2 a 100).
- **Er** é o erro relativo aceito (ex.: 0,05 = 5%).
- A resposta indica se a **amostra é suficiente** (n ≥ N) ou quantos ciclos ainda faltam cronometrar.

O cálculo fica isolado em [`CalculadoraCronoanalise`](src/main/java/br/com/senai/cronoanalise/calculo/CalculadoraCronoanalise.java), sem dependência de Spring nem de banco, e é coberto por testes com um exemplo resolvido à mão.

A cronoanálise guarda uma **cópia** dos dados da empresa (turno, paradas, fator de velocidade, nível de confiança) e do produto no momento da medição: alterar o cadastro depois não muda o resultado de análises antigas.

## Perfis

| Perfil | Pode |
|---|---|
| `ADMIN` | tudo, inclusive gerenciar usuários |
| `PROFESSOR` | cadastrar e bloquear empresas e produtos; corrigir e excluir qualquer cronoanálise |
| `ALUNO` | consultar empresas e produtos; registrar cronoanálises e corrigir ou excluir as próprias |

## Stack

- **Java 21** e **Spring Boot 3.5** (Web, Data JPA, Validation, Security, OAuth2 Resource Server)
- **JWT** HS256 assinado e validado pelo Spring Security (Nimbus)
- **PostgreSQL 16** com migrations no **Flyway** (`ddl-auto=validate`)
- **OpenAPI/Swagger** com springdoc
- Testes com **JUnit 5**, **AssertJ**, **MockMvc** e **Testcontainers**

## Estrutura

```
src/main/java/br/com/senai/cronoanalise/
├── calculo/        cálculo puro: médias, R, d2, z, N, TN, FT, TP e capacidade
├── cronoanalise/   registro dos tempos, cópia dos parâmetros e resultado
├── empresa/        empresa, turno e paradas
├── produto/        produto e operações em ordem
├── usuario/        usuários e perfis
├── auth/           login, JWT, filtros de usuário ativo e de tentativas
├── bootstrap/      administrador inicial e dados de demonstração
└── comum/          erros padronizados (Problem Details), CNPJ, configurações
```

## Endpoints

| Recurso | Rotas |
|---|---|
| Autenticação | `POST /api/auth/login` · `GET /api/auth/eu` |
| Usuários (ADMIN) | `GET/POST /api/usuarios` · `GET/PUT /api/usuarios/{id}` · `POST /api/usuarios/{id}/bloquear` · `/desbloquear` |
| Empresas | `GET/POST /api/empresas` · `GET/PUT /api/empresas/{id}` · `POST /api/empresas/{id}/bloquear` · `/desbloquear` |
| Produtos | `GET/POST /api/produtos` · `GET/PUT /api/produtos/{id}` · `POST /api/produtos/{id}/bloquear` · `/desbloquear` |
| Cronoanálises | `GET/POST /api/cronoanalises` · `GET/PUT/DELETE /api/cronoanalises/{id}` |

A documentação interativa fica em `/swagger-ui.html` (a raiz da API redireciona para ela).

## Como rodar

Pré-requisitos: JDK 21 e Docker.

```bash
docker compose up -d          # PostgreSQL local na porta 5434

export DB_PORT=5434
export JWT_SECRET=$(openssl rand -base64 48)
export CRONO_ADMIN_LOGIN=admin CRONO_ADMIN_SENHA=uma-senha-forte
export CRONO_DEMO_LOGIN=visitante CRONO_DEMO_SENHA=visitante123   # opcional: cria dados de exemplo
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`.

### Variáveis de ambiente

| Variável | Uso |
|---|---|
| `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`, `DB_SSLMODE` | Conexão com o PostgreSQL |
| `JWT_SECRET` | Segredo do JWT (mínimo de 32 caracteres) |
| `JWT_VALIDADE` | Validade do token (padrão `8h`) |
| `CRONO_ADMIN_LOGIN`, `CRONO_ADMIN_SENHA` | Cria o primeiro administrador, se ainda não existir |
| `CRONO_DEMO_LOGIN`, `CRONO_DEMO_SENHA` | Conta de demonstração (perfil ALUNO, protegida contra alteração) e dados de exemplo |
| `CORS_ORIGINS` | Origens liberadas para o front (padrão `http://localhost:5173`) |
| `CRONO_CABECALHO_IP` | Cabeçalho com o IP real atrás de proxy, para o limite de tentativas de login |
| `CRONO_FORWARD_HEADERS` | `framework` quando a API roda atrás de proxy com TLS |

### Testes

```bash
./mvnw test
```

Os testes de integração sobem um PostgreSQL descartável com Testcontainers (precisam do Docker).

## O que mudou na versão 2.0

**Segurança**
- Senhas passaram a ser gravadas com **BCrypt**; antes eram texto puro e comparadas direto no banco.
- As respostas de usuário **não trazem mais a senha** (antes a listagem devolvia a senha de todos).
- O login deixou de guardar dados num campo compartilhado entre requisições, o que podia **trocar o token entre usuários** em logins simultâneos.
- Removido o endpoint que recebia **login e senha na URL**.
- **Perfis aplicados nos endpoints** (antes qualquer usuário logado podia criar e bloquear usuários).
- Usuário **bloqueado** não entra e tem o token recusado na hora; token inválido responde 401 em vez de 500.
- **Limite de tentativas** de login por IP.

**Cálculo**
- O fator de tolerância usa o **turno cadastrado** da empresa (antes era fixo em 480 minutos).
- O **z** passou a vir do **nível de confiança**; antes era tirado do fator de velocidade, misturando duas grandezas diferentes.
- Validações de entrada (mesmo número de observações em todas as operações, tempos positivos, paradas menores que o turno) e indicação de **amostra suficiente**.
- Valores com `BigDecimal` e tabelas de coeficientes no código, no lugar de ~2.400 linhas que gravavam a tabela da normal no banco a cada inicialização.

**Base**
- Java 8 → 21, Spring Boot 2.0 → 3.5, springfox → springdoc, MySQL com `ddl-auto=update` → PostgreSQL com Flyway.
- Testes unitários do cálculo e testes de integração dos fluxos de segurança.
