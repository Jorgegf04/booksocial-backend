Feature: Gestión de obras

  Scenario: Crear una nueva obra
    When creo una obra con título "Berserk" tipo "MANGA" y género "ACTION"
    Then la respuesta de creación tiene estado 201
    And la obra devuelta tiene título "Berserk"

  Scenario: Listar todas las obras devuelve 200
    When solicito el listado completo de obras
    Then la respuesta del listado tiene estado 200

  Scenario: Buscar obra por título existente
    Given existe una obra creada con título "Vinland Saga" tipo "MANGA" y género "ACTION"
    When busco obras por título "Vinland"
    Then la búsqueda devuelve al menos una obra
    And una de las obras tiene título "Vinland Saga"