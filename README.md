# T-Bank Invest API SDK

[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![gRPC](https://img.shields.io/badge/gRPC-1.83.x-blue.svg)](https://grpc.io/)
[![License](https://img.shields.io/badge/License-Apache%202.0-green.svg)](LICENSE)

Готовый к использованию SDK для **T-Bank Invest API**. Библиотека объединяет **gRPC-** и **REST-клиенты**, сгенерированные из официальных контрактов `.proto` и `OpenAPI`, и предоставляет их как Spring-бины через механизм авто-конфигурации.

## 🚀 Возможности

SDK автоматически:

1. Генерирует gRPC-стабы всех сервисов T-Bank Invest API (Instruments, MarketData, Orders, Operations, Sandbox, StopOrders, Users, а также stream-сервисы)
2. Генерирует REST-клиенты из OpenAPI-спецификации T-Bank (`tinkoff-swagger.yaml`)
3. Регистрирует все стабы и API-клиенты как Spring-бины через `@AutoConfiguration`
4. Настраивает gRPC-каналы и HTTP-клиент (Apache HttpClient 5) с оптимальными пулами соединений и таймаутами
5. Позволяет независимо включать и отключать gRPC- и REST-части

## 📦 Подключение

### 1. Настройка репозитория (GitHub Packages)

Добавьте в `build.gradle.kts`:

    repositories {
        mavenCentral()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/isToxic/t-bank-sdk")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }

### 2. Добавление зависимости

    dependencies {
        implementation("com.github.isToxic:t-bank-sdk:1.0.0")
    }

Все необходимые транзитивные зависимости (`spring-boot-starter-grpc-client`, `spring-boot-starter-web`, `httpclient5`, `protobuf-java`, `grpc-*`, `jackson-datatype-jsr310`, `jackson-databind-nullable`, `jakarta.annotation-api`, `resilience4j-circuitbreaker`, `resilience4j-ratelimiter`) подтягиваются автоматически.

## ⚙️ Настройка

### Обязательные properties

В `application.yml`:

    platforms:
      t-bank:
        url: https://invest-public-api.tinkoff.ru/rest   # REST endpoint
        token: ${TINKOFF_TOKEN}                          # Bearer-токен

    spring:
      grpc:
        client:
          channel:
            t-bank:
              target: static://invest-public-api.tinkoff.ru:443

#### Контракт REST-свойств

Свойства `platforms.t-bank.url` и `platforms.t-bank.token` читаются через `@ConfigurationProperties` в классе `TinkoffProperties` (prefix = `platforms.t-bank`). Это даёт IDE-подсказки и явно документирует контракт:

| Свойство              | Тип   | Описание                          |
| --------------------- | ----- | --------------------------------- |
| `platforms.t-bank.url`    | `String` | Базовый URL REST-эндпоинта T-Bank |
| `platforms.t-bank.token`  | `String` | Bearer-токен (инжектируется в `ApiClient`) |

#### Контракт gRPC-канала

Все gRPC-конфиги используют `@ImportGrpcClients(target = "t-bank", ...)`. Это означает, что потребитель должен настроить канал с именем `t-bank`:

    spring:
      grpc:
        client:
          channel:
            t-bank:
              target: static://invest-public-api.tinkoff.ru:443

Где:
- `t-bank` — имя канала, которое используется как `target` в `@ImportGrpcClients`
- `target` — адрес gRPC-сервера (формат Spring Cloud gRPC `static://host:port`)

### Отключение отдельных частей

Чтобы использовать только REST или только gRPC, отключите соответствующие автоконфигурации через Spring Boot `spring.config.import` или явно исключите пакеты `is.toxic.tbank.sdk.rest` / `is.toxic.tbank.sdk.proto` из сканирования компонентов.

### Рубильники (Resilience4j)

SDK включает **Circuit Breaker** и **Rate Limiter** на основе [Resilience4j](https://resilience4j.readme.io/), которые применяются ко всем REST-вызовам `ApiClient` автоматически через `ClientHttpRequestInterceptor`.

#### Официальные лимиты T-Bank Invest API

Лимиты зависят от грейда пользователя (определяется по количеству и проценту исполнения заявок за 30 дней). Полная таблица: [tinkoff.github.io/investAPI/limits](https://tinkoff.github.io/investAPI/limits/).

| Сервис              | gRPC (запросов/мин) | REST (запросов/мин) |
| ------------------- | ------------------- | ------------------- |
| Instruments         | 200                 | 100                 |
| Users               | 100                 | 50                  |
| Operations          | 200                 | 100                 |
| MarketData          | 300                 | 150                 |
| StopOrders          | 50                  | 25                  |
| Sandbox             | 200                 | 100                 |
| Orders              | 100                 | 50                  |
| Orders / postOrder  | 300                 | 150                 |
| Orders / cancelOrder| 100                 | 50                  |
| Operations / report | 5                   | —                   |
| IP-потолок          | 1000                | 1000                |

Также есть ограничения на активные stream-соединения (до 16 для грейда 4–5, не ограничено для 5) и 300 одновременных подписок на свечи/стаканы/МДС в одном стриме сервиса котировок.

#### Настройка по умолчанию

По умолчанию SDK использует значения, соответствующие официальным REST-лимитам T-Bank (запросов/мин) и порогу срабатывания circuit breaker 50% ошибок:

| Сервис      | Лимит REST (запросов/мин) | Circuit Breaker (50% ошибок, минимум 10 вызовов, окно 30 с) |
| ----------- | ------------------------- | ------------------------------------------------------------ |
| instruments | 100                       | 50% / 10 / 30 с / окно 20                                   |
| users       | 50                        | 50% / 10 / 30 с / окно 20                                   |
| operations  | 100                       | 50% / 10 / 30 с / окно 20                                   |
| marketdata  | 150                       | 50% / 10 / 30 с / окно 20                                   |
| stoporders  | 25                        | 50% / 10 / 30 с / окно 20                                   |
| sandbox     | 100                       | 50% / 10 / 30 с / окно 20                                   |
| orders      | 50                        | 50% / 10 / 30 с / окно 20                                   |
| signal      | 50                        | 50% / 10 / 30 с / окно 20                                   |

#### Кастомизация

Лимиты настраивать можно под каждый сервис отдельно через `platforms.t-bank.resilience.services.<name>.*`. Значения, переданные пользователем, перекрывают дефолтные:

    platforms:
      t-bank:
        url: https://invest-public-api.tinkoff.ru/rest
        token: ${TINKOFF_TOKEN}
        resilience:
          services:
            instruments:
              rateLimiter:
                limitForPeriod: 200          # под грейд 2 для Instruments
                limitRefreshPeriodMinutes: 1
                timeoutSeconds: 5
              circuitBreaker:
                failureRateThreshold: 50.0
                minimumNumberOfCalls: 10
                waitDurationInSeconds: 30
                slidingWindowSize: 20
            orders:
              rateLimiter:
                limitForPeriod: 150          # под postOrder
                limitRefreshPeriodMinutes: 1
                timeoutSeconds: 5
              circuitBreaker:
                failureRateThreshold: 50.0
                minimumNumberOfCalls: 10
                waitDurationInSeconds: 30
                slidingWindowSize: 20

#### Mapping URL → сервис

По умолчанию interceptor сопоставляет путь запроса к сервису по префиксу:

| Префикс пути   | Сервис      |
| -------------- | ----------- |
| `/instruments` | instruments |
| `/marketdata`  | marketdata  |
| `/operations`  | operations  |
| `/orders`      | orders      |
| `/sandbox`     | sandbox     |
| `/users`       | users       |
| `/stoporders`  | stoporders  |
| `/signal`      | signal      |

Настройку можно изменить через `platforms.t-bank.resilience.pathMapping`:

    platforms:
      t-bank:
        resilience:
          pathMapping:
            "/custom/instruments": "instruments"

#### Использование в коде

Бины `CircuitBreaker` и `RateLimiter` для каждого сервиса зарегистрированы как Spring-бины (в виде `Map<String, CircuitBreaker>` и `Map<String, RateLimiter>`) и могут быть внедрены в любой компонент. Применяются автоматически ко всем REST-вызовам `ApiClient`, но при необходимости можно использовать и вручную:

    @Service
    @RequiredArgsConstructor
    public class InstrumentsFacade {

        private final Map<String, CircuitBreaker> circuitBreakers;
        private final Map<String, RateLimiter> rateLimiters;
        private final InstrumentsServiceApi instrumentsServiceApi;

        public BondsResponse getBonds() {
            CircuitBreaker cb = circuitBreakers.get("instruments");
            RateLimiter rl = rateLimiters.get("instruments");
            Supplier<BondsResponse> supplier = Decorators.ofSupplier(
                () -> instrumentsServiceApi.bonds(new BondsRequest())
            )
            .withCircuitBreaker(cb)
            .withRateLimiter(rl)
            .get();
            return supplier.get();
        }
    }

## 💡 Примеры использования

### gRPC — блокирующий клиент

    @Service
    public class InstrumentsFacade {

        private final InstrumentsServiceGrpc.InstrumentsServiceBlockingStub instrumentsStub;

        public InstrumentsFacade(InstrumentsServiceGrpc.InstrumentsServiceBlockingStub instrumentsStub) {
            this.instrumentsStub = instrumentsStub;
        }

        public BondsResponse getBonds() {
            return instrumentsStub.bonds(InstrumentsRequest.getDefaultInstance());
        }
    }

### gRPC — асинхронный клиент и стримы

Все четыре типа стабов (`BlockingStub`, `BlockingV2Stub`, `FutureStub`, `Stub`) зарегистрированы как отдельные бины:

    @Service
    @RequiredArgsConstructor
    public class MarketDataStreamFacade {

        private final MarketDataStreamServiceGrpc.MarketDataStreamServiceStub streamStub;

        public void subscribe(StreamObserver<MarketDataResponse> observer) {
            StreamObserver<MarketDataRequest> requestObserver = streamStub.marketDataStream(observer);
            // ... подписки
        }
    }

### REST — OpenAPI-клиент

Каждый сервис T-Bank доступен как отдельный Spring-бин:

    @Service
    @RequiredArgsConstructor
    public class OperationsFacade {

        private final OperationsServiceApi operationsServiceApi;

        public PortfolioResponse getPortfolio(String accountId) {
            return operationsServiceApi.operationsServiceGetPortfolio(
                new V1PortfolioRequest().accountId(accountId)
            );
        }
    }

Все API-клиенты внедряются без дополнительной настройки: `InstrumentsServiceApi`, `MarketDataServiceApi`, `OrdersServiceApi`, `OperationsServiceApi`, `SandboxServiceApi`, `StopOrdersServiceApi`, `UsersServiceApi`, `SignalServiceApi`.

## 🛠 Требования

- **Java**: 25+
- **Spring Boot**: 4.1.x
- **gRPC**: 1.83.x
- **Protobuf**: 4.35.x

## 🔧 Сборка и публикация (для контрибьюторов)

    ./gradlew clean build

Публикация в GitHub Packages:

    ./gradlew publish -PGH_USERNAME=<user> -PGH_TOKEN=<token>

Версия управляется в `libs.versions.toml`. Для публикации из CI рекомендуется передавать её явно:

    ./gradlew publish -Pversion=$GITHUB_REF_NAME

## 📄 Лицензия

Apache License 2.0. См. файл [LICENSE](LICENSE).

## 👤 Автор

**Stepan Mokrov**  
GitHub: [@isToxic](https://github.com/isToxic)
Telegram: [@istoxic](https://t.me/istoxic)
