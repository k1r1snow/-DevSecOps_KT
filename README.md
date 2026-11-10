# Сервис задач

Учебный проект на Java 25 и Spring Boot. `task-api` хранит задачи в PostgreSQL, а при создании и изменении отправляет уведомление и увеличивает счётчик аналитики.

## Структура

```text
.
|-- task-api/             API задач и Dockerfile
|-- notification-service/ список уведомлений и Dockerfile
|-- analytics-service/    счётчик и Dockerfile
`-- docker-compose.yml    запуск сервисов и PostgreSQL
```

PostgreSQL 16 работает как сервис `db`; данные задач сохраняются в Docker volume `db_data`.

Каждая задача содержит только `id` и `title`. Уведомления и счётчик очищаются при перезапуске соответствующего сервиса.

## Запуск одного контейнера

Этот вариант нужен для простой проверки Dockerfile. В нём задачи хранятся во временной H2, поэтому данные пропадут после остановки контейнера.

```bash
docker build -t task-api ./task-api
docker run --rm --name task-api-demo -p 8080:8080 task-api
```

В другом терминале проверьте API и контейнер:

```bash
curl.exe http://localhost:8080/health
docker ps
docker logs task-api-demo
```

Остановите контейнер сочетанием `Ctrl+C`.

## Запуск всей системы

Из корня проекта запустите PostgreSQL и все три сервиса:

```bash
docker compose up -d --build
```

Сервисы доступны на `http://localhost:8080` (задачи), `http://localhost:8081` (аналитика) и `http://localhost:8082` (уведомления). В PowerShell используйте `curl.exe`, чтобы избежать алиаса `curl`.

Состояние и логи контейнеров:

```bash
docker compose ps
docker compose logs
```

Проверить состояние API:

```bash
curl.exe http://localhost:8080/health
```

## Примеры запросов

Создать задачу:

```bash
curl.exe -X POST http://localhost:8080/tasks -H "Content-Type: application/json" -d '{"title":"Изучить Java"}'
```

Получить список:

```bash
curl.exe http://localhost:8080/tasks
```

Изменить задачу (замените `1` на ID задачи):

```bash
curl.exe -X PUT http://localhost:8080/tasks/1 -H "Content-Type: application/json" -d '{"title":"Задача выполнена"}'
```

Удалить задачу:

```bash
curl.exe -X DELETE http://localhost:8080/tasks/1
```

Проверить накопленные уведомления и счётчик после создания или изменения задач:

```bash
curl.exe http://localhost:8082/notifications
curl.exe http://localhost:8081/analytics
```

Остановить сервисы, сохранив данные PostgreSQL:

```bash
docker compose down
docker compose up -d
curl.exe http://localhost:8080/tasks
```

После повторного запуска список задач должен сохраниться благодаря `db_data`. Не добавляйте `-v` к `docker compose down`: этот параметр удалит volume с данными.

Чтобы удалить данные PostgreSQL полностью, выполните `docker compose down -v`.
