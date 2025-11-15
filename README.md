# ORM Learning Platform - Система управления онлайн-обучением

Учебная платформа для онлайн-курсов с управлением курсами, заданиями и тестированием знаний.

## Архитектурные слои:
- **Controllers** - REST API endpoints
- **Services** - Бизнес-логика приложения (транзакции)
- **Repositories** - Доступ к данным (Spring Data JPA)
- **Entities** - Модели данных (JPA сущности + валидация)
- **DTOs** - Объекты передачи данных
- **Exceptions** - Обработка ошибок
- **Resources** - Конфигурация приложения

## Используемые переменные окружения:
```bash
# Настройки БД:
DB_URL=jdbc:postgresql://localhost:5432/orm_lms  # URL БД
DB_USERNAME=orm_lms                               # Пользователь БД
DB_PASSWORD=orm_lms                               # Пароль БД

# Приложение:
SPRING_PROFILES_ACTIVE=dev                        # Активный профиль (dev/test/prod)
```

## Maven профили и команды для сборки

**Профили:**
- `dev` - для разработки (PostgreSQL, демо-данные)
- `test` - для тестов (H2 база)
- `prod` - для продакшена (PostgreSQL)

### 1. Сборка:

**ПРОДАКШН**

Без тестов / с тестами:
```bash
mvn clean install -P prod -DskipTests
mvn clean install -P prod
```

**РАЗРАБОТКА**

Дефолтная сборка без тестов / с тестами:
```bash
mvn clean install -DskipTests
mvn clean install
```

**ТОЛЬКО ТЕСТЫ**

Unit + интеграционные тесты:
```bash
mvn test
mvn test -P test
```

**БЫСТРАЯ СБОРКА**

Полное отключение тестов:

PowerShell:
```powershell
mvn clean install -P prod "-Dmaven.test.skip=true"
```

Bash:
```bash
mvn clean install -P prod -Dmaven.test.skip=true
```

## Развернуть приложение в Docker контейнере

**Собираем:**
```bash
mvn clean install -P prod -DskipTests
```

**Разворачиваем:**
```bash
docker-compose up -d
```

**Остановка:**
```bash
docker-compose down
```

## API Endpoints

### Пользователи
- `GET /api/users/{id}` - Получить пользователя по ID
- `GET /api/users/email/{email}` - Получить пользователя по email
- `GET /api/users/role/{role}` - Получить пользователей по роли
- `POST /api/users` - Создать пользователя
- `PUT /api/users/{id}` - Обновить пользователя
- `DELETE /api/users/{id}` - Удалить пользователя

### Курсы
- `GET /api/courses/{id}` - Получить курс по ID
- `GET /api/courses/{id}/details` - Получить курс с модулями/уроками
- `GET /api/courses/teacher/{teacherId}` - Получить курсы преподавателя
- `GET /api/courses/category/{name}` - Получить курсы по категории
- `POST /api/courses` - Создать курс
- `POST /api/courses/{id}/modules` - Добавить модуль к курсу
- `POST /api/courses/{courseId}/modules/{moduleId}/lessons` - Добавить урок
- `PUT /api/courses/{id}` - Обновить курс
- `DELETE /api/courses/{id}` - Удалить курс

### Записи на курсы
- `POST /api/courses/{courseId}/enroll?studentId={id}` - Записать студента на курс
- `DELETE /api/courses/{courseId}/enroll?studentId={id}` - Отписать студента
- `GET /api/courses/{courseId}/students` - Получить студентов курса
- `GET /api/students/{studentId}/courses` - Получить курсы студента

### Задания
- `GET /api/assignments/{id}` - Получить задание по ID
- `GET /api/assignments/{id}/submissions` - Получить решения задания
- `POST /api/lessons/{lessonId}/assignments` - Создать задание для урока
- `POST /api/assignments/{id}/submit` - Отправить решение задания
- `POST /api/assignments/{id}/grade` - Оценить решение
- `DELETE /api/assignments/{id}` - Удалить задание

### Тесты
- `GET /api/quiz/{quizId}` - Получить тест по ID
- `GET /api/quiz/{quizId}/submissions` - Получить результаты теста
- `GET /api/students/{studentId}/quiz-submissions` - Результаты студента
- `POST /api/modules/{moduleId}/quiz` - Создать тест для модуля
- `POST /api/quiz/{quizId}/questions` - Добавить вопрос к тесту
- `POST /api/quiz/questions/{questionId}/options` - Добавить вариант ответа
- `POST /api/quiz/{quizId}/take` - Пройти тест

### Отзывы
- `GET /api/courses/{courseId}/reviews` - Получить отзывы курса
- `POST /api/courses/{courseId}/reviews` - Оставить отзыв о курсе

## Структура проекта

```
ORM-Learning-Platform/
├── src/
│   ├── main/
│   │   ├── java/ru/n1str/ormlms/
│   │   │   ├── api/                          # REST контроллеры
│   │   │   │   ├── dto/                      # DTO для запросов/ответов
│   │   │   │   ├── UserController.java
│   │   │   │   ├── CourseController.java
│   │   │   │   ├── EnrollmentController.java
│   │   │   │   ├── AssignmentController.java
│   │   │   │   ├── QuizController.java
│   │   │   │   ├── ReviewController.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── entity/                       # JPA сущности
│   │   │   │   ├── User.java                # Пользователь
│   │   │   │   ├── Profile.java             # Профиль
│   │   │   │   ├── Course.java              # Курс
│   │   │   │   ├── Category.java            # Категория
│   │   │   │   ├── Tag.java                 # Тег
│   │   │   │   ├── Module.java              # Модуль
│   │   │   │   ├── Lesson.java              # Урок
│   │   │   │   ├── Assignment.java          # Задание
│   │   │   │   ├── Submission.java          # Решение
│   │   │   │   ├── Enrollment.java          # Запись на курс
│   │   │   │   ├── Quiz.java                # Тест
│   │   │   │   ├── Question.java            # Вопрос
│   │   │   │   ├── AnswerOption.java        # Вариант ответа
│   │   │   │   ├── QuizSubmission.java      # Результат теста
│   │   │   │   └── CourseReview.java        # Отзыв
│   │   │   ├── model/                        # Enum-ы
│   │   │   │   ├── UserRole.java
│   │   │   │   ├── EnrollmentStatus.java
│   │   │   │   └── QuestionType.java
│   │   │   ├── repository/                   # Spring Data JPA репозитории
│   │   │   ├── service/                      # Бизнес-логика
│   │   │   ├── exception/                    # Обработка ошибок
│   │   │   ├── config/                       # Конфигурация
│   │   │   └── OrmLmsApplication.java        # Main класс
│   │   └── resources/
│   │       ├── application.yml               # Основная конфигурация
│   │       └── application-prod.yml          # Конфигурация для продакшена
│   └── test/
│       ├── java/ru/n1str/ormlms/             # Тесты
│       │   ├── CourseAndEnrollmentIntegrationTest.java
│       │   ├── AssignmentAndSubmissionIntegrationTest.java
│       │   ├── QuizIntegrationTest.java
│       │   ├── LazyLoadingTest.java
│       │   └── service/                      # Unit-тесты
│       └── resources/
│           └── application-test.yml          # Конфигурация для тестов (H2)
├── .github/workflows/ci.yml                  # GitHub Actions CI/CD
├── docker-compose.yml                        # Docker Compose
├── Dockerfile                                # Docker образ
├── pom.xml                                   # Maven конфигурация
└── README.md                                 # Документация
```

## Демо-данные

При запуске с профилем `dev` автоматически загружаются:
- 1 преподаватель, 2 студента
- 1 категория "Programming"
- 2 тега "Java", "Hibernate"
- 1 курс "Основы Hibernate"
- 1 модуль "Введение в ORM"
- 1 урок "Что такое Hibernate"
- 1 задание и 1 квиз

---

**Технологии:** Java 17, Spring Boot 3.3.4, Hibernate, PostgreSQL, Docker  
**Домен:** ru.n1str.ormlms  
**Версия:** 0.0.1-SNAPSHOT
