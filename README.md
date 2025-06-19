# 🧩 Ippen Microservice Demo

Diese Demo zeigt eine moderne Microservice-Architektur mit **Spring Boot**, **Keycloak**, **Docker**, **Kubernetes** und optionaler Anbindung an **AWS S3**. Sie wurde als technisches Showcase für das Interview bei **Ippen Digital** entwickelt – mit Fokus auf Authentifizierung, Service-Sicherheit und skalierbarer Architektur.

---

## ⚙️ Architekturüberblick

![Architekturübersicht](./docs/architecture.png)

> Visualisiert sind: Authentifizierung via Keycloak, getrennte Microservices mit DB-Anbindung und geschütztem Zugriff über JWT-Token.

---

## 📦 Services

| Service         | Port | Beschreibung                            |
|-----------------|------|------------------------------------------|
| `keycloak`      | 8080 | Authentifizierungsserver (OIDC, OAuth2) |
| `user-service`  | 8081 | Benutzerverwaltung                      |
| `order-service` | 8082 | Bestellungen mit OAuth2/JWT-Schutz      |
| `mysql`         | 3306 | Datenbank für User & Orders             |

---

## 🚀 Starten mit Docker Compose

```bash
docker compose up --build
````

> Danach erreichbar:

* 🔐 [Keycloak Admin](http://localhost:8080) (`admin` / `admin`)
* 👤 User-Service: [http://localhost:8081](http://localhost:8081)
* 📦 Order-Service: [http://localhost:8082](http://localhost:8082)

Test-User: `alice` / `alicepass` wird automatisch importiert.

---

## ☁️ Kubernetes Deployment (Minikube oder EKS)

```bash
kubectl apply -f deploy/k8s/
```

**Bereitgestellt werden:**

* Deployments & Services für `mysql`, `keycloak`, `user-service`, `order-service`
* Persistente Volumes via PVC
* Skalierbarkeit durch Replikation

---

## 🔐 Authentifizierung (Keycloak & JWT)

* Realm: `myrealm`
* Zugriff auf geschützte Endpunkte im `order-service` nur mit gültigem JWT-Token
* Konfiguration über `spring-security-oauth2-resource-server`

---

## 🌐 Beispiel-API-Aufrufe

```http
# Token holen
POST http://localhost:8080/realms/myrealm/protocol/openid-connect/token

# Bestellungen abrufen
GET http://localhost:8082/orders
Authorization: Bearer <JWT>

# Benutzer verwalten
GET http://localhost:8081/users
```

👉 Siehe `example-requests.http` für konkrete Beispiele (z. B. nutzbar mit IntelliJ HTTP Client oder VS Code).

---

## 🛠️ Technologien

* Java 17, Spring Boot 3
* Spring Security + OAuth2 Resource Server
* Keycloak 24
* Docker & Docker Compose
* Kubernetes (Minikube / EKS)
* AWS SDK v2 (für S3 vorbereitet)
* GitHub Actions für CI/CD

---

## 🔁 Build & CI/CD

```bash
# Lokales Docker-Build
docker build -t thanhtuanh/user-service:latest ./user-service
docker build -t thanhtuanh/order-service:latest ./order-service
docker push ...
```

✅ Automatisierter CI/CD-Workflow (`.github/workflows/ci.yml`) beinhaltet:

* Build & Unit-Test pro Service
* Push der Docker-Images zu Docker Hub
* Tests werden bei jedem Push auf `main` ausgeführt

---

## ✅ Unit-Tests

* Beispieltest: `OrderControllerTest.java`
* Simulierte JWT-Authentifizierung über `spring-security-test`
* Getrennte Tests pro Service

```bash
cd order-service
mvn clean verify
```

---

## 👤 Autor

[🔗 Duc Thanh Nguyen – GitHub Portfolio](https://github.com/thanhtuanh/bewerbung)

---

**Stand:** Juni 2025

```
