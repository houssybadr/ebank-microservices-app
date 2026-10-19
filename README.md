# EBank Microservices App

A full microservices demo that combines Spring Cloud, Spring AI, MCP, and an Angular
frontend. It is a synthesis of the concepts covered in the course:

- functional microservices: `customer-service` and `ebank-service`
- technical services: `discovery-service` (Eureka) and `gateway-service`
- synchronous REST communication with **OpenFeign**
- fault tolerance with **Resilience4j** (circuit breaker)
- an AI chatbot (`ebank-bot`) built with **Spring AI** (OpenAI) that reaches the
  business services through the **Model Context Protocol (MCP)**
- chatbot access from a web client, **Discord** and **Telegram**
- an **Angular 21** frontend

## Architecture

```
                 Angular 21            Discord            Telegram
                      |                   |                  |
                      v                   v                  v
                 +-----------------------------------------------+
                 |                   gateway-service             |
                 +-----------------------------------------------+
                          |                    |
              +-----------+-----+    +---------+-----------+
              | customer-service |    |      ebank-service  |
              |  (MCP server)    |<---|  (MCP server)       |
              +------------------+    |  OpenFeign -> cust  |
                          ^           |  Resilience4j       |
                          |           +---------------------+
                          |                     ^
                          |                     |
              +-----------+---------------------+-----------+
              |                  ebank-bot (Spring AI)      |
              |  MCP client + OpenAI + Discord + Telegram   |
              +---------------------------------------------+
                                   |
                           discovery-service (Eureka)
```

## Services

| Service            | Port | Description                                        |
|--------------------|------|----------------------------------------------------|
| discovery-service  | 8761 | Eureka service registry                            |
| gateway-service    | 9999 | reactive API gateway with dynamic routing + CORS   |
| customer-service   | 8081 | manages customers (JPA / H2), exposes MCP tools    |
| ebank-service      | 8082 | manages bank accounts, OpenFeign + Resilience4j, MCP tools |
| ebank-bot          | 8083 | Spring AI chatbot (OpenAI) + MCP client + connectors |

## Endpoints

- `GET http://localhost:9999/ebank-service/accounts` — list of bank accounts (via gateway)
- `GET http://localhost:9999/customer-service/customers` — list of customers
- `GET http://localhost:9999/ebank-bot/chat?query=...` — ask the chatbot
- `GET http://localhost:9999/ebank-bot/chat-stream?query=...` — streamed chatbot answer
- `http://localhost:4200/` — Angular frontend (`ng serve`)

## Configuration & secrets

Secrets are **not** committed. They are read from environment variables. A local
`.env` file (gitignored) is provided and loaded by `docker compose`.

| Variable            | Used by    |
|---------------------|------------|
| `OPENAI_API_KEY`    | ebank-bot  |
| `TELEGRAM_BOT_TOKEN`| ebank-bot  |
| `DISCORD_BOT_TOKEN` | ebank-bot  |

To run outside Docker:

```bash
set -a && source .env && set +a
mvn -pl discovery-service spring-boot:run   # then customer, ebank, gateway, bot
```

The Telegram/Discord connectors are enabled by default. If you only want the web
chatbot (and don't have Discord/Telegram tokens), set
`telegram.enabled=false` and `discord.enabled=false`.

## Run with Docker Compose

```bash
docker compose up --build
```

## Run locally

```bash
# 1. discovery-service
mvn -pl discovery-service spring-boot:run

# 2. customer-service and ebank-service
mvn -pl customer-service spring-boot:run
mvn -pl ebank-service spring-boot:run

# 3. gateway-service
mvn -pl gateway-service spring-boot:run

# 4. ebank-bot (needs OPENAI_API_KEY)
mvn -pl ebank-bot spring-boot:run
```

## Frontend

```bash
cd angular-frontend
npm install
ng serve
```

The frontend uses the `loading.interceptor` to show a spinner on every HTTP request,
and the bot component streams the assistant answer token by token.

## How the chatbot works

1. `customer-service` and `ebank-service` expose their business methods as **MCP tools**
   (`@Tool` annotations) over the streamable HTTP transport.
2. `ebank-bot` is an **MCP client**: at startup it discovers the tools exposed by both
   services.
3. The `EbankAiAgent` wires the tools, a chat memory and a system prompt into a
   `ChatClient`. When the user asks a question, the LLM decides which tool to call,
   retrieves the data and generates the answer.
4. The same agent is reachable from the REST controller, the Discord bot and the
   Telegram bot.
