# Docker Development Guide

## Быстрый старт

### 1. Подготовка окружения

Создайте файл `.env` в корне проекта на основе `.env.example`:

```bash
cp .env.example .env
```

Отредактируйте `.env` и укажите ваши настройки email:

```env
EMAIL_USERNAME=your_email@gmail.com
EMAIL_PASSWORD=your_app_password
JWT_SECRET=your_secret_key_here
```

**Важно**: Для Gmail используйте [App Password](https://myaccount.google.com/apppasswords), а не обычный пароль.

### 2. Запуск приложения

```bash
docker-compose up --build
```

Приложение будет доступно по адресу: `http://localhost:8080`

### 3. Остановка приложения

```bash
docker-compose down
```

Для удаления данных БД:
```bash
docker-compose down -v
```

## Доступные сервисы

- **API**: http://localhost:8080
- **MySQL**: localhost:3306
  - Database: `coffee_db`
  - User: `coffee_user`
  - Password: `coffee_password`

## Полезные команды

### Просмотр логов
```bash
# Все сервисы
docker-compose logs -f

# Только приложение
docker-compose logs -f app

# Только MySQL
docker-compose logs -f mysql
```

### Перезапуск сервисов
```bash
# Перезапустить все
docker-compose restart

# Только приложение
docker-compose restart app
```

### Подключение к MySQL
```bash
docker exec -it coffee_mysql mysql -u coffee_user -p
# Пароль: coffee_password
```

### Пересборка приложения
```bash
# Если изменился код
docker-compose up --build app
```

### Очистка
```bash
# Остановить и удалить контейнеры
docker-compose down

# Остановить и удалить контейнеры + volumes (БД будет очищена)
docker-compose down -v

# Удалить образы
docker-compose down --rmi all
```

## Разработка

### Локальная разработка с Docker БД

Если хотите запускать приложение локально (IDE), но использовать Docker для MySQL:

```bash
# Запустить только MySQL
docker-compose up mysql

# В IDE установите переменные окружения:
DB_URL=jdbc:mysql://localhost:3306/coffee_db?createDatabaseIfNotExist=true
DB_USERNAME=coffee_user
DB_PASSWORD=coffee_password
```

## Troubleshooting

### MySQL не запускается
```bash
# Проверьте логи
docker-compose logs mysql

# Попробуйте очистить volume
docker-compose down -v
docker-compose up
```

### Приложение не может подключиться к БД
- Убедитесь, что MySQL healthcheck проходит успешно
- Проверьте логи: `docker-compose logs mysql`
- MySQL должен запуститься до приложения (это настроено через `depends_on`)

### Email не отправляется
- Проверьте правильность `EMAIL_USERNAME` и `EMAIL_PASSWORD` в `.env`
- Для Gmail: используйте App Password, включите 2FA
- Проверьте логи: `docker-compose logs app`

### Порты заняты
Если порты 8080 или 3306 уже заняты, измените их в `docker-compose.yml`:

```yaml
ports:
  - "8081:8080"  # Для приложения
  - "3307:3306"  # Для MySQL
```
