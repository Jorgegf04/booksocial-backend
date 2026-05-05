package com.example.booksocial_backend.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
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

        @Value("${seeder.force-reseed:false}")
        private boolean forceReseed;

        @Override
        @Transactional
        public void run(ApplicationArguments args) {

                seedAdmin();

                if (forceReseed) {
                        log.warn("[DataInitializer] FORCE_RESEED=true → borrando catálogo existente...");
                        clearCatalog();
                }

                log.info("[DataInitializer] Verificando catálogo inicial (idempotente)...");

                // ── Editoriales ───────────────────────────────────────────
                Editorial planeta = saveEditorial("Planeta", "España");
                Editorial norma   = saveEditorial("Norma Editorial", "España");
                Editorial panini  = saveEditorial("Panini Manga", "Italia");
                Editorial ecc     = saveEditorial("ECC Ediciones", "España");
                Editorial anaya   = saveEditorial("Anaya", "España");
                Editorial harper  = saveEditorial("HarperCollins", "USA");
                Editorial penguin = saveEditorial("Penguin Random House", "España");

                // ── Autores ───────────────────────────────────────────────
                Author tolkien    = saveAuthor("J.R.R. Tolkien",         "Británica",       null);
                Author martin     = saveAuthor("George R.R. Martin",     null,              LocalDate.of(1948,  9, 20));
                Author sanderson  = saveAuthor("Brandon Sanderson",      null,              LocalDate.of(1975, 12, 19));
                Author rothfuss   = saveAuthor("Patrick Rothfuss",       null,              LocalDate.of(1973,  6,  6));
                Author kishimoto  = saveAuthor("Masashi Kishimoto",      null,              LocalDate.of(1974, 11,  8));
                Author oda        = saveAuthor("Eiichiro Oda",           "Japonesa",        LocalDate.of(1975,  1,  1));
                Author kubo       = saveAuthor("Tite Kubo",              "Japonesa",        LocalDate.of(1977,  6, 26));
                Author isayama    = saveAuthor("Hajime Isayama",         null,              LocalDate.of(1986,  8, 29));
                Author moore      = saveAuthor("Alan Moore",             null,              LocalDate.of(1953, 11, 18));
                Author miller     = saveAuthor("Frank Miller",           null,              LocalDate.of(1957,  1, 27));
                Author gaiman     = saveAuthor("Neil Gaiman",            null,              LocalDate.of(1960, 11, 10));
                Author asimov     = saveAuthor("Isaac Asimov",           null,              LocalDate.of(1920,  1,  2));
                Author dick       = saveAuthor("Philip K. Dick",         null,              LocalDate.of(1928, 12, 16));
                Author clarke     = saveAuthor("Arthur C. Clarke",       null,              LocalDate.of(1917, 12, 16));
                Author king       = saveAuthor("Stephen King",           null,              LocalDate.of(1947,  9, 21));
                Author lovecraft  = saveAuthor("H.P. Lovecraft",         null,              LocalDate.of(1890,  8, 19));
                Author dostoevsky = saveAuthor("Fiódor Dostoyevski",     null,              LocalDate.of(1821, 11, 10));
                Author kafka      = saveAuthor("Franz Kafka",            null,              LocalDate.of(1883,  7,  2));
                Author camus      = saveAuthor("Albert Camus",           null,              LocalDate.of(1913, 11,  7));
                Author orwell     = saveAuthor("George Orwell",          null,              LocalDate.of(1903,  6, 25));
                Author miura      = saveAuthor("Kentaro Miura",          "Japonesa",        LocalDate.of(1966,  7, 11));
                Author arakawa    = saveAuthor("Hiromu Arakawa",         "Japonesa",        LocalDate.of(1973,  5,  8));
                Author urasawa    = saveAuthor("Naoki Urasawa",          "Japonesa",        LocalDate.of(1960,  1,  2));
                Author sapkowski  = saveAuthor("Andrzej Sapkowski",      "Polaca",          LocalDate.of(1948,  6, 21));
                Author herbert    = saveAuthor("Frank Herbert",          "Estadounidense",  LocalDate.of(1920, 10,  8));
                Author tolstoi    = saveAuthor("León Tolstoi",           "Rusa",            LocalDate.of(1828,  9,  9));

                // ── Obras ─────────────────────────────────────────────────
                Work hobbit = saveWork(
                        "El Hobbit",
                        "Bilbo Bolsón, un hobbit tranquilo, es convencido por el mago Gandalf para unirse a un grupo de enanos en busca de un tesoro custodiado por el dragón Smaug.",
                        LocalDate.of(1937, 9, 21), 4.5, Genre.FANTASY, WorkType.BOOK, null,
                        List.of(tolkien));

                Work lotr = saveWork(
                        "El Señor de los Anillos",
                        "La épica historia del hobbit Frodo Bolsón y la Comunidad del Anillo en su misión de destruir el Anillo Único y derrotar al Señor Oscuro Sauron.",
                        LocalDate.of(1954, 7, 29), 4.8, Genre.FANTASY, WorkType.BOOK, null,
                        List.of(tolkien));

                Work got = saveWork(
                        "Juego de Tronos",
                        "Nobles familias luchan por el control del Trono de Hierro en los Siete Reinos de Westeros, mientras una antigua amenaza despierta más allá del Muro.",
                        LocalDate.of(1996, 8, 1), 4.3, Genre.FANTASY, WorkType.BOOK, null,
                        List.of(martin));

                Work nameOfWind = saveWork(
                        "El nombre del viento",
                        "Kvothe, el legendario mago y músico, relata su propia historia: desde su infancia en una troupe de artistas hasta su época en la universidad de magia.",
                        LocalDate.of(2007, 3, 27), 4.2, Genre.FANTASY, WorkType.BOOK, null,
                        List.of(rothfuss));

                Work wayOfKings = saveWork(
                        "El camino de los reyes",
                        "En el mundo de Roshar, devastado por tormentas, tres vidas convergen: un antiguo guerrero, una erudita que busca secretos prohibidos y un esclavo destinado al liderazgo.",
                        LocalDate.of(2010, 8, 31), 4.4, Genre.FANTASY, WorkType.BOOK, null,
                        List.of(sanderson));

                Work foundation = saveWork(
                        "Fundación",
                        "Hari Seldon, matemático y psicólogo, predice la caída del Imperio Galáctico y establece la Fundación para preservar el conocimiento y reducir el período de barbarie.",
                        LocalDate.of(1951, 5, 1), 4.6, Genre.SCIENCE_FICTION, WorkType.BOOK, null,
                        List.of(asimov));

                Work doAndroidsDream = saveWork(
                        "¿Sueñan los androides con ovejas eléctricas?",
                        "Rick Deckard, cazarrecompensas en un mundo posapocalíptico, persigue a seis androides fugitivos que son casi indistinguibles de los humanos.",
                        LocalDate.of(1968, 3, 1), 4.0, Genre.SCIENCE_FICTION, WorkType.BOOK, null,
                        List.of(dick));

                Work space2001 = saveWork(
                        "2001: Una odisea espacial",
                        "Una misteriosa roca negra aparece en la Tierra e influye en la evolución humana. Millones de años después, astronautas viajan a Júpiter con la IA HAL 9000.",
                        LocalDate.of(1968, 7, 1), 2.8, Genre.SCIENCE_FICTION, WorkType.BOOK, null,
                        List.of(clarke));

                Work dune = saveWork(
                        "Dune",
                        "Paul Atreides hereda el control del planeta desértico Arrakis, único productor de la especia más valiosa del universo, en medio de intrigas políticas y proféticas.",
                        LocalDate.of(1965, 8, 1), 4.7, Genre.SCIENCE_FICTION, WorkType.BOOK, null,
                        List.of(herbert));

                Work it = saveWork(
                        "It",
                        "Un grupo de niños en el pueblo de Derry se enfrenta a una entidad maligna que adopta la forma de sus peores miedos, principalmente como el payaso Pennywise.",
                        LocalDate.of(1986, 9, 15), 3.8, Genre.HORROR, WorkType.BOOK, null,
                        List.of(king));

                Work cthulhu = saveWork(
                        "La llamada de Cthulhu",
                        "Una serie de relatos interconectados revelan la existencia de Cthulhu, una deidad cósmica antigua que duerme en la ciudad sumergida de R'lyeh.",
                        LocalDate.of(1928, 2, 1), 2.3, Genre.HORROR, WorkType.BOOK, null,
                        List.of(lovecraft));

                Work crimeAndPunishment = saveWork(
                        "Crimen y castigo",
                        "Raskolnikov, un estudiante empobrecido, comete un asesinato y lucha con la culpa, el miedo y su propia filosofía sobre los hombres extraordinarios.",
                        LocalDate.of(1866, 1, 1), 4.6, Genre.DRAMA, WorkType.BOOK, null,
                        List.of(dostoevsky));

                Work metamorphosis = saveWork(
                        "La metamorfosis",
                        "Gregor Samsa despierta convertido en un insecto gigante. La novela explora su alienación y el impacto en su familia mientras lucha por mantener su humanidad.",
                        LocalDate.of(1915, 10, 15), 3.2, Genre.DRAMA, WorkType.BOOK, null,
                        List.of(kafka));

                Work stranger = saveWork(
                        "El extranjero",
                        "Meursault, un hombre indiferente, mata a un árabe en una playa de Argelia sin motivación aparente y enfrenta su juicio sin mostrar arrepentimiento.",
                        LocalDate.of(1942, 6, 1), 4.0, Genre.PHILOSOPHICAL, WorkType.BOOK, null,
                        List.of(camus));

                Work animalFarm = saveWork(
                        "1984",
                        "En un Estado totalitario controlado por el Gran Hermano, Winston Smith trabaja reescribiendo la historia y comienza a resistir el sistema de forma secreta.",
                        LocalDate.of(1949, 6, 8), 4.5, Genre.SCIENCE_FICTION, WorkType.BOOK, null,
                        List.of(orwell));

                Work witcher = saveWork(
                        "El último deseo",
                        "Geralt de Rivia, un brujo cazador de monstruos, navega por un mundo de magia, moral ambigua y criaturas peligrosas en una serie de relatos entrelazados.",
                        LocalDate.of(1993, 1, 1), 3.5, Genre.FANTASY, WorkType.BOOK, null,
                        List.of(sapkowski));

                Work warAndPeace = saveWork(
                        "Guerra y paz",
                        "A través de cinco familias nobles rusas, Tolstói retrata la invasión napoleónica de Rusia y sus consecuencias en la sociedad, el amor y el destino humano.",
                        LocalDate.of(1869, 1, 1), 4.2, Genre.HISTORICAL, WorkType.BOOK, null,
                        List.of(tolstoi));

                Work onePiece = saveWork(
                        "One Piece",
                        "Monkey D. Luffy, un joven con poderes de goma, reúne a su tripulación y navega el Gran Line en busca del legendario tesoro One Piece para convertirse en Rey de los Piratas.",
                        LocalDate.of(1997, 7, 22), 4.9, Genre.ACTION, WorkType.MANGA, Demographic.SHONEN,
                        List.of(oda));

                Work naruto = saveWork(
                        "Naruto",
                        "Naruto Uzumaki, un joven ninja que alberga al Zorro de Nueve Colas, sueña con convertirse en Hokage y ganarse el respeto de su aldea.",
                        LocalDate.of(1999, 9, 21), 4.5, Genre.ACTION, WorkType.MANGA, Demographic.SHONEN,
                        List.of(kishimoto));

                Work bleach = saveWork(
                        "Bleach",
                        "Ichigo Kurosaki, un joven con la capacidad de ver fantasmas, se convierte en Shinigami sustituto para defender a los humanos de los Hollows.",
                        LocalDate.of(2001, 8, 7), 4.0, Genre.ACTION, WorkType.MANGA, Demographic.SHONEN,
                        List.of(kubo));

                Work aot = saveWork(
                        "Ataque a los Titanes",
                        "La humanidad vive encerrada tras murallas gigantes para protegerse de los Titanes. Eren Jaeger jura destruirlos a todos tras ver caer su aldea.",
                        LocalDate.of(2009, 9, 9), 4.6, Genre.ACTION, WorkType.MANGA, Demographic.SHONEN,
                        List.of(isayama));

                Work berserk = saveWork(
                        "Berserk",
                        "Guts, el guerrero de la espada negra, busca venganza contra Griffith y los Apóstoles en un mundo oscuro de fantasía medieval lleno de demonios y tragedia.",
                        LocalDate.of(1989, 8, 25), 4.9, Genre.DARK_FANTASY, WorkType.MANGA, Demographic.SEINEN,
                        List.of(miura));

                Work fma = saveWork(
                        "Fullmetal Alchemist",
                        "Los hermanos Elric buscan la Piedra Filosofal para recuperar sus cuerpos tras un fallido intento de transmutación humana en un mundo regido por la alquimia.",
                        LocalDate.of(2001, 7, 12), 4.8, Genre.ACTION, WorkType.MANGA, Demographic.SHONEN,
                        List.of(arakawa));

                Work monster = saveWork(
                        "Monster",
                        "El cirujano Kenzo Tenma salva la vida de un niño que se convierte en un asesino en serie. Un thriller psicológico sobre la naturaleza del bien y el mal.",
                        LocalDate.of(1994, 12, 5), 4.7, Genre.THRILLER, WorkType.MANGA, Demographic.SEINEN,
                        List.of(urasawa));

                Work watchmen = saveWork(
                        "Watchmen",
                        "En una América alternativa donde los superhéroes son reales, un ex vigilante investiga el asesinato de un colega mientras se revela una conspiración global.",
                        LocalDate.of(1986, 9, 1), 4.7, Genre.SUPERHERO, WorkType.COMIC, Demographic.SEINEN,
                        List.of(moore));

                Work darkKnight = saveWork(
                        "Batman: El regreso del Caballero Oscuro",
                        "Un Batman retirado de 55 años regresa para enfrentarse al crimen en Gotham, desafiando al gobierno y al propio Superman en una distopía futurista.",
                        LocalDate.of(1986, 2, 1), 3.0, Genre.SUPERHERO, WorkType.COMIC, Demographic.SEINEN,
                        List.of(miller));

                Work sandman = saveWork(
                        "Sandman",
                        "Morfeo, el Señor de los Sueños, es capturado y liberado tras décadas de cautiverio, reconstruyendo su reino mientras influye en la mitología y la historia humana.",
                        LocalDate.of(1989, 1, 1), 4.5, Genre.DARK_FANTASY, WorkType.COMIC, Demographic.SEINEN,
                        List.of(gaiman));

                // ── Ediciones ─────────────────────────────────────────────
                Edition edHobbit1   = saveEdition("9780000000011", LocalDate.of(2001, 1,  1),  "El Hobbit (Edición revisada)",                    null, hobbit,           planeta);
                Edition edHobbit2   = saveEdition("9780000000033", LocalDate.of(2005, 1,  1),  "El Hobbit (Edición revisada)",                    null, hobbit,           norma);
                Edition edLotr1     = saveEdition("9780000000021", LocalDate.of(2002, 1,  1),  "El Señor de los Anillos",                         null, lotr,             planeta);
                Edition edLotr2     = saveEdition("9780000000302", LocalDate.of(2010, 1,  1),  "El Señor de los Anillos",                         null, lotr,             norma);
                Edition edGot1      = saveEdition("9780000000031", LocalDate.of(1998, 1,  1),  "Juego de Tronos",                                 null, got,              planeta);
                Edition edGot2      = saveEdition("9780000000301", LocalDate.of(2019, 3, 15),  "Juego de Tronos - Ed. Bolsillo",                  null, got,              norma);
                Edition edGot3      = saveEdition("9780000000034", LocalDate.of(2022, 6,  1),  "A Game of Thrones - HarperCollins",               null, got,              harper);
                Edition edNow1      = saveEdition("9780000000054", LocalDate.of(2021, 9, 10),  "The Name of the Wind - HarperCollins",            null, nameOfWind,       harper);
                Edition edNow2      = saveEdition("9780000000067", LocalDate.of(2023, 1, 20),  "El nombre del viento - Debolsillo",               null, nameOfWind,       penguin);
                Edition edWayKings  = saveEdition("9780000000074", LocalDate.of(2012, 5,  5),  "El camino de los reyes",                          null, wayOfKings,       norma);
                Edition edFound1    = saveEdition("9780000000132", LocalDate.of(2010, 4, 22),  "Fundación - Anaya",                               null, foundation,       anaya);
                Edition edFound2    = saveEdition("9780000000133", LocalDate.of(2020, 11, 5),  "Foundation - HarperCollins",                      null, foundation,       harper);
                Edition edAndroid   = saveEdition("9780000000142", LocalDate.of(2015, 2, 28),  "Blade Runner - Ed. Aniversario",                  null, doAndroidsDream,  anaya);
                Edition ed2001      = saveEdition("9780000000151", LocalDate.of(1968, 6,  1),  "2001: Una odisea espacial",                       null, space2001,        planeta);
                Edition edDune1     = saveEdition("9780000000162", LocalDate.of(2001, 3,  1),  "Dune",                                            null, dune,             norma);
                Edition edIt1       = saveEdition("9780000000163", LocalDate.of(2022, 6, 18),  "It - Edición 30 Aniversario",                     null, it,               norma);
                Edition edIt2       = saveEdition("9780000000183", LocalDate.of(2018, 7, 22),  "It - HarperCollins",                              null, it,               harper);
                Edition edCthulhu   = saveEdition("9780000000202", LocalDate.of(2018, 5, 12),  "La llamada de Cthulhu - Anaya",                   null, cthulhu,          anaya);
                Edition edCrimen1   = saveEdition("9780000000303", LocalDate.of(2018, 3, 15),  "Crimen y castigo - Anaya Clásicos",               null, crimeAndPunishment, anaya);
                Edition edCrimen2   = saveEdition("9780000000182", LocalDate.of(2018, 7, 22),  "Crime and Punishment - HarperCollins",            null, crimeAndPunishment, harper);
                Edition edMeta      = saveEdition("9780000000222", LocalDate.of(2021, 4,  5),  "La metamorfosis - Ed. Ilustrada",                 null, metamorphosis,    anaya);
                Edition edExt       = saveEdition("9780000000232", LocalDate.of(2022, 9, 14),  "El extranjero - Ed. Especial",                    null, stranger,         norma);
                Edition ed1984      = saveEdition("9780000000243", LocalDate.of(2003, 6,  8),  "1984",                                            null, animalFarm,       norma);
                Edition edWitcher   = saveEdition("9780000000253", LocalDate.of(2016, 4, 12),  "El último deseo - Salamandra",                    null, witcher,          norma);
                Edition edWarPeace  = saveEdition("9780000000263", LocalDate.of(2000, 9,  1),  "Guerra y paz",                                    null, warAndPeace,      planeta);
                Edition edOp1       = saveEdition("9780000000071", LocalDate.of(1997, 1,  1),  "One Piece",                                       100,  onePiece,         norma);
                Edition edOp2       = saveEdition("9780000000998", LocalDate.of(2024, 1,  1),  "One Piece Edición Especial",                      100,  onePiece,         norma);
                Edition edNar1      = saveEdition("9780000000061", LocalDate.of(2000, 1,  1),  "Naruto",                                          72,   naruto,           norma);
                Edition edNar2      = saveEdition("9780000000024", LocalDate.of(2024, 1,  1),  "Naruto Edición Deluxe",                           72,   naruto,           norma);
                Edition edBle1      = saveEdition("9780000000081", LocalDate.of(2002, 1,  1),  "Bleach",                                          74,   bleach,           norma);
                Edition edBle2      = saveEdition("9780000000997", LocalDate.of(2022, 1,  1),  "Bleach Edición Completa",                         74,   bleach,           norma);
                Edition edAot       = saveEdition("9780000000091", LocalDate.of(2013, 1,  1),  "Ataque a los Titanes",                            34,   aot,              norma);
                Edition edBer       = saveEdition("9780000000101", LocalDate.of(2001, 1,  1),  "Berserk",                                         41,   berserk,          planeta);
                Edition edFma       = saveEdition("9780000000111", LocalDate.of(2003, 1,  1),  "Fullmetal Alchemist",                             27,   fma,              norma);
                Edition edMon       = saveEdition("9780000000121", LocalDate.of(2002, 1,  1),  "Monster",                                         18,   monster,          norma);
                Edition edWatch1    = saveEdition("9780000000102", LocalDate.of(1987, 1,  1),  "Watchmen",                                        null, watchmen,         ecc);
                Edition edWatch2    = saveEdition("9780000000092", LocalDate.of(2020, 10, 1),  "Watchmen - Edición Absolute",                     null, watchmen,         norma);
                Edition edDark1     = saveEdition("9780000000112", LocalDate.of(1987, 1,  1),  "Batman: El Regreso del Caballero Oscuro",          null, darkKnight,       ecc);
                Edition edDark2     = saveEdition("9780000000304", LocalDate.of(2016, 7, 19),  "Batman: El Regreso - Ed. Especial",               null, darkKnight,       norma);
                Edition edSand1     = saveEdition("9780000000122", LocalDate.of(1989, 1,  1),  "Sandman",                                         null, sandman,          ecc);
                Edition edSand2     = saveEdition("9780000000305", LocalDate.of(2015, 1,  1),  "Sandman",                                         null, sandman,          norma);

                // ── Productos ─────────────────────────────────────────────
                saveProduct(14.95, 40,  edHobbit1);
                saveProduct(12.95, 30,  edHobbit2);
                saveProduct(16.95, 35,  edLotr1);
                saveProduct(14.95, 25,  edLotr2);
                saveProduct(15.95, 50,  edGot1);
                saveProduct(12.95, 45,  edGot2);
                saveProduct(13.95, 30,  edGot3);
                saveProduct(14.95, 20,  edNow1);
                saveProduct(11.95, 25,  edNow2);
                saveProduct(16.95, 15,  edWayKings);
                saveProduct(12.95, 30,  edFound1);
                saveProduct(14.95, 20,  edFound2);
                saveProduct(13.95, 25,  edAndroid);
                saveProduct(11.95, 20,  ed2001);
                saveProduct(15.95, 20,  edDune1);
                saveProduct(13.95, 30,  edIt1);
                saveProduct(12.95, 25,  edIt2);
                saveProduct(10.95, 15,  edCthulhu);
                saveProduct(11.95, 20,  edCrimen1);
                saveProduct(13.95, 15,  edCrimen2);
                saveProduct(10.95, 20,  edMeta);
                saveProduct(11.95, 20,  edExt);
                saveProduct(12.95, 35,  ed1984);
                saveProduct(14.95, 20,  edWitcher);
                saveProduct(18.95, 15,  edWarPeace);
                saveProduct( 9.95, 50,  edOp1);
                saveProduct(12.95, 30,  edOp2);
                saveProduct( 9.95, 60,  edNar1);
                saveProduct(14.95, 25,  edNar2);
                saveProduct( 9.95, 55,  edBle1);
                saveProduct(18.95, 20,  edBle2);
                saveProduct( 9.95, 45,  edAot);
                saveProduct(12.50, 30,  edBer);
                saveProduct(10.95, 40,  edFma);
                saveProduct(10.95, 25,  edMon);
                saveProduct(16.95, 20,  edWatch1);
                saveProduct(29.95, 10,  edWatch2);
                saveProduct(15.95, 15,  edDark1);
                saveProduct(19.95, 10,  edDark2);
                saveProduct(15.95, 20,  edSand1);
                saveProduct(12.95, 15,  edSand2);

                // ── Usuarios ──────────────────────────────────────────────
                seedUser("jorge_dev",        "jorge.devv@test.com",     "Jorge",  "Gujarro",   Role.ADMIN);
                seedUser("maria.reader",     "maria.reader@test.com",   "María",  "López",     Role.REGISTERED);
                seedUser("carolinacarolina", "carolina_new@email.com",  null,     null,        Role.REGISTERED);
                seedUser("ana_fantasy",      "ana.fantasy@test.com",    "Ana",    "García",    Role.REGISTERED);
                seedUser("luis_manga",       "luis.manga@test.com",     "Luis",   "Fernández", Role.SUBSCRIBED);
                seedUser("sofia_reader",     "sofia.reader@test.com",   "Sofia",  "Ramírez",   Role.REGISTERED);
                seedUser("pedro_terror",     "pedro.terror@test.com",   "Pedro",  "Sánchez",   Role.REGISTERED);
                seedUser("laura_scifi",      "laura.sofi@test.com",     "Laura",  "Torres",    Role.SUBSCRIBED);
                seedUser("david_comics",     "david.comics@test.com",   "David",  "Ruiz",      Role.REGISTERED);
                seedUser("elena_admin",      "elena.admin@test.com",    "Elena",  "Navarro",   Role.REGISTERED);
                seedUser("jorge11",          "user1@test.com",          null,     null,        Role.REGISTERED);
                seedUser("admin11",          "admin11@test.com",        null,     null,        Role.REGISTERED);
                seedUser("Lucia123",         "lucia@test.com",          "Lucía",  "Gujarro",   Role.REGISTERED);
                seedUser("jorgegi4",         "jgfestudios@gmail.com",   null,     null,        Role.REGISTERED);

                // ── Eventos ───────────────────────────────────────────────
                saveEvent(
                        "Club de Lectura: Berserk",
                        "Reunión mensual para comentar los últimos volúmenes de Berserk. ¡Spoilers permitidos! Acceso libre para todos los suscriptores.",
                        LocalDateTime.now().plusDays(10));
                saveEvent(
                        "Maratón: Los mejores arcos de One Piece",
                        "Sesión especial comentando los arcos más icónicos de One Piece. Entrada gratuita para suscriptores.",
                        LocalDateTime.now().plusDays(20));
                saveEvent(
                        "Debate: Los mejores finales del manga",
                        "Mesa redonda virtual sobre los desenlaces más impactantes e icónicos del mundo del manga. Participación abierta con registro previo.",
                        LocalDateTime.now().plusDays(35));
                saveEvent(
                        "Presentación: Novedades de Panini Manga 2026",
                        "Conoce todas las novedades y reediciones que Panini Manga tiene preparadas para la segunda mitad del año. Evento exclusivo para suscriptores.",
                        LocalDateTime.now().plusDays(50));
                saveEvent(
                        "Taller: Cómo empezar con los cómics de autor",
                        "Una guía práctica para iniciarse en el cómic europeo y americano de autor, con recomendaciones de Watchmen, Sandman y más.",
                        LocalDateTime.now().plusDays(65));

                log.info("[DataInitializer] Catálogo verificado correctamente.");
        }

        // ── Helpers ───────────────────────────────────────────────────────────────

        private void clearCatalog() {
                productRepository.deleteAll();
                editionRepository.deleteAll();
                workRepository.deleteAll();
                authorRepository.deleteAll();
                editorialRepository.deleteAll();
                eventRepository.deleteAll();
                log.info("[DataInitializer] Catálogo borrado.");
        }

        private void seedAdmin() {
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
        }

        private Editorial saveEditorial(String name, String country) {
                return editorialRepository.findByName(name)
                        .orElseGet(() -> editorialRepository.save(
                                Editorial.builder().name(name).country(country).build()));
        }

        private Author saveAuthor(String name, String nationality, LocalDate birthDate) {
                return authorRepository.findByName(name)
                        .orElseGet(() -> authorRepository.save(
                                Author.builder().name(name).nationality(nationality).birthDate(birthDate).build()));
        }

        private Work saveWork(String title, String description, LocalDate pubDate,
                        double rating, Genre genre, WorkType type,
                        Demographic demographic, List<Author> authors) {
                return workRepository.findByTitle(title)
                        .orElseGet(() -> workRepository.save(Work.builder()
                                .title(title)
                                .description(description)
                                .publicationDate(pubDate)
                                .img(null)
                                .averageRating(rating)
                                .genre(genre)
                                .type(type)
                                .demographic(demographic)
                                .authors(new ArrayList<>(authors))
                                .build()));
        }

        private Edition saveEdition(String isbn, LocalDate date, String title,
                        Integer totalTomes, Work work, Editorial editorial) {
                return editionRepository.findByIsbn(isbn)
                        .orElseGet(() -> editionRepository.save(Edition.builder()
                                .isbn(isbn)
                                .editionDate(date)
                                .title(title)
                                .totalTomes(totalTomes)
                                .work(work)
                                .editorial(editorial)
                                .build()));
        }

        private void saveProduct(double price, int stock, Edition edition) {
                if (productRepository.findByEditionId(edition.getId()).isEmpty()) {
                        productRepository.save(Product.builder()
                                .price(price)
                                .stock(stock)
                                .edition(edition)
                                .build());
                }
        }

        private void seedUser(String username, String email, String name,
                        String secondName, Role role) {
                if (userRepository.existsByUsername(username))
                        return;
                userRepository.save(User.builder()
                        .username(username)
                        .email(email)
                        .name(name)
                        .secondName(secondName)
                        .img(null)
                        .role(role)
                        .active(true)
                        .registrationDate(LocalDate.now())
                        .password(passwordEncoder.encode("password123"))
                        .build());
        }

        private void saveEvent(String title, String description, LocalDateTime date) {
                if (!eventRepository.existsByTitle(title)) {
                        eventRepository.save(Event.builder()
                                .title(title)
                                .description(description)
                                .date(date)
                                .users(new ArrayList<>())
                                .build());
                }
        }
}
