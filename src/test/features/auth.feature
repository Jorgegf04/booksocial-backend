Feature: Autenticación y registro con JWT

  Scenario: Registro exitoso de un nuevo usuario
    When me registro con username "testuser1" email "testuser1@test.com" y contraseña "pass1234"
    Then el registro devuelve estado 200

  Scenario: Login con credenciales correctas devuelve JWT
    Given existe un usuario registrado con username "tokenuser" email "tokenuser@test.com" y contraseña "pass1234"
    When inicio sesión con username "tokenuser" y contraseña "pass1234"
    Then recibo un token JWT no vacío
    And el tipo de token es "Bearer"

  Scenario: Login con contraseña incorrecta devuelve 401
    When inicio sesión con username "admin" y contraseña "wrongpassword"
    Then el login devuelve estado 401
