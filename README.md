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
            "token" : "токен авторизации пользователя",
            "id": "id пользователя",
            "name": "имя пользователя",
            "email": "адрес электронной почты пользователя",
            "accountNumber": "номер счёта пользователя",
            "expire": "длительность валидности токена в миллисекундах с момента аутентификации"
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

### Orders

1. #### Получение всех заказов пользователя
    - request type: GET
    - url: `/users/{userId}/orders/`
    - path variable:
        - `userId` - ID пользователя
    - response
        - 200:
            ```json
            [
                {
                    "id": "ID документа",
                    "createdAt": "дата создания заказа",
                    "doneAt": "дата выполнения заказа",
                    "receivedAt": "дата получения заказа клиентом",
                    "cost": "цена",
                    "location": "",
                    "radius": "", // int
                    "receiveOptionId": "",
                    "statusId": "",
                    "clientId": "",
                    "spotId": ""
                },
                ...
            ]
            
            ```

2. #### Получение конкретного заказа пользователя
    - request type: GET
    - url: `/users/{userId}/orders/{orderId}`
    - path variable:
        - `userId` - ID пользователя
        - `orderId` - ID заказа
    - response
        - 200:
            ```json
                {
                    "id": "ID документа",
                    "createdAt": "дата создания заказа",
                    "doneAt": "дата выполнения заказа",
                    "receivedAt": "дата получения заказа клиентом",
                    "cost": "цена",
                    "location": "",
                    "radius": "", // int
                    "receiveOptionId": "",
                    "statusId": "",
                    "clientId": "",
                    "spotId": ""
                }
            ```

3. #### Получение документов из заказа
    - request type: GET
    - url: `/orders/{orderId}/documents`
    - path variable:
        - `orderId` - ID заказа
    - response
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
                ...
             ]
            ```
4. #### Оформление нового заказа
    - request type: GET
    - url: `/orders/{orderId}/documents`
    - path variable:
        - `orderId` - ID заказа
    - body:
        ```json
             {
                    "createdAt": "дата создания заказа",
                    "doneAt": "дата выполнения заказа",
                    "receivedAt": "дата получения заказа клиентом",
                    "cost": "цена",
                    "latitude": "",
                    "longitude": "",
                    "radius": "", // int
                    "receiveOption": "",
                    "status": "",
                    "userId": "",
            }
        ```
    - response
        - 404
            
5. #### Получение информации о заказах с определённым статусом на определённой точке печати
    - request type: GET
    - url: `/spots/{spotId}/orders?status=`
    - request parameters:
        - `spotId` - ID точки печати
        - GET parameters:
            - status: строковый статус точки печати. Возможные статусы: inwork, ready, received
    - response:
        - 200:
            ```json
                [
                    {
                        "id": 1,
                        "cost": 117.0,
                        "createdAt": "2019-11-05 07:05:33",
                        "doneAt": "2019-11-05 07:32:04",
                        "receivedAt": "2019-11-05 07:05:33",
                        "receiveOption": "university",
                        "status": "received",
                        "clientId": 2
                    },
                ]
            ```
        - 404: не найдены заказы с определённым статусом на заданной точке печати

6. #### Получение информации о всех размещённых (не взятых в работу) заказах
    - request type: GET
    - url: `/orders/placed`
    - response:
        - 200:
            ```json
                [
                    {
                        "id": 19,
                        "cost": 15.0,
                        "createdAt": "2020-01-11 16:15:10",
                        "doneAt": null,
                        "receivedAt": "2020-01-11 16:15:10",
                        "receiveOption": "personal",
                        "status": "placed",
                        "spotId": null
                    },
                    {
                        "id": 20,
                        "cost": 15.0,
                        "createdAt": "2020-01-11 16:15:10",
                        "doneAt": null,
                        "receivedAt": "2020-01-11 16:15:10",
                        "receiveOption": "personal",
                        "status": "placed",
                        "spotId": null
                    }
                ]
            ```
        - 404: не найдены размещённые заказы

7. #### Получение информации о всех размещённых (не взятых в работу) заказах
    - request type: DELETE
    - url: `/orders/{orderId}`
    - path variable:
        - `{orderId}` - id заказа
    - response:
        - 204: успешно удалено
        - 404: заказ не найден

### Spots

1. #### Получение информации о точке печати 
    - request type: GET
    - url: `/spots/{spotId}`
    - path variables:
        - `spotId` - id точки печати
    - response
        - 200:
            ```json
            {
                "id": "ID точки печати",
                "address": "адрес точки печати на русском языке",
                "status": "название текущего статуса точки печати на английском языке"
                "name": "название точки печати?"
            }
            ```
        - 404: не найдена точка печати с данным id

2. #### Создание точки печати
    - request type: POST
    - url: `/spots`
    - body: 
        ```json
            {
                "address": "адрес точки печати на русском языке",
                "location": "долгота и широта?", // double[]
                "status": "название текущего статуса точки печати на английском языке"
            }
         ```
    - response
        - 201: точка печати создана успешно
        - 400: ошибка в создании точки

3. #### Обновление информации о точки печати
    - request type: PATCH
    - url: `/spots/{spotId}`
    - path variables:
        - `spotId` - id точки печати
    - body:
        ```json
            {
                "address": "адрес точки печати на русском языке",
                "location": "долгота и широта?", // double[]
                "status": "название текущего статуса точки печати на английском языке"
            }
        ```
    - response
        - 204: обновлено успешно
        - 403: метод был вызван не админом точки печати
        - 409: ошибка в обновлении точки точки

4. #### Получение точек печати для конкретного администратора
    - request type: GET
    - url: `/spots`
    - request paramentrs:
        - `adminId` - id администратора точки печати
    - response
        - 200:
            ```json
            [
                {
                    "id": "ID точки печати",
                    "address": "адрес точки печати на русском языке",
                    "status": "название текущего статуса точки печати на английском языке"
                    "name": "название точки печати?"
                },
                ...
            ]
            ```
        - 404: не найдены точки печати с заданным администратором

5. #### Удаление точки печати
    - request type: DELETE
    - url: `/spots/{spotId}`
    - path variables:
        - `spotId` - id точки печати
    - response
        - 204: удалено успешно
        - 404: точка печати не найдена

### Users

1. #### Получить информацию о пользователе
    - request type: GET
    - url: `users/{user_id}`
    - path variables:
        `user_id` - id пользователя
    - response:
        - 200: 
            ```json
            {
                "Id": "id пользователя", // int
                "name": "имя пользователя",
                "email": "email пользователя",
                "phoneNumber": "номер телефона",
                "accountNumber": "" // int
            }
            ```
        - 404: пользователь с заданным id не найден

2. #### Обновить информацию о пользователе
    - request type: PATCH
    - url: `users/{user_id}`
    - path variables: 
        -  `user_id` - id пользователя
    - body:
        ```json
        {
            "email": "email пользователя",
            "phoneNumber": "номер телефона"
        }
        ```
    - response:
        - 204: информация обновлена успешно
        - 400: ошибка при обновлении
        - 409: ?

3. #### Удаление пользователя
    - request type: PATCH
    - url: `users/{user_id}`
    - path variables: 
        -  `user_id` - id пользователя
    - response: 
        - 501: `¯\_(ツ)_/¯`

### Accounts

1. #### Получение данных об аккаунте
    - request type: GET
    - url: `/accounts/{accountId}`
    - path variables:
        `accountId` - id аккаунта
    - response:
        - 200: 
            ```json
            {
                "balance": "баланс", // Decimal
                "rememberCard": "?", // bool
                "cardNumberCut": "?",
            }
            ```
        - 404: аккаунт не найден

2. #### Обновление аккаунта
    - request type: PATCH
    - url: `/users/{userId}/account`
    - path variables:
        `userId` - id пользователя
    - body:
            ```json
            {
                "balanceChange": "изменение баланса", // Double
                "rememberCard": "?", // bool
                "cardNumberCut": "?",
            }
            ```
    - response:
        - 204: обновление аккаунта успешно
        - 403: авторизованный пользователь пытается изменить не свой аккаунт
        - 409: что-то пошло не так
