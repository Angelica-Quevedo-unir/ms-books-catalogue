CREATE TABLE books (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       author VARCHAR(255) NOT NULL,
                       publication_date DATE NOT NULL,
                       category VARCHAR(100) NOT NULL,
                       isbn VARCHAR(13) NOT NULL UNIQUE,
                       rating TINYINT CHECK (rating >= 1 AND rating <= 5),
                       visibility BOOLEAN DEFAULT TRUE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Índices individuales para búsquedas frecuentes
CREATE INDEX idx_books_title ON books (title);
CREATE INDEX idx_books_author ON books (author);
CREATE INDEX idx_books_category ON books (category);

-- Índice compuesto para búsquedas combinadas comunes
CREATE INDEX idx_books_title_author ON books (title, author);

-- Índice único para ISBN (ya está garantizado por la restricción UNIQUE)
