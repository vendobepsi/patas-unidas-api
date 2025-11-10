-- Flyway Migration V1
-- Descricao: Criacao inicial das tabelas 'user' e 'animal_profile' e suas sequencias.

----------------------------------------------------
-- 1. Criação da Tabela USER e sua Sequência
----------------------------------------------------

-- Criação da sequência para o ID do Usuário (conforme 'user_id_seq' na anotação @SequenceGenerator)
CREATE SEQUENCE app_user_id_seq START WITH 1 INCREMENT BY 1;

-- Criação da tabela de Usuários
CREATE TABLE app_user (
    -- ID e Chave Primária, usando a sequência
    id BIGINT PRIMARY KEY DEFAULT nextval('app_user_id_seq'),
    
    -- Dados de Autenticação e Perfil
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(60) NOT NULL, -- Armazena o hash BCrypt (60 caracteres)
    
    -- Dados de Localização e Tipo
    city VARCHAR(255),
    state VARCHAR(255),
    
    -- Enumeração (Armazenada como String)
    user_type VARCHAR(50) NOT NULL, -- Baseado no enum UserType (COMMON, PROTECTOR_ONG, ADOPTER)

    -- Campos Booleanos (armazenados como BOOLEAN)
    is_verified_protector BOOLEAN NOT NULL DEFAULT FALSE,
    has_other_pets BOOLEAN NOT NULL DEFAULT FALSE,
    
    -- Outros detalhes
    profile_picture_url VARCHAR(512),
    housing_type VARCHAR(100)
);

----------------------------------------------------
-- 2. Criação da Tabela ANIMAL_PROFILE e sua Sequência
----------------------------------------------------

-- Criação da sequência para o ID do Perfil Animal (conforme 'animal_codigo_seq' na anotação @SequenceGenerator)
CREATE SEQUENCE animal_codigo_seq START WITH 1 INCREMENT BY 1;

-- Criação da tabela de Perfis de Animais
CREATE TABLE animal_profile (
    -- ID e Chave Primária, usando a sequência
    id BIGINT PRIMARY KEY DEFAULT nextval('animal_codigo_seq'),
    
    -- Chaves Estrangeiras para Usuários
    -- Referencia o usuário que criou o perfil
    created_by_user_id BIGINT REFERENCES "app_user" (id), 
    -- Referencia o usuário que está gerenciando o perfil
    managed_by_user_id BIGINT REFERENCES "app_user" (id),
    
    -- Dados do Perfil
    provisional_name VARCHAR(255) NOT NULL,
    description TEXT, -- Usamos TEXT para descrições longas
    approximate_age VARCHAR(50), -- Mantido como String conforme sua Entidade
    
    -- Enumerações (Armazenadas como String)
    sex VARCHAR(20) NOT NULL, -- Baseado em AnimalSex (MALE, FEMALE, UNKNOWN)
    size VARCHAR(20) NOT NULL, -- Baseado em AnimalSize (SMALL, MEDIUM, LARGE)
    status VARCHAR(50) NOT NULL, -- Baseado em AnimalStatus

    -- Localização (Composição: GeoLocation)
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    
    -- Timestamp (Long no Java, armazenado como Bigint no DB)
    created_at BIGINT NOT NULL, 
    
    -- Fotos (Composição: ArrayList<String> no Java)
    -- JSONB é o melhor tipo para armazenar arrays/listas no PostgreSQL
    photos JSONB
);

-- FIM DA MIGRATION V1