# Printed Server

Серверная часть приложения Printed, разрабатываемого в рамках предмета "Разработка и анализ требований к ПО"

## Server Endpoints

### Authentication

1. #### Авторизация 

    - request type: POST
    - url: `/login`
    - body: 
        ```json
        { 
            "phoneNumber" : "номер телефона пользователя",
            "password" : "пароль пользователя"
        }
        ```
    - response: 
        ```json
        {
            "phonenumber" : "номер телефона пользователя",
            "token" : "токен авторизации пользователя"
        }
        ```

### Documents

1. #### Загрузка документа на сервер

    - request type: POST
    - url: `/documents`
    - request params: 
        ```
            document: (multipart/form-data)
            userId: (id пользователя)
        ```
    - possible response codes: 200, 500

2. #### Скачивание документа с сервера

    - request type: GET
    - url: `/documents/download/{document_id}`
    - path variable: `document_id` - id документа для скачивания
    - response:
        - 200: содержимое запрашиваемого файла
        - 404: файл не найден

3. #### Получение информации о документе

    - request type: GET
    - url: `/documents/{document_id}`
    - path variable: `document_id` - id документа
    - response:
        - 200:
            ```json
            {
                "id": "id документа", // int
                "size": "размер документа в байтах", // int
                "name": "название документа",
                "userId": "id пользователя, которому принадлежит файл", // int
                "pagesCount": "количество страниц в документе" // int
            }
            ```
        - 404: файл не найден

4. #### Получение документов, принадлежащих пользователю:

    - request type: GET
    - url: `/documents`
    - request parametrs:
        - `userId` - id пользователя, для которого получить список документов
    - response:
        - 200:
            ```json
            [
                {
                    "id": "id документа", // int
                    "size": "размер документа в байтах", // int
                    "name": "название документа",
                    "userId": "id пользователя, которому принадлежит файл", // int
                    "pagesCount": "количество страниц в документе" // int
                },
            ]
            ```
        - 404: пользователь не найден
    
5. #### Получение информации о точке печати 
    - request type: GET
    - url: `/spots`
    - request parameters:
        - `adminId` - ID администратора точки печати [GET]
    - response
        - 200:
            ```json
            {
                "id": "ID точки печати",
                "address": "адрес точки печати на русском языке",
                "status": "название текущего статуса точки печати на английском языке"
            }
            ```
        - 404: не найдена точка печати, администратором которой является пользователь с заданным ID
