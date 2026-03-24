package com.example.booksocial_backend.DTO.catalog;

/**
 * DTO de entrada para la creación y actualización de editoriales.
 *
 * Contiene los datos necesarios para registrar o modificar una editorial
 * dentro del sistema.
 *
 * @author Jorge
 * @since 23/03/2026
 */
public record CreateEditorialRequest(

    String name,

    String country

) {
}