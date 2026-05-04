package com.example.booksocial_backend.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.booksocial_backend.domain.catalog.Author;
import com.example.booksocial_backend.domain.catalog.Demographic;
import com.example.booksocial_backend.domain.catalog.Edition;
import com.example.booksocial_backend.domain.catalog.Editorial;
import com.example.booksocial_backend.domain.catalog.Genre;
import com.example.booksocial_backend.domain.catalog.Product;
import com.example.booksocial_backend.domain.catalog.Work;
import com.example.booksocial_backend.domain.catalog.WorkType;
import com.example.booksocial_backend.domain.social.Event;
import com.example.booksocial_backend.domain.user.Role;
import com.example.booksocial_backend.domain.user.User;
import com.example.booksocial_backend.repository.AuthorRepository;
import com.example.booksocial_backend.repository.EditionRepository;
import com.example.booksocial_backend.repository.EditorialRepository;
import com.example.booksocial_backend.repository.EventRepository;
import com.example.booksocial_backend.repository.ProductRepository;
import com.example.booksocial_backend.repository.UserRepository;
import com.example.booksocial_backend.repository.WorkRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Siembra datos iniciales en MySQL si las tablas están vacías.
 * Crea admin, obras, autores, editoriales, ediciones, productos y eventos.
 * El bloque es idempotente: no hace nada si ya existen obras.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

        private final UserRepository userRepository;
        private final WorkRepository workRepository;
        private final AuthorRepository authorRepository;
        private final EditorialRepository editorialRepository;
        private final EditionRepository editionRepository;
        private final ProductRepository productRepository;
        private final EventRepository eventRepository;
        private final PasswordEncoder passwordEncoder;

        @Override
        @Transactional
        public void run(ApplicationArguments args) {

                if (!userRepository.existsByUsername("admin")) {
                        userRepository.save(User.builder()
                                        .username("admin")
                                        .email("admin@booksocial.com")
                                        .password(passwordEncoder.encode("admin123"))
                                        .name("Admin")
                                        .secondName("BookSocial")
                                        .role(Role.ADMIN)
                                        .active(true)
                                        .registrationDate(LocalDate.now())
                                        .build());
                        log.info("==> Usuario admin creado (admin / admin123)");
                }

                // ── Resto de datos solo si no hay obras ──────────────────
                if (workRepository.count() > 0) {
                        log.info("[DataInitializer] Catálogo ya presente, omitiendo siembra.");
                        return;
                }

                log.info("[DataInitializer] Insertando catálogo inicial...");

                Editorial norma = saveEditorial("Norma Editorial", "España");
                Editorial planeta = saveEditorial("Planeta Cómic", "España");
                Editorial ecc = saveEditorial("ECC Ediciones", "España");

                Author oda = saveAuthor("Eiichiro Oda", "Japonesa", LocalDate.of(1975, 1, 1));
                Author isayama = saveAuthor("Hajime Isayama", "Japonesa", LocalDate.of(1986, 8, 29));
                Author miura = saveAuthor("Kentaro Miura", "Japonesa", LocalDate.of(1966, 7, 11));
                Author urasawa = saveAuthor("Naoki Urasawa", "Japonesa", LocalDate.of(1960, 1, 2));
                Author kishimoto = saveAuthor("Masashi Kishimoto", "Japonesa", LocalDate.of(1974, 11, 8));
                Author kubo = saveAuthor("Tite Kubo", "Japonesa", LocalDate.of(1977, 6, 26));
                Author arakawa = saveAuthor("Hiromu Arakawa", "Japonesa", LocalDate.of(1973, 5, 8));

                Work onePiece = saveWork(
                                "One Piece",
                                "La historia de Monkey D. Luffy y su tripulación en busca del legendario tesoro One Piece "
                                                +
                                                "en un vasto mundo de piratas, marines y frutas del diablo.",
                                LocalDate.of(1997, 7, 22),
                                "/images/covers/onepiece.jpg",
                                4.9, Genre.ACTION, WorkType.MANGA, Demographic.SHONEN, List.of(oda));

                Work aot = saveWork(
                                "Ataque a los Titanes",
                                "En un mundo donde la humanidad vive encerrada detrás de enormes murallas para protegerse "
                                                +
                                                "de los titanes, gigantes devoradores de hombres sin intención aparente.",
                                LocalDate.of(2009, 9, 9),
                                "/images/covers/aot.jpg",
                                4.8, Genre.ACTION, WorkType.MANGA, Demographic.SHONEN, List.of(isayama));

                Work berserk = saveWork(
                                "Berserk",
                                "Guts, el guerrero de la espada negra, busca venganza contra el destino y los demonios "
                                                +
                                                "que una vez fueron sus compañeros. Una obra oscura y épica de fantasía medieval.",
                                LocalDate.of(1989, 8, 25),
                                "/images/covers/berserk.jpg",
                                4.9, Genre.DARK_FANTASY, WorkType.MANGA, Demographic.SEINEN, List.of(miura));

                Work monster = saveWork(
                                "Monster",
                                "El prestigioso cirujano Kenzo Tenma salva la vida de un niño que años después se convierte "
                                                +
                                                "en un asesino en serie. Un thriller psicológico sobre la naturaleza del bien y el mal.",
                                LocalDate.of(1994, 12, 5),
                                "/images/covers/monster.jpg",
                                4.8, Genre.THRILLER, WorkType.MANGA, Demographic.SEINEN, List.of(urasawa));

                Work naruto = saveWork(
                                "Naruto",
                                "Naruto Uzumaki, un joven ninja que alberga al Zorro de Nueve Colas, sueña con convertirse "
                                                +
                                                "en Hokage y ganarse el respeto de su aldea.",
                                LocalDate.of(1999, 9, 21),
                                "/images/covers/naruto.jpg",
                                4.7, Genre.ACTION, WorkType.MANGA, Demographic.SHONEN, List.of(kishimoto));

                Work bleach = saveWork(
                                "Bleach",
                                "Ichigo Kurosaki, un joven con la capacidad de ver fantasmas, se convierte en Shinigami "
                                                +
                                                "sustituto para defender a los humanos de los Hollows.",
                                LocalDate.of(2001, 8, 7),
                                "/images/covers/bleach.jpg",
                                4.6, Genre.ACTION, WorkType.MANGA, Demographic.SHONEN, List.of(kubo));

                Work fma = saveWork(
                                "Fullmetal Alchemist",
                                "Los hermanos Elric buscan la Piedra Filosofal para recuperar sus cuerpos tras un fallido "
                                                +
                                                "intento de transmutación humana en un mundo regido por la alquimia.",
                                LocalDate.of(2001, 7, 12),
                                "/images/covers/fma.jpg",
                                4.9, Genre.ACTION, WorkType.MANGA, Demographic.SHONEN, List.of(arakawa));

                // ── Ediciones ─────────────────────────────────────────────
                Edition edOp = saveEdition("978-84-679-1234-1", LocalDate.of(2000, 1, 1), "One Piece Vol. 1", 109,
                                onePiece, norma);
                Edition edAot = saveEdition("978-84-679-2345-2", LocalDate.of(2013, 3, 15),
                                "Ataque a los Titanes Vol. 1", 34, aot, norma);
                Edition edBer = saveEdition("978-84-679-3456-3", LocalDate.of(2001, 6, 20), "Berserk Vol. 1", 41,
                                berserk, planeta);
                Edition edMon = saveEdition("978-84-679-4567-4", LocalDate.of(2002, 9, 10), "Monster Vol. 1", 18,
                                monster, planeta);
                Edition edNar = saveEdition("978-84-679-5678-5", LocalDate.of(2002, 3, 15), "Naruto Vol. 1", 72, naruto,
                                norma);
                Edition edBle = saveEdition("978-84-679-6789-6", LocalDate.of(2002, 10, 5), "Bleach Vol. 1", 74, bleach,
                                planeta);
                Edition edFma = saveEdition("978-84-679-7890-7", LocalDate.of(2002, 8, 22),
                                "Fullmetal Alchemist Vol. 1", 27, fma, ecc);

                // ── Productos ─────────────────────────────────────────────
                saveProduct(9.95, 50, edOp);
                saveProduct(9.95, 45, edAot);
                saveProduct(12.50, 30, edBer);
                saveProduct(10.95, 25, edMon);
                saveProduct(9.95, 60, edNar);
                saveProduct(9.95, 55, edBle);
                saveProduct(10.95, 40, edFma);

                // ── Usuarios de ejemplo ───────────────────────────────────
                seedUser("lector1", "lector1@booksocial.com", "Ana", "García", null, Role.REGISTERED);
                seedUser("lector2", "lector2@booksocial.com", "Carlos", "López", null, Role.SUBSCRIBED);
                seedUser("lector3", "lector3@booksocial.com", "María", "Martínez", null, Role.REGISTERED);
                seedUser("lector4", "lector4@booksocial.com", "Pedro", "Sánchez", null, Role.SUBSCRIBED);

                // ── Eventos ───────────────────────────────────────────────
                saveEvent(
                                "Club de Lectura: Berserk",
                                "Reunión mensual para comentar los últimos volúmenes de Berserk. ¡Spoilers permitidos! "
                                                +
                                                "Acceso libre para todos los suscriptores.",
                                LocalDateTime.now().plusDays(10));
                saveEvent(
                                "Maratón: Los mejores arcos de One Piece",
                                "Sesión especial viendo y comentando los arcos más icónicos de One Piece en streaming. "
                                                +
                                                "Entrada gratuita para suscriptores.",
                                LocalDateTime.now().plusDays(20));
                saveEvent(
                                "Debate: Los mejores finales del manga",
                                "Mesa redonda virtual sobre los desenlaces más impactantes e icónicos del mundo del manga. "
                                                +
                                                "Participación abierta con registro previo.",
                                LocalDateTime.now().plusDays(35));

                log.info("[DataInitializer] Catálogo inicial insertado correctamente.");
        }

        // ── Helpers ───────────────────────────────────────────────────────────────

        private Editorial saveEditorial(String name, String country) {
                return editorialRepository.save(
                                Editorial.builder().name(name).country(country).build());
        }

        private Author saveAuthor(String name, String nationality, LocalDate birthDate) {
                return authorRepository.save(
                                Author.builder().name(name).nationality(nationality).birthDate(birthDate).build());
        }

        private Work saveWork(String title, String description, LocalDate pubDate,
                        String img, double rating, Genre genre, WorkType type,
                        Demographic demographic, List<Author> authors) {
                return workRepository.save(Work.builder()
                                .title(title)
                                .description(description)
                                .publicationDate(pubDate)
                                .img(img)
                                .averageRating(rating)
                                .genre(genre)
                                .type(type)
                                .demographic(demographic)
                                .authors(new ArrayList<>(authors))
                                .build());
        }

        private Edition saveEdition(String isbn, LocalDate date, String title,
                        int totalTomes, Work work, Editorial editorial) {
                return editionRepository.save(Edition.builder()
                                .isbn(isbn)
                                .editionDate(date)
                                .title(title)
                                .totalTomes(totalTomes)
                                .work(work)
                                .editorial(editorial)
                                .build());
        }

        private void saveProduct(double price, int stock, Edition edition) {
                productRepository.save(Product.builder()
                                .price(price)
                                .stock(stock)
                                .edition(edition)
                                .build());
        }

        private void seedUser(String username, String email, String name,
                        String secondName, String img, Role role) {
                if (userRepository.existsByUsername(username))
                        return;
                userRepository.save(User.builder()
                                .username(username)
                                .email(email)
                                .name(name)
                                .secondName(secondName)
                                .img(img)
                                .role(role)
                                .active(true)
                                .registrationDate(LocalDate.now())
                                .password(passwordEncoder.encode("password"))
                                .build());
        }

        private void saveEvent(String title, String description, LocalDateTime date) {
                eventRepository.save(Event.builder()
                                .title(title)
                                .description(description)
                                .date(date)
                                .users(new ArrayList<>())
                                .build());
        }
}
