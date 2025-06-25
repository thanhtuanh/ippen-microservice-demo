# 🧩 Ippen Microservice Demo

Dieses Projekt demonstriert eine skalierbare Microservice-Architektur mit **Spring Boot**, **Keycloak**, **Docker**, **Kubernetes** und optionaler AWS S3-Anbindung.  
Es entstand als technisches Showcase für die Position „Senior Java Backend Engineer“ bei **Ippen Digital** – mit Fokus auf Authentifizierung, Service-Security und Cloud-Readiness.

---

## 🎯 Bezug zu Ippen Digital & USER.ID

- **Microservice-Prinzipien**: Klare Trennung, RESTful APIs, Cloud-native Design
- **Moderne Authentifizierung**: OIDC & JWT (Keycloak), OAuth2 Resource Server
- **Kubernetes & AWS**: K8s-Deployments, vorbereitet für RDS & S3
- **Service-Security**: Geschützte Endpunkte, Unit- & Integrationstests, DevOps
- **Automatisierte API-Doku**: OpenAPI/Swagger für alle Services

---

## ⚙️ Architekturüberblick

![Architekturübersicht](./docs/architecture.png)

*Keycloak übernimmt die zentrale Authentifizierung, Microservices sind per JWT abgesichert, Datenhaltung via MySQL.*

---

## 📦 Services & Ports

| Service         | Port | Zweck                                  |
|-----------------|------|----------------------------------------|
| `keycloak`      | 8080 | Authentifizierung (OIDC, OAuth2)       |
| `user-service`  | 8081 | Benutzerverwaltung                     |
| `order-service` | 8082 | Bestellungen (OAuth2/JWT-gesichert)    |
| `mysql`         | 3306 | Zentrale Datenbank                     |

---

## 🚀 Schnellstart mit Docker Compose

```bash
docker compose up --build
```

*Danach erreichbar:*

- [Keycloak Admin](http://localhost:8080) (`admin`/`admin`)
- [User-Service](http://localhost:8081)
- [Order-Service](http://localhost:8082)

Testnutzer: `alice` / `alicepass` (wird automatisch importiert)

---

## 📖 API-Dokumentation

- [User-Service Swagger UI](http://localhost:8081/swagger-ui.html)
- [Order-Service Swagger UI](http://localhost:8082/swagger-ui.html)

---

## ☁️ Kubernetes Deployment

```bash
kubectl apply -f deploy/k8s/
```
- Deployments & Services für alle Komponenten
- Persistente Volumes via PVC
- Skalierbarkeit und Konfigurierbarkeit
- Secrets & Konfigs per K8s-Mechanismen

---

## 🔐 Authentifizierung (Keycloak & JWT)

- Realm: `myrealm`
- Nur mit gültigem JWT Zugriff auf geschützte Endpunkte (z.B. `/orders`)
- Konfiguration via `spring-security-oauth2-resource-server`

---

## 🌐 Beispiel-API-Aufrufe

```http
# Token holen
curl -X POST "http://localhost:8080/realms/myrealm/protocol/openid-connect/token" \
  -d "grant_type=password" -d "client_id=order-service" \
  -d "username=alice" -d "password=alicepass"

# Bestellungen abrufen (JWT erforderlich)
curl -H "Authorization: Bearer <JWT>" http://localhost:8082/orders

# Benutzer anzeigen
curl http://localhost:8081/users
```
👉 Siehe `example-requests.http` für weitere Beispiele (nutztbar mit IntelliJ HTTP Client oder VS Code).

---

## 🛠️ Technologien

- Java 17, Spring Boot 3
- Spring Security & OAuth2 Resource Server
- Keycloak 24
- Docker & Docker Compose
- Kubernetes (Minikube/EKS-ready)
- AWS SDK v2 (S3-Integration vorbereitet)
- GitHub Actions (CI/CD)
- JUnit, Mockito, Spring Security Test
- OpenAPI/Swagger

---

## 🔁 Build & CI/CD

```bash
# Build User-Service
docker build -t thanhtuanh/user-service:latest ./user-service

# Build Order-Service
docker build -t thanhtuanh/order-service:latest ./order-service
```

**CI/CD-Workflow (.github/workflows/ci.yml):**
- Build & Unit-Tests (`mvn verify`)
- Test-Reports (Surefire) als Artefakte
- Push Images zu Docker Hub
- Linting & Manifest-Validierung
- Automatischer Test bei jedem Push/PR auf `main`

---

## ✅ Unit- & Integrationstests

- `OrderControllerTest`: GET `/orders` mit JWT
- `UserControllerTest`: GET `/users`, ohne Auth
- JUnit, MockMvc, @WebMvcTest
- Test-Reports per GitHub Actions

```bash
cd order-service && mvn clean verify
cd user-service && mvn clean verify
```

---

## 📸 Beispiel: API-Test mit curl

Ein erfolgreicher API-Call auf den User-Service sieht so aus:

[➡️ PDF-Screenshot ansehen](./docs/curl-users.pdf)

![curl-users](./docs/curl-users.png)

---

## 📊 Monitoring & Logging

Dieses Projekt nutzt [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html) für Health- und Metrik-Endpunkte:

- Health-Check: [`/actuator/health`](http://localhost:8081/actuator/health)
- Metriken: [`/actuator/metrics`](http://localhost:8081/actuator/metrics)

**Für produktive Umgebungen empfohlen:**
- **ELK Stack:** Zentrales Log-Management & Visualisierung mit [Kibana](https://www.elastic.co/kibana/)
- **Prometheus & Grafana:** Metriken-Scraping, Dashboards & Alerting
- **(Optional)** [Grafana Loki](https://grafana.com/oss/loki/): Cloud-native Logging

Weitere Infos: [Spring Boot Actuator Doku](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)

---

## 📝 Hinweise & Best Practices

- Zugangsdaten & Secrets als K8s-Secrets verwalten (siehe YAML-Kommentare)
- Health-Checks via `/actuator/health`
- Beispiel-DB & Testuser dienen Demo-Zwecken
- Security: OIDC/JWT, Service-Isolation, Integrationstests mit Security-Kontext

---

## 🚀 Weiterführende Themen & Empfehlungen

Um dieses Demo-Projekt produktionsreif und zukunftssicher zu gestalten, sind folgende Aspekte wichtig:

### Monitoring & Observability
- **Spring Boot Actuator** ist bereits integriert (Health/Metrics).
- Für zentrale Überwachung und Auswertung empfiehlt sich:
  - **ELK Stack** (Elasticsearch, Logstash, Kibana) für zentrale Logsuche & Visualisierung
  - **Prometheus & Grafana** für Metrik-Scraping, Dashboards und Alerting
  - **Grafana Loki** als Cloud-native Alternative für Log-Streaming

### Security-Härtung
- Produktivbetrieb sollte ausschließlich über HTTPS erfolgen.
- Secrets (Datenbank, Keycloak, etc.) konsequent als Kubernetes-Secrets verwalten.
- Regelmäßige Security-Scans, Penetration-Tests und Audit-Logs sind empfohlen.

### Automatisierung & DevOps
- Die bestehende CI/CD-Pipeline kann um automatisches Deployment, Rollbacks und Tagging erweitert werden.
- Infrastructure as Code (bspw. mit Helm, Terraform) ermöglicht reproduzierbare Setups für verschiedene Umgebungen.

### Skalierbarkeit & Hochverfügbarkeit
- Kubernetes-Deployments ermöglichen horizontale Skalierung (mehrere Replikas pro Service).
- Liveness/Readiness-Probes sind für zuverlässige Updates & Verfügbarkeit konfigurierbar.
- Rolling Updates & Self-Healing durch Kubernetes sind vorbereitet.

### Cloud Readiness
- Integration von AWS-Diensten (z.B. S3, RDS, Secrets Manager) ist vorbereitet und kann produktiv genutzt werden.
- Multi-Environment-Setups (Staging, Prod) sind leicht realisierbar.

### Dokumentation & Onboarding
- Quellcode, API-Doku, Beispiel-Requests und Monitoring-Screenshots sind enthalten.
- Für Onboarding und Wissensaustausch empfiehlt sich ein dediziertes Developer-Handbuch.

### Teststrategie & Qualitätssicherung
- Unit-, Integrations- und End-to-End-Tests sind vorhanden und ausbaufähig.
- Automatisiertes Testdatenmanagement und QA-Prozesse werden empfohlen.

### Nachhaltige Weiterentwicklung
- Clean Code, Architekturprinzipien und Code-Reviews sichern langfristige Wartbarkeit.
- Modularisierung ermöglicht einfache Erweiterung um neue Services oder Features.

---

**Hinweis:**  
Diese Empfehlungen bilden die Basis für einen langfristig erfolgreichen Betrieb und die kontinuierliche Weiterentwicklung – insbesondere im Cloud- und Kubernetes-Umfeld.

---

## 👤 Autor

[🔗 Duc Thanh Nguyen – GitHub Portfolio](https://github.com/thanhtuanh/bewerbung)

---

**Stand:** Juni 2025
