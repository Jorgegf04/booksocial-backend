INSERT INTO author (name, nationality, birth_date) VALUES
('Albert Camus', 'France', '1913-11-07'),
('Franz Kafka', 'Czech Republic', '1883-07-03'),
('George Orwell', 'United Kingdom', '1903-06-25'),
('J.R.R. Tolkien', 'United Kingdom', '1892-01-03'),
('Brandon Sanderson', 'USA', '1975-12-19'),
('Fiódor Dostoyevski', 'Russia', '1821-11-11'),
('Masashi Kishimoto', 'Japan', '1974-11-08'),
('Eiichiro Oda', 'Japan', '1975-01-01'),
('Kentaro Miura', 'Japan', '1966-07-11'),
('Alan Moore', 'United Kingdom', '1953-11-18'),
('Frank Miller', 'USA', '1957-01-27'),
('Haruki Murakami', 'Japan', '1949-01-12'),
('Stephen King', 'USA', '1947-09-21'),
('Isaac Asimov', 'Russia', '1920-01-02'),
('J.K. Rowling', 'United Kingdom', '1965-07-31');

INSERT INTO editorial (name, country) VALUES
('Penguin Random House', 'USA'),
('Planeta', 'Spain'),
('Shueisha', 'Japan'),
('Kodansha', 'Japan'),
('DC Comics', 'USA'),
('Dark Horse Comics', 'USA');

INSERT INTO work (title, description, genre, publication_date, img, average_rating) VALUES
('La Peste', 'Novela existencialista', 'NOVELA', '1947-01-01', NULL, 4.5),
('El extranjero', 'Existencialismo', 'NOVELA', '1942-01-01', NULL, 4.6),

('1984', 'Distopía política', 'NOVELA', '1949-01-01', NULL, 4.7),
('Rebelión en la granja', 'Sátira política', 'NOVELA', '1945-01-01', NULL, 4.6),

('El Señor de los Anillos', 'Fantasía épica', 'FANTASIA', '1954-01-01', NULL, 5.0),
('El Hobbit', 'Fantasía', 'FANTASIA', '1937-01-01', NULL, 4.8),

('Mistborn', 'Fantasía moderna', 'FANTASIA', '2006-01-01', NULL, 4.8),
('El archivo de las tormentas', 'Fantasía épica', 'FANTASIA', '2010-01-01', NULL, 4.9),

('Crimen y castigo', 'Novela psicológica', 'NOVELA', '1866-01-01', NULL, 4.9),
('Los hermanos Karamazov', 'Filosofía', 'NOVELA', '1880-01-01', NULL, 5.0),

('Naruto', 'Manga ninja', 'MANGA', '1999-01-01', NULL, 4.6),
('Naruto Shippuden', 'Manga ninja', 'MANGA', '2007-01-01', NULL, 4.7),

('One Piece', 'Manga piratas', 'MANGA', '1997-01-01', NULL, 4.9),

('Berserk', 'Manga oscuro', 'MANGA', '1989-01-01', NULL, 5.0),

('Watchmen', 'Comic de superhéroes', 'COMIC', '1986-01-01', NULL, 4.9),
('V for Vendetta', 'Comic político', 'COMIC', '1988-01-01', NULL, 4.8),

('Sin City', 'Comic noir', 'COMIC', '1991-01-01', NULL, 4.7),

('It', 'Terror', 'NOVELA', '1986-01-01', NULL, 4.7),
('El resplandor', 'Terror psicológico', 'NOVELA', '1977-01-01', NULL, 4.8),

('Fundación', 'Ciencia ficción', 'NOVELA', '1951-01-01', NULL, 4.9),

('Harry Potter', 'Fantasía', 'FANTASIA', '1997-01-01', NULL, 5.0);

INSERT INTO work_author (work_id, author_id) VALUES
(1,1),(2,1),
(3,3),(4,3),
(5,4),(6,4),
(7,5),(8,5),
(9,6),(10,6),
(11,7),(12,7),
(13,8),
(14,9),
(15,10),(16,10),
(17,11),
(18,13),(19,13),
(20,14),
(21,15);
INSERT INTO edition (isbn, edition_date, work_id, editorial_id) VALUES
('ISBN-001', '2000-01-01', 1, 1),
('ISBN-002', '2001-01-01', 2, 1),
('ISBN-003', '2002-01-01', 3, 2),
('ISBN-004', '2006-01-01', 4, 1);

INSERT INTO product (price, stock, edition_id) VALUES
(19.99, 10, 1),
(15.99, 20, 2),
(25.99, 5, 3),
(29.99, 7, 4);