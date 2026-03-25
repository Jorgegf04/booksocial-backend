INSERT INTO author (id, name, nationality, birth_date) VALUES
(1, 'Albert Camus', 'France', '1913-11-07'),
(2, 'Franz Kafka', 'Czech Republic', '1883-07-03'),
(3, 'George Orwell', 'United Kingdom', '1903-06-25'),
(4, 'J.R.R. Tolkien', 'United Kingdom', '1892-01-03'),
(5, 'Brandon Sanderson', 'USA', '1975-12-19'),
(6, 'Fiódor Dostoyevski', 'Russia', '1821-11-11'),
(7, 'Masashi Kishimoto', 'Japan', '1974-11-08'),
(8, 'Eiichiro Oda', 'Japan', '1975-01-01'),
(9, 'Kentaro Miura', 'Japan', '1966-07-11'),
(10, 'Alan Moore', 'United Kingdom', '1953-11-18'),
(11, 'Frank Miller', 'USA', '1957-01-27');

INSERT INTO editorial (id, name, country) VALUES
(1, 'Penguin Random House', 'USA'),
(2, 'Planeta', 'Spain'),
(3, 'Shueisha', 'Japan'),
(4, 'Kodansha', 'Japan'),
(5, 'DC Comics', 'USA'),
(6, 'Dark Horse Comics', 'USA');

INSERT INTO work (id, title, description, genre, publication_date, img, average_rating) VALUES
(1, 'La Peste', 'Novela existencialista', 'NOVELA', '1947-01-01', NULL, 4.5),
(2, '1984', 'Distopía política', 'NOVELA', '1949-01-01', NULL, 4.7),
(3, 'El Señor de los Anillos', 'Fantasía épica', 'FANTASIA', '1954-01-01', NULL, 5.0),
(4, 'Mistborn', 'Fantasía moderna', 'FANTASIA', '2006-01-01', NULL, 4.8),
(5, 'Crimen y castigo', 'Novela psicológica', 'NOVELA', '1866-01-01', NULL, 4.9),
(6, 'Naruto', 'Manga ninja', 'MANGA', '1999-01-01', NULL, 4.6),
(7, 'One Piece', 'Manga piratas', 'MANGA', '1997-01-01', NULL, 4.9),
(8, 'Berserk', 'Manga oscuro', 'MANGA', '1989-01-01', NULL, 5.0),
(9, 'Watchmen', 'Comic de superhéroes', 'COMIC', '1986-01-01', NULL, 4.9),
(10, 'Sin City', 'Comic noir', 'COMIC', '1991-01-01', NULL, 4.7);

INSERT INTO work_author (work_id, author_id) VALUES
(1,1),
(2,3),
(3,4),
(4,5),
(5,6),
(6,7),
(7,8),
(8,9),
(9,10),
(10,11);

INSERT INTO edition (id, isbn, edition_date, work_id, editorial_id) VALUES
(1, 'ISBN-001', '2000-01-01', 1, 1),
(2, 'ISBN-002', '2001-01-01', 2, 1),
(3, 'ISBN-003', '2002-01-01', 3, 2),
(4, 'ISBN-004', '2006-01-01', 4, 1),
(5, 'ISBN-005', '1999-01-01', 6, 3),
(6, 'ISBN-006', '1997-01-01', 7, 3),
(7, 'ISBN-007', '1989-01-01', 8, 4),
(8, 'ISBN-008', '1986-01-01', 9, 5),
(9, 'ISBN-009', '1991-01-01', 10, 6);

INSERT INTO product (id, price, stock, edition_id) VALUES
(1, 19.99, 10, 1),
(2, 15.99, 20, 2),
(3, 25.99, 5, 3),
(4, 29.99, 7, 4),
(5, 9.99, 50, 5),
(6, 8.99, 100, 6),
(7, 12.99, 30, 7),
(8, 18.99, 12, 8),
(9, 17.99, 15, 9);