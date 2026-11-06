create table usuario
(
    id            uuid primary key,
    nome          varchar(120) not null,
    email         varchar(160),
    login         varchar(60)  not null,
    senha_hash    varchar(100) not null,
    perfil        varchar(20)  not null,
    bloqueado_em  timestamptz,
    criado_em     timestamptz  not null,
    atualizado_em timestamptz,
    constraint uk_usuario_login unique (login),
    constraint ck_usuario_perfil check (perfil in ('ADMIN', 'PROFESSOR', 'ALUNO'))
);

create table empresa
(
    id               uuid primary key,
    cnpj             varchar(14)   not null,
    nome             varchar(160)  not null,
    turno_minutos    integer       not null,
    fator_velocidade numeric(6, 3) not null,
    nivel_confianca  numeric(5, 4) not null,
    bloqueado_em     timestamptz,
    criado_em        timestamptz   not null,
    atualizado_em    timestamptz,
    constraint uk_empresa_cnpj unique (cnpj),
    constraint ck_empresa_turno check (turno_minutos between 1 and 1440),
    constraint ck_empresa_fator check (fator_velocidade > 0),
    constraint ck_empresa_confianca check (nivel_confianca > 0 and nivel_confianca < 1)
);

create table empresa_parada
(
    empresa_id uuid         not null references empresa (id) on delete cascade,
    ordem      integer      not null,
    descricao  varchar(120) not null,
    minutos    integer      not null check (minutos > 0),
    primary key (empresa_id, ordem)
);

create table produto
(
    id            uuid primary key,
    empresa_id    uuid         not null references empresa (id),
    codigo        varchar(40)  not null,
    nome          varchar(160) not null,
    bloqueado_em  timestamptz,
    criado_em     timestamptz  not null,
    atualizado_em timestamptz,
    constraint uk_produto_empresa_codigo unique (empresa_id, codigo)
);

create table produto_operacao
(
    produto_id            uuid         not null references produto (id) on delete cascade,
    ordem                 integer      not null,
    nome                  varchar(120) not null,
    descricao             varchar(255),
    quantidade_operadores integer      not null check (quantidade_operadores > 0),
    primary key (produto_id, ordem)
);

create table cronoanalise
(
    id               uuid primary key,
    criado_por       uuid          not null references usuario (id),
    criado_em        timestamptz   not null,
    atualizado_em    timestamptz,
    cronoanalista    varchar(120)  not null,
    erro_relativo    numeric(5, 4) not null,
    nivel_confianca  numeric(5, 4) not null,
    fator_velocidade numeric(6, 3) not null,
    turno_minutos    integer       not null,
    produto_id       uuid          references produto (id) on delete set null,
    produto_codigo   varchar(40)   not null,
    produto_nome     varchar(160)  not null,
    empresa_nome     varchar(160)  not null,
    empresa_cnpj     varchar(14)   not null
);

create index idx_cronoanalise_criado_em on cronoanalise (criado_em desc);

create table cronoanalise_parada
(
    cronoanalise_id uuid         not null references cronoanalise (id) on delete cascade,
    ordem           integer      not null,
    descricao       varchar(120) not null,
    minutos         integer      not null,
    primary key (cronoanalise_id, ordem)
);

create table cronoanalise_operacao
(
    id                    uuid primary key,
    cronoanalise_id       uuid         not null references cronoanalise (id) on delete cascade,
    ordem                 integer      not null,
    nome                  varchar(120) not null,
    quantidade_operadores integer      not null,
    constraint uk_cronoanalise_operacao_ordem unique (cronoanalise_id, ordem)
);

create table cronoanalise_observacao
(
    operacao_id uuid           not null references cronoanalise_operacao (id) on delete cascade,
    ciclo       integer        not null,
    segundos    numeric(10, 3) not null check (segundos > 0),
    primary key (operacao_id, ciclo)
);
