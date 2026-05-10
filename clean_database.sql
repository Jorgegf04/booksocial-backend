-- ============================================================
-- clean_database.sql — BookSocial
-- Vacía todas las tablas en orden seguro (hojas → raíces)
-- Compatible con H2 y MySQL.
-- Uso H2:    RUNSCRIPT FROM 'clean_database.sql'
-- Uso MySQL: SOURCE clean_database.sql
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;   -- MySQL: deshabilita comprobación de FK temporalmente

-- 1. Reacciones (dependen de Comment y User)
DELETE FROM REACTION;

-- 2. Respuestas y comentarios (autorrelación: primero replies, luego raíces)
--    La cascada de JPA se encarga si se borra por ORM.
--    Desde SQL directo, borrar todo a la vez funciona porque parent_id es nullable.
DELETE FROM COMMENT;

-- 3. Seguimientos de obras (dependen de User y Work)
DELETE FROM TRACKING_WORK;

-- 4. Tabla join User ↔ Event
DELETE FROM USER_EVENT;

-- 5. Tabla join Work ↔ Author
DELETE FROM WORK_AUTHOR;

-- 6. Líneas de pedido (dependen de Order y Product)
DELETE FROM ORDER_LINE;

-- 7. Seguimiento logístico de pedidos
DELETE FROM TRACKING_ORDER;

-- 8. Pedidos (dependen de User)
DELETE FROM ORDERS;

-- 9. Productos (dependen de Edition)
DELETE FROM PRODUCT;

-- 10. Capítulos (dependen de Tome)
DELETE FROM CHAPTER;

-- 11. Tomos (dependen de Edition)
DELETE FROM TOME;

-- 12. Volúmenes (dependen de Edition)
DELETE FROM VOLUME;

-- 13. Ediciones (dependen de Work y Editorial)
DELETE FROM EDITION;

-- 14. Obras
DELETE FROM WORK;

-- 15. Autores
DELETE FROM AUTHOR;

-- 16. Editoriales
DELETE FROM EDITORIAL;

-- 17. Suscripciones (dependen de User)
DELETE FROM SUBSCRIPTION;

-- 18. Relaciones de seguimiento entre usuarios
DELETE FROM USER_FOLLOW;

-- 19. Seguimientos de autores (si la tabla existe)
DELETE FROM AUTHOR_FOLLOW;

-- 20. Usuarios
DELETE FROM APPUSER;

-- 21. Eventos
DELETE FROM EVENT;

SET FOREIGN_KEY_CHECKS = 1;   -- MySQL: rehabilita comprobación de FK

-- ============================================================
-- Para H2 (no soporta FOREIGN_KEY_CHECKS), usar:
--   SET REFERENTIAL_INTEGRITY FALSE;
--   ... DELETE statements ...
--   SET REFERENTIAL_INTEGRITY TRUE;
-- ============================================================
