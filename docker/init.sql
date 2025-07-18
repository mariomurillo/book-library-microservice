CREATE TABLE IF NOT EXISTS Book (
    isbn VARCHAR(255) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    publication_year INT NOT NULL
);

-- Sample data
INSERT INTO Book (isbn, title, author, publication_year) VALUES
('978-3-16-148410-0', 'The Book', 'Author Name', 2023);
