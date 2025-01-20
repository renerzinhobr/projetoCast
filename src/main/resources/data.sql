


CREATE TABLE usuario (
    IDUSUARIO INT AUTO_INCREMENT PRIMARY KEY,
    ATIVO TINYINT NOT NULL,
    LOGIN VARCHAR(45) NOT NULL UNIQUE,
    SENHA VARCHAR(200) NOT NULL,
    PERFIL VARCHAR(20) NOT NULL,
    NOME VARCHAR(100) NOT NULL,
    CPFCNPJ VARCHAR(45) NOT NULL,
    EMAIL VARCHAR(100),
    DATAHORAINCLUSAO DATETIME NOT NULL
);


INSERT INTO usuario (
    ATIVO,
    LOGIN,
    SENHA,
    PERFIL,
    NOME,
    CPFCNPJ,
    EMAIL,
    DATAHORAINCLUSAO
) VALUES (
    1,
    'rener',
    '$2a$10$GD9uBFzCctslvG4TzzxvL.W8YrMOD4hZmrjhrnNyzkPsqz8Qx36ka',
    'CORRENTISTA',
    'Rener Ferreira Lemos',
    '12345678900',
    'rener.lemos@example.com',
    NOW()
);

INSERT INTO usuario (
    ATIVO,
    LOGIN,
    SENHA,
    PERFIL,
    NOME,
    CPFCNPJ,
    EMAIL,
    DATAHORAINCLUSAO
) VALUES (
    1, -- Ativo
    'admin', -- Login
    '$2a$10$GD9uBFzCctslvG4TzzxvL.W8YrMOD4hZmrjhrnNyzkPsqz8Qx36ka',
    'ADMIN',
    'João Da Silva',
    '12345678988',
    'Joao@example.com',
    NOW()
);


INSERT INTO usuario (
    ATIVO,
    LOGIN,
    SENHA,
    PERFIL,
    NOME,
    CPFCNPJ,
    EMAIL,
    DATAHORAINCLUSAO
) VALUES (
    1, -- Ativo
    'maria', -- Login
    '$2a$10$GD9uBFzCctslvG4TzzxvL.W8YrMOD4hZmrjhrnNyzkPsqz8Qx36ka',
    'CORRENTISTA',
    'Maria Lucia Lima',
    '12345678977',
    'maria@example.com',
    NOW()
);




CREATE TABLE Conta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    saldo DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    version INT,
    numeroContaCorrente BIGINT UNIQUE NOT NULL,
    IDUSUARIO BIGINT,
    dataHoraInclusao TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    digitoContaCorrente SMALLINT,
    tipoContaCorrente TINYINT,
    indAtivo BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (IDUSUARIO) REFERENCES Usuario(IDUSUARIO)
);

INSERT INTO conta (
    saldo,
    version,
    NUMEROCONTACORRENTE,
    IDUSUARIO,
    DATAHORAINCLUSAO,
    DIGITOCONTACORRENTE,
    TIPOCONTACORRENTE,
    INDATIVO
) VALUES (
    888.88,
    0,
    8888888,
    2,
    NOW(),
    8,
    1,
    TRUE
);


INSERT INTO conta (
    saldo,
    version,
    NUMEROCONTACORRENTE,
    IDUSUARIO,
    DATAHORAINCLUSAO,
    DIGITOCONTACORRENTE,
    TIPOCONTACORRENTE,
    INDATIVO
) VALUES (
    1000.00,
    0,
    123456789,
    1,
    NOW(),
    1,
    1,
    TRUE
);


INSERT INTO conta (
    saldo,
    version,
    NUMEROCONTACORRENTE,
    IDUSUARIO,
    DATAHORAINCLUSAO,
    DIGITOCONTACORRENTE,
    TIPOCONTACORRENTE,
    INDATIVO
) VALUES (
    100.00,
    0,
    887777,
    3,
    NOW(),
    9,
    1,
    TRUE
);



