----------------------------------------------------
-- 1. Alteração na Tabela USER (Adicionar Telefone)
----------------------------------------------------
-- Adiciona a coluna phone (VARCHAR para aceitar formatação (11) 99999...)
-- Deixamos NULL por enquanto para não quebrar usuários que já existem
ALTER TABLE app_user ADD COLUMN phone VARCHAR(20);


----------------------------------------------------
-- 2. Criação da Tabela para Imagens (Base64)
----------------------------------------------------
-- Esta tabela resolve o problema do Render apagar arquivos locais.
-- O "nome do arquivo" (UUID) será a chave primária.

CREATE TABLE image_store (
    -- O UUID String serve como ID único e nome do arquivo
    file_name VARCHAR(255) PRIMARY KEY, 
    
    -- O conteúdo Base64 pode ser GIGANTE, então usamos TEXT (O Postgres lida muito bem com TEXT grande)
    base64_data TEXT NOT NULL
    
);