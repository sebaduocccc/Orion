USE orion_db;

CREATE TABLE usuario(
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(30) NOT NULL UNIQUE,
                        email VARCHAR(255) NOT NULL UNIQUE,
                        password_hash VARCHAR(255) NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT current_timestamp,
                        is_activa BOOLEAN DEFAULT TRUE,
                        email_verified BOOLEAN DEFAULT FALSE
);

CREATE TABLE usuario_profile(

    user_id BIGINT PRIMARY KEY,
    display_name VARCHAR(100),
    bio VARCHAR(255),
    avatar_url VARCHAR(150),
    birth_date DATE,
    FOREIGN KEY (user_id) REFERENCES usuario(id)
);