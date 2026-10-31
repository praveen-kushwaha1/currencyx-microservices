# CurrencyX Microservices – Local Setup and Execution Guide

## 1. Project Overview

**GitHub Repository:**  
https://github.com/praveen-kushwaha1/currencyx-microservices

**Purpose:**  
This document explains how to clone, configure, build, run, and test the CurrencyX microservices project on a local Windows machine.

### Technology Stack

| Technology | Purpose |
|---|---|
| Java | Programming language |
| Spring Boot | Microservice development |
| Spring Cloud | Microservices infrastructure |
| Spring Cloud Gateway | API Gateway |
| Netflix Eureka | Service discovery |
| Spring Cloud Config | Centralized configuration |
| OpenFeign | Synchronous service communication |
| Resilience4j | Fault tolerance |
| Prometheus | Metrics collection |
| Grafana | Metrics visualization |
| Micrometer + Brave | Distributed tracing |
| Zipkin | Trace visualization |
| Elasticsearch | Log storage and search |
| Logstash | Log collection and processing |
| Kibana | Log visualization |
| Docker Desktop | Infrastructure containers |
| Maven | Build and dependency management |

## 2. Project Architecture

### Microservices

| Application | Port | Responsibility |
|---|---:|---|
| CurrencyX API Gateway | 8080 | Entry point for API requests |
| Currency Conversion Service | 8082 | Currency conversion |
| Currency Exchange Service | 8081 | Exchange rate management |
| Eureka Server | 8761 | Service discovery |
| Config Server | 8888 | Centralized configuration |

### Monitoring and Observability

| Application | Port | Responsibility |
|---|---:|---|
| Prometheus | 9090 | Metrics collection |
| Grafana | 3000 | Monitoring dashboards |
| Zipkin | 9411 | Distributed tracing |
| Elasticsearch | 9200 | Centralized log storage |
| Logstash | 5000 | Log ingestion |
| Kibana | 5601 | Centralized log searching |
| Redis | 6379 | Separate infrastructure container; not required by current CurrencyX configuration |

## 3. Prerequisites

Install the following software before cloning the project.

- Git
- Java JDK (use the Java version configured by the project)
- IntelliJ IDEA
- Maven (or use the Maven Wrapper if included)
- Docker Desktop
- Postman or another REST API client

### Verify installations

Open PowerShell or Git Bash:

```bash
git --version
java -version
javac -version
docker --version
docker compose version
```

Check Java configuration:

```bash
echo %JAVA_HOME%
```

For PowerShell:

```powershell
$env:JAVA_HOME
```

Make sure IntelliJ uses the same compatible JDK as Maven.

## 4. Clone the GitHub Repository

Open PowerShell or Git Bash.

Navigate to your preferred development directory:

```bash
cd C:/Users/prave/Repo/Training
```

Clone the project:

```bash
git clone https://github.com/praveen-kushwaha1/currencyx-microservices.git
```

Navigate into the repository:

```bash
cd currencyx-microservices
```

Verify the repository:

```bash
git status
git branch
git remote -v
```

Expected remote:

```text
origin
https://github.com/praveen-kushwaha1/currencyx-microservices.git
```

## 5. Import the Project into IntelliJ IDEA

1. Open IntelliJ IDEA.
2. Select **File → Open**.
3. Select the cloned `currencyx-microservices` root folder.
4. Allow IntelliJ to import the Maven projects.
5. Wait for dependency downloads and indexing.
6. Check Project SDK and Maven JDK.
7. Verify that all modules are imported successfully.

Recommended project layout:

```text
currencyx-microservices/
│
├── currencyx-api-gateway/
├── currency-conversion-service/
├── currency-exchange-service/
├── currencyx-config-server/
├── currencyx-eureka-server/
│
└── monitoring/
    ├── prometheus/
    ├── grafana/
    └── elk/
        ├── docker-compose.yml
        └── logstash/
            └── pipeline/
                └── logstash.conf
```

The exact folders may evolve as new features are added.

## 6. Start Docker Infrastructure

The monitoring containers are already available in Docker Desktop.

Current containers:

| Container | Image | Port |
|---|---|---:|
| flight-grafana | grafana/grafana | 3000 |
| flight-prometheus | prom/prometheus | 9090 |
| zipkin | openzipkin/zipkin:latest | 9411 |
| currencyx-elasticsearch | elasticsearch:8.15.0 | 9200 |
| currencyx-kibana | kibana:8.15.0 | 5601 |
| currencyx-logstash | logstash:8.15.0 | 5000 |

**Important:** These containers are already running. Do not create duplicate containers unnecessarily.

Verify:

```bash
docker ps
```

Check ELK logs:

```bash
docker logs currencyx-elasticsearch --tail 50
docker logs currencyx-logstash --tail 50
docker logs currencyx-kibana --tail 50
```

Check Zipkin:

```text
http://localhost:9411
```

Check Prometheus:

```text
http://localhost:9090
```

Check Grafana:

```text
http://localhost:3000
```

Check Elasticsearch:

```text
http://localhost:9200
```

Check Kibana:

```text
http://localhost:5601
```

If any required container is stopped, start it:

```bash
docker start flight-grafana
docker start flight-prometheus
docker start zipkin
docker start currencyx-elasticsearch
docker start currencyx-kibana
docker start currencyx-logstash
```

Redis is not required for the current CurrencyX setup.

### Important: Prometheus Configuration

The existing Prometheus container uses a configuration mounted from the separate monitoring assignment.

Preserve its existing flight-booking jobs and ensure it also contains the CurrencyX scrape targets:

```yaml
scrape_configs:
  - job_name: "currencyx-api-gateway"
    metrics_path: "/actuator/prometheus"
    static_configs:
      - targets: ["host.docker.internal:8080"]

  - job_name: "currencyx-exchange-service"
    metrics_path: "/actuator/prometheus"
    static_configs:
      - targets: ["host.docker.internal:8081"]

  - job_name: "currencyx-conversion-service"
    metrics_path: "/actuator/prometheus"
    static_configs:
      - targets: ["host.docker.internal:8082"]
```

Reload or restart Prometheus after changing its configuration.

## 7. Configure the Spring Cloud Config Server

The Config Server runs on port `8888`.

Check the configuration in:

```text
currencyx-config-server/src/main/resources/application.yml
```

For local Native configuration, an example is:

```yaml
server:
  port: 8888

spring:
  application:
    name: currencyx-config-server

  profiles:
    active: native

  cloud:
    config:
      server:
        native:
          search-locations: file:///C:/Users/prave/Repo/Training/currencyx-microservices/currencyx-config-server/config-repo
```

**Important:** Update the path to match the directory where you cloned the repository. If the project is configured to use Git instead, keep its Git configuration and do not replace it blindly.

Start Config Server first.

Verify:

```text
http://localhost:8888/actuator/health
```

Check whether the configuration is being served:

```text
http://localhost:8888/currencyx-api-gateway/default
```

The response should contain the expected Gateway configuration and property sources.

## 8. Start Services in the Correct Order

Start the applications in this order:

| Order | Application | Port |
|---:|---|---:|
| 1 | Config Server | 8888 |
| 2 | Eureka Server | 8761 |
| 3 | Currency Exchange Service | 8081 |
| 4 | Currency Conversion Service | 8082 |
| 5 | CurrencyX API Gateway | 8080 |

### Step 1: Start Config Server

Run the main application class:

```text
CurrencyxConfigServerApplication.java
```

Expected URL:

```text
http://localhost:8888
```

### Step 2: Start Eureka Server

Run:

```text
CurrencyxEurekaServerApplication.java
```

Open:

```text
http://localhost:8761
```

Verify that the Eureka dashboard loads.

### Step 3: Start Currency Exchange Service

Run:

```text
CurrencyExchangeServiceApplication.java
```

Expected port:

```text
8081
```

### Step 4: Start Currency Conversion Service

Run:

```text
CurrencyConversionServiceApplication.java
```

Expected port:

```text
8082
```

### Step 5: Start API Gateway

Run:

```text
CurrencyxApiGatewayApplication.java
```

Expected port:

```text
8080
```

## 9. Verify Eureka Service Registration

Open:

```text
http://localhost:8761
```

Expected registered services:

```text
CURRENCY-EXCHANGE-SERVICE
CURRENCY-CONVERSION-SERVICE
CURRENCYX-API-GATEWAY
```

The Config Server and Eureka Server themselves do not necessarily need to appear as registered applications.

## 10. Test Application Health

Open these URLs in your browser:

```text
http://localhost:8080/actuator/health

http://localhost:8081/actuator/health

http://localhost:8082/actuator/health
```

Expected response:

```json
{
  "status": "UP"
}
```

Check Prometheus metrics:

```text
http://localhost:8080/actuator/prometheus

http://localhost:8081/actuator/prometheus

http://localhost:8082/actuator/prometheus
```

## 11. Test CurrencyX APIs

Use Postman to test the existing exchange and conversion endpoints.

Example direct service URLs:

```text
http://localhost:8081
http://localhost:8082
```

Example Gateway URL:

```text
http://localhost:8080
```

Use the actual controller mappings and request parameters from the cloned version of the project. Do not assume endpoint paths or payload fields if they have changed.

Verify:

- Exchange rate API works.
- Conversion API works.
- Conversion Service can communicate with Exchange Service.
- Gateway routes requests correctly.
- Eureka service discovery resolves service instances.

## 12. Verify Observability

### Prometheus

```text
http://localhost:9090
```

Try:

```promql
up
```

All three CurrencyX targets should report `1`.

### Grafana

```text
http://localhost:3000
```

Open the existing dashboard:

```text
CurrencyX - Microservices Monitoring
```

Check service availability, HTTP request rate, JVM memory, CPU and response time.

### Zipkin

```text
http://localhost:9411
```

1. Trigger a request through the API Gateway.
2. Open Zipkin.
3. Search for recent traces.
4. Inspect the Gateway and downstream service spans.

### ELK

Open Kibana:

```text
http://localhost:5601
```

Navigate to **Analytics → Discover**.

Select the data view:

```text
currencyx-logs-*
```

Verify that logs from the Gateway, Exchange and Conversion services are visible.

Useful fields to inspect:

- `application`
- `level`
- `message`
- `traceId`
- `spanId`
- `@timestamp`

## 13. Common Issues and Solutions

| Problem | What to check |
|---|---|
| Port already in use | Check `netstat -ano \| findstr :PORT` |
| Config Server unavailable | Start Config Server before business services |
| Eureka shows no services | Check Eureka client configuration and service startup logs |
| Gateway returns 503 | Check route configuration and downstream service health |
| Feign client fails | Verify service name and Eureka registration |
| Redis connection error | Check that Redis dependencies/configuration have not been added to CurrencyX |
| Prometheus target DOWN | Verify metrics endpoint and Docker host access |
| Zipkin traces missing | Verify tracing dependencies, sampling and Zipkin endpoint |
| Kibana has no logs | Verify Logstash TCP input and Elasticsearch index creation |
| Maven build fails | Check JDK version, Maven dependencies and application logs |

Useful Docker commands:

```bash
docker ps -a
docker logs currencyx-logstash --tail 100
docker logs currencyx-elasticsearch --tail 100
docker logs currencyx-kibana --tail 100
```

Useful port check in Windows:

```powershell
netstat -ano | findstr :8080
```

Replace `8080` with the port you want to inspect.

## 14. Stop the Applications

First stop the Spring Boot applications using IntelliJ's Stop button.

Then stop the Docker containers if you no longer need the infrastructure:

```bash
docker stop currencyx-logstash
docker stop currencyx-kibana
docker stop currencyx-elasticsearch
docker stop flight-grafana
docker stop flight-prometheus
docker stop zipkin
```

Do not remove containers or volumes unless you intentionally want to delete the infrastructure or its stored data.

## 15. Quick Startup Checklist

Use this checklist whenever you start working on CurrencyX.

- [ ] Open Docker Desktop.
- [ ] Verify required infrastructure containers are running.
- [ ] Open IntelliJ IDEA.
- [ ] Open the CurrencyX repository.
- [ ] Check the configured JDK.
- [ ] Start Config Server.
- [ ] Start Eureka Server.
- [ ] Start Exchange Service.
- [ ] Start Conversion Service.
- [ ] Start API Gateway.
- [ ] Verify Eureka registration.
- [ ] Test APIs through Gateway.
- [ ] Check Prometheus and Grafana.
- [ ] Check Zipkin traces.
- [ ] Check Kibana logs.

## 16. Current and Future Project Scope

### Implemented

- [x] Currency Exchange Service
- [x] Currency Conversion Service
- [x] API Gateway
- [x] Eureka Service Discovery
- [x] Spring Cloud Config Server
- [x] OpenFeign and synchronous service communication
- [x] Resilience features at the Gateway
- [x] Prometheus metrics
- [x] Grafana dashboards
- [x] Zipkin distributed tracing
- [x] ELK centralized logging

### Planned

- [ ] Dedicated Authentication Service
- [ ] Spring Security and JWT
- [ ] Role-based authorization
- [ ] Apache Kafka integration
- [ ] Kafka retry and Dead Letter Topics
- [ ] Idempotent event consumers
- [ ] Transactional Outbox pattern
- [ ] Docker Compose for the complete CurrencyX application stack
- [ ] Integration testing with Testcontainers
- [ ] CI/CD using GitHub Actions

---

**Final recommendation:** Keep this document in the repository as `README-LOCAL-SETUP.md`. Update the startup order, ports and API examples whenever the project architecture changes.