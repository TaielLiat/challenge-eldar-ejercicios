
# ELDAR CHALLENGE

Ejercicios 1 y 2 del challenge: Configuración local del repositorio.




Para implementar este proyecto:

```bash
  git clone https://github.com/TaielLiat/challenge-eldar-ejercicios.git
```
```bash
  cd challenge-eldar-ejercicios/ejercicio2
```

```bash
  mvn clean install
```

```bash
  mvn spring-boot:run
```

##


## Variables de Entorno

Para correr este proyecto, vas a necesitar modificar los valores del `application.properties` o introducir estas variables en un archivo .env

`DATABASE_URL`

`DATABASE_USERNAME`

`DATABASE_PASSWORD`

`MAIL_HOST`

`MAIL_PORT`

`MAIL_USERNAME`

`MAIL_PASSWORD`

`MAIL_FROM`

`SECRET_KEY` 

##

Para visualizar los endpoints podés ingresar a:
http://localhost:8080/swagger-ui/index.html#/

Algunos valores manuales de prueba:

POST: USER
```bash
{
  "id": "42a8f0f5-cdb3-436b-a34c-58f0e62d0c10",
  "name": "Martin Colapinto",
  "email": "martincolapinto@gmail.com",
  "dni": "53600397",
  "birthDate": "1990-01-01",
  "cards": null
}
```
POST: CARD
```bash
 {
  "id": "2d49bb0a-94b8-41ea-a4f7-2bfa97337417",
  "cardNumber": "7041100933440973",
  "brand": "VISA",
  "expiryDate": "2025-09-20",
  "cvv": "123",
  "expired": false
}
```
POST: OPERATION
```bash
{
  "card": {
    "id": "2d49bb0a-94b8-41ea-a4f7-2bfa97337417",
    "cardNumber": "7041100933440973",
    "brand": "VISA",
    "expiryDate": "2025-09-20",
    "cvv": "123",
    "expired": false
  },
  "user": {
    "id": "42a8f0f5-cdb3-436b-a34c-58f0e62d0c10",  
    "name": "Martin Colapinto",
    "email": "martincolapinto@gmail.com",
    "dni": "53600397",
    "birthDate": "1990-01-01"
  },
  "amount": 5000,
  "date": "2024-09-20"
}
```
