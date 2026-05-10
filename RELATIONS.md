# BookSocial — Mapa de Relaciones JPA

## Convenciones

| Columna | Significado |
|---------|-------------|
| Tipo | OneToOne / OneToMany / ManyToOne / ManyToMany |
| Propietario | Entidad que tiene la FK (o tabla join) |
| mappedBy | Campo en el lado inverso |
| cascade | Tipos de cascada activos |
| orphanRemoval | Si se borran huérfanos automáticamente |
| fetch | LAZY / EAGER |

---

## Dominio User

### User ↔ Subscription (1:1 opcional)
| Propiedad | Valor |
|-----------|-------|
| Tipo | OneToOne bidireccional |
| Propietario | `Subscription` (FK `user_id` en tabla SUBSCRIPTION) |
| mappedBy | `User.subscription` → `mappedBy = "user"` |
| cascade (User side) | `CascadeType.ALL` — borrar User elimina su Subscription |
| orphanRemoval | No declarado explícitamente en User |
| fetch | LAZY en ambos lados |
| Política de negocio | Si se borra el User (soft delete), la Subscription queda en BD pero `active=false`. Si se borra físicamente, la cascada elimina la Subscription. |

### User → TrackingWork (1:N)
| Propiedad | Valor |
|-----------|-------|
| Tipo | OneToMany |
| Propietario | `TrackingWork` (FK `user_id`) |
| mappedBy | `User.trackingWorks` → `mappedBy = "user"` |
| cascade | `CascadeType.ALL` |
| orphanRemoval | `true` |
| fetch | LAZY |
| Política | Al borrar User, se eliminan en cascada todos sus TrackingWork. |

### User → UserFollow (1:N como follower y como following)
| Propiedad | Valor |
|-----------|-------|
| Tipo | OneToMany (dos relaciones) |
| Propietario | `UserFollow` (FKs `follower_id`, `following_id`) |
| mappedBy | `User.following` → `mappedBy = "follower"` / `User.followers` → `mappedBy = "following"` |
| cascade | **Ninguna** — las relaciones UserFollow se gestionan explícitamente en el servicio |
| orphanRemoval | false |
| fetch | LAZY |
| Política | Al borrar User (soft delete), las relaciones UserFollow permanecen. Si se borra físicamente, hay que borrar previamente los UserFollow del usuario mediante `userFollowRepository.deleteByFollowerOrFollowing(user, user)`. |

### User ↔ Event (N:M)
| Propiedad | Valor |
|-----------|-------|
| Tipo | ManyToMany |
| Propietario | `Event` (tabla join `USER_EVENT`) |
| mappedBy | `Event.users` |
| cascade | Ninguna |
| fetch | LAZY |
| Política | Al borrar un Event, Spring limpia las filas de `USER_EVENT`. Al borrar un User, las inscripciones a eventos quedan huérfanas en la tabla JOIN — deben eliminarse antes en el servicio. |

---

## Dominio Catalog

### Work ↔ Author (N:M)
| Propiedad | Valor |
|-----------|-------|
| Tipo | ManyToMany |
| Propietario | `Work` (tabla join `WORK_AUTHOR`) |
| mappedBy | `Author.works` → `mappedBy = "authors"` |
| cascade | Ninguna |
| fetch | LAZY |
| Política | Borrar Work desasocia autores de `WORK_AUTHOR` (Spring borra las filas de la tabla join). No borra los Author. Borrar Author **no** elimina sus Works (lado inverso sin cascade). |

### Work → Edition (1:N)
| Propiedad | Valor |
|-----------|-------|
| Tipo | OneToMany |
| Propietario | `Edition` (FK `work_id`) |
| mappedBy | `Work.editions` → `mappedBy = "work"` |
| cascade | `CascadeType.ALL` |
| orphanRemoval | `true` |
| fetch | LAZY |
| Política | Borrar Work elimina en cascada todas sus Edition, que a su vez eliminan Tomes, Chapters, Volumes y Products (salvo bloqueo por OrderLines activas — ver `EditionHasOrderLinesException`). |

### Work → TrackingWork (1:N)
| Propiedad | Valor |
|-----------|-------|
| Tipo | OneToMany |
| Propietario | `TrackingWork` (FK `work_id`) |
| mappedBy | `Work.trackingWorks` → `mappedBy = "work"` |
| cascade | `CascadeType.ALL` |
| orphanRemoval | `true` |
| fetch | LAZY |

### Work → Comment (1:N)
| Propiedad | Valor |
|-----------|-------|
| Tipo | OneToMany |
| Propietario | `Comment` (FK `work_id`) |
| mappedBy | `Work.comments` → `mappedBy = "work"` |
| cascade | `CascadeType.ALL` |
| orphanRemoval | `true` |
| fetch | LAZY |
| Política | Borrar Work elimina en cascada todos los Comment raíz y sus replies y Reactions. |

### Edition → Editorial (N:1)
| Propiedad | Valor |
|-----------|-------|
| Tipo | ManyToOne |
| Propietario | `Edition` (FK `editorial_id`) |
| cascade | Ninguna |
| fetch | LAZY |
| Política | Borrar Editorial está **bloqueado** si tiene ediciones asociadas (`EditorialHasEditionsException`). |

### Edition → Tome (1:N)
| Propiedad | Valor |
|-----------|-------|
| Tipo | OneToMany |
| Propietario | `Tome` (FK `edition_id`) |
| mappedBy | `Edition.tomes` → `mappedBy = "edition"` |
| cascade | `CascadeType.ALL` |
| orphanRemoval | `true` |
| fetch | LAZY (default) |

### Edition → Volume (1:N)
| Propiedad | Valor |
|-----------|-------|
| Tipo | OneToMany |
| Propietario | `Volume` (FK `edition_id`) |
| mappedBy | `Edition.volumes` → `mappedBy = "edition"` (si existe) |
| cascade | `CascadeType.ALL` |
| orphanRemoval | `true` |
| fetch | LAZY |

### Edition → Product (1:N)
| Propiedad | Valor |
|-----------|-------|
| Tipo | OneToMany |
| Propietario | `Product` (FK `edition_id`) |
| mappedBy | `Edition.products` → `mappedBy = "edition"` |
| cascade | `CascadeType.ALL` |
| orphanRemoval | `true` |
| fetch | LAZY |
| Política | Borrar Edition está **bloqueado** si alguno de sus Products tiene OrderLines activas (`EditionHasOrderLinesException`). |

### Tome → Chapter (1:N)
| Propiedad | Valor |
|-----------|-------|
| Tipo | OneToMany |
| Propietario | `Chapter` (FK `tome_id`) |
| mappedBy | `Tome.chapters` → `mappedBy = "tome"` |
| cascade | `CascadeType.ALL` |
| orphanRemoval | `true` |
| fetch | LAZY |

### Product → OrderLine (1:N)
| Propiedad | Valor |
|-----------|-------|
| Tipo | OneToMany |
| Propietario | `OrderLine` (FK `product_id`, con FK explícita `fk_orderline_product`) |
| mappedBy | `Product.orderLines` → `mappedBy = "product"` |
| cascade | `PERSIST, MERGE` — **no DELETE** |
| orphanRemoval | false |
| fetch | LAZY |
| Política | Los OrderLine sobreviven al borrado del Product por integridad histórica del pedido. El borrado de Edition (padre) está bloqueado si hay OrderLines activas. |

---

## Dominio Social

### Comment autorrelación (parent/replies)
| Propiedad | Valor |
|-----------|-------|
| Tipo | ManyToOne (parent) + OneToMany (replies) |
| Propietario | `Comment` (FK `parent_id` nullable) |
| mappedBy | `Comment.replies` → `mappedBy = "parent"` |
| cascade (replies) | `CascadeType.ALL` |
| orphanRemoval | `true` |
| fetch | LAZY |
| Política | Borrar un Comment raíz elimina en cascada todas sus replies y sus Reactions. Se recomienda marcar como `[eliminado]` en lugar de borrar físicamente para no romper hilos. |

### Comment → Reaction (1:N)
| Propiedad | Valor |
|-----------|-------|
| Tipo | OneToMany |
| Propietario | `Reaction` (FK `comment_id`) |
| mappedBy | `Comment.reactions` → `mappedBy = "comment"` |
| cascade | `CascadeType.ALL` |
| orphanRemoval | `true` |
| Constraint | UNIQUE(`user_id`, `comment_id`) — un usuario, una reacción por comentario |

### Comment → User (N:1)
| Propiedad | Valor |
|-----------|-------|
| Tipo | ManyToOne |
| cascade | Ninguna |
| fetch | LAZY |
| Política | Si User se borra (soft delete), los comentarios quedan en BD con la referencia al User. Si se borra físicamente, la FK fallará salvo que se anonimicen primero los comentarios. |

### TrackingWork → User, Work (N:1)
| Propiedad | Valor |
|-----------|-------|
| Tipo | ManyToOne en ambos |
| cascade | Ninguna (los padres User y Work tienen cascade hacia TrackingWork) |
| fetch | LAZY |
| Constraint | UNIQUE(`user_id`, `work_id`) — un seguimiento por usuario/obra |

---

## Dominio Commerce

### Order → User (N:1)
| Propiedad | Valor |
|-----------|-------|
| Tipo | ManyToOne |
| cascade | Ninguna |
| fetch | EAGER (sin especificar → default ManyToOne) |
| Política | Los pedidos **no se borran** al borrar un User por trazabilidad histórica. Si se borra User físicamente, la FK fallará — se recomienda soft delete de User. |

### Order → OrderLine (1:N)
| Propiedad | Valor |
|-----------|-------|
| Tipo | OneToMany |
| Propietario | `OrderLine` (FK `order_id`) |
| mappedBy | `Order.orderLines` → `mappedBy = "order"` |
| cascade | `CascadeType.ALL` |
| orphanRemoval | `true` |
| Política | Borrar un pedido elimina en cascada todas sus líneas. |

### Order → TrackingOrder (1:N)
| Propiedad | Valor |
|-----------|-------|
| Tipo | OneToMany (si existe la relación mapeada en Order) |
| cascade | `CascadeType.ALL` (recomendado) |
| Política | Borrar Order elimina los registros de seguimiento logístico. |

---

## Restricciones de unicidad de base de datos

| Tabla | Columnas únicas |
|-------|----------------|
| APPUSER | `username`, `email` |
| EDITION | `isbn` |
| EDITORIAL | `name` |
| REACTION | (`user_id`, `comment_id`) |
| TRACKING_WORK | (`user_id`, `work_id`) — recomendado añadir |
| USER_FOLLOW | (`follower_id`, `following_id`) |
| CHAPTER | (`chapterNumber`, `tome_id`) |
| SUBSCRIPTION | `user_id` (OneToOne) |

---

## Orden seguro de borrado (sin CASCADE en JPA)

Para vaciar la BD sin errores de FK ejecutar en este orden:

1. REACTION
2. COMMENT (hojas primero, luego raíces — o borrar por CASCADE)
3. TRACKING_WORK
4. USER_EVENT (tabla join)
5. WORK_AUTHOR (tabla join)
6. ORDER_LINE
7. TRACKING_ORDER
8. ORDERS
9. PRODUCT
10. CHAPTER
11. TOME
12. VOLUME
13. EDITION
14. WORK
15. AUTHOR
16. EDITORIAL
17. SUBSCRIPTION
18. USER_FOLLOW
19. APPUSER
20. EVENT
