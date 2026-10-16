# cronoanalise

API REST de **cronoanálise industrial**, desenvolvida no curso de Análise e Desenvolvimento de Sistemas do SENAI. A partir dos tempos cronometrados de cada operação de um produto, a API calcula os indicadores usados para medir a capacidade de produção.

O front-end que consome esta API é o [cronoanalise-client](https://github.com/Jean8791/cronoanalise-client).

## O que a API calcula

Para cada cronoanálise:

- **média dos tempos** observados por operação e no total;
- **número de ciclos ideal** a cronometrar, a partir do erro relativo, do fator de velocidade (tabela de distribuição normal) e do coeficiente d2;
- **tempo normal** e **tempo padrão**, aplicando o fator de tolerância (FT);
- **paradas** previstas da empresa no turno;
- **peças por hora** e **peças por turno**.

## Funcionalidades

- Cadastro de **empresas** (com turno, fator de velocidade e paradas), **produtos**, **operações**, **máquinas** e **usuários**.
- Registro e detalhamento de **cronoanálises**.
- Bloqueio de empresas, produtos e usuários.
- Endpoints de busca rápida ("zoom") de empresas, máquinas e produtos para os formulários.
- **Autenticação com JWT** via Spring Security.
- Documentação da API com **Swagger**.

## Stack

- Java 8 e Spring Boot 2.0 (Web, Data JPA, Security)
- JWT (jjwt)
- MySQL (com suporte a PostgreSQL)
- Swagger (springfox)
- Maven

## Estrutura

```
src/main/java/br/com/senai/cronoanalise/
├── resources/      controllers REST (/api/...)
├── service/        regras de negócio e cálculo (CalculoService)
├── repository/     acesso a dados com Spring Data JPA
├── dto/            objetos de entrada e saída
├── autenticacao/   JWT e configuração do Spring Security
└── config/         Swagger
```

## Como rodar

Pré-requisitos: JDK 8 e um MySQL local com o banco `cronoanalise`.

```bash
export DB_PASSWORD=sua_senha
export JWT_SECRET=$(openssl rand -base64 48)
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080/api` e a documentação fica em `http://localhost:8080/swagger-ui.html`.
