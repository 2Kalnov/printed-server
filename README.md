# Printed Server

Серверная часть приложения Printed, разрабатываемого в рамках предмета "Разработка и анализ требований к ПО"

## Server Endpoints

### Authentication

- url: `/login`
- body: 
    ```{ 
        "phoneNumber" : <номер телефона пользователя>,
        "password" : <пароль пользователя>
    }```
- response: 
    ```{
        "phonenumber" : <номер телефона пользователя>.
        "token" : <токен авторизации пользователя>
    }```