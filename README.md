# Microservice Demo – Plattform für user.ID (Spring Boot, OAuth2/OIDC, Kubernetes, AWS)

Diese Demo zeigt eine Referenzarchitektur für Microservices (user-service, order-service) mit Authentifizierung via Keycloak sowie Deployments für lokale Entwicklung (Docker Compose) und Kubernetes (z.B. Minikube, AWS EKS). Das Setup ist vorbereitet für eine **user.ID Plattform** und kann leicht erweitert werden.

---

## Übersicht

- **user-service**: Zentrale User-Verwaltung (Spring Boot, MySQL)
- **order-service**: Bestellverwaltung (Spring Boot, OAuth2 Resource Server, S3-Demo)
- **Keycloak**: Single-Sign-On & OIDC für sichere Authentifizierung
- **Kubernetes Deployments**: Für Produktion/Cloud (z.B. AWS EKS)
- **Docker Compose**: Für lokale Entwicklung & schnelles Testen
- **AWS S3/RDS**: Demo-Integration (optional)
- **CI/CD**: GitHub Actions Workflow für Build & Docker Push

---

## 1. Lokale Entwicklung mit Docker Compose

**Voraussetzung:** Docker & Docker Compose installiert.

```bash
docker compose up --build
```

> **Achtung:** Die Services starten u.a. auf:
> - MySQL:      [localhost:3306](http://localhost:3306)
> - Keycloak:   [localhost:8080](http://localhost:8080)
> - User-Svc:   [localhost:8081](http://localhost:8081)
> - Order-Svc:  [localhost:8082](http://localhost:8082)

**Datei:** `./docker-compose.yaml`
```yaml
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: rootpw
      MYSQL_DATABASE: demo
      MYSQL_USER: demo
      MYSQL_PASSWORD: demopw
    ports: ["3306:3306"]
    volumes: [mysql-data:/var/lib/mysql]
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 5s
      retries: 10

  keycloak:
    image: quay.io/keycloak/keycloak:24.0.4
    command: start-dev --import-realm
    volumes:
      - ./keycloak/realm-export.json:/opt/keycloak/data/import/realm-export.json
    environment:
      KEYCLOAK_ADMIN: admin
      KEYCLOAK_ADMIN_PASSWORD: admin
    ports: ["8080:8080"]
    depends_on:
      mysql:
        condition: service_healthy

  user-service:
    build: { context: user-service }
    image: thanhtuanh/user-service:latest
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/demo
      SPRING_DATASOURCE_USERNAME: demo
      SPRING_DATASOURCE_PASSWORD: demopw
    ports: ["8081:8080"]
    depends_on:
      mysql:
        condition: service_healthy

  order-service:
    build: { context: order-service }
    image: thanhtuanh/order-service:latest
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/demo
      SPRING_DATASOURCE_USERNAME: demo
      SPRING_DATASOURCE_PASSWORD: demopw
      SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI: http://keycloak:8080/realms/myrealm
    ports: ["8082:8080"]
    depends_on:
      mysql:
        condition: service_healthy
      keycloak:
        condition: service_started

volumes:
  mysql-data:
```

---

## 2. Kubernetes Deployment (z.B. Minikube/EKS)

### Cluster starten (z.B. mit Minikube)
```bash
minikube delete
minikube start
```

### Deployments anwenden
```bash
kubectl apply -f deploy/k8s/mysql-deployment.yaml
kubectl apply -f deploy/k8s/keycloak-deployment.yaml
kubectl apply -f deploy/k8s/user-service-deployment.yaml
kubectl apply -f deploy/k8s/order-service-deployment.yaml
```

### Beispiel-Konfiguration: `deploy/k8s/user-service-deployment.yaml`
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: user-service
spec:
  replicas: 2 # Ausfallsicherheit
  selector:
    matchLabels:
      app: user-service
  template:
    metadata:
      labels:
        app: user-service
    spec:
      containers:
        - name: user-service
          image: <dein-dockerhub>/user-service:latest
          env:
            - name: SPRING_DATASOURCE_URL
              value: jdbc:mysql://<rds-endpoint>:3306/users
            - name: SPRING_DATASOURCE_USERNAME
              value: <db-user>
            - name: SPRING_DATASOURCE_PASSWORD
              value: <db-password>
          ports:
            - containerPort: 8080
---
apiVersion: v1
kind: Service
metadata:
  name: user-service
spec:
  type: ClusterIP
  selector:
    app: user-service
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
```
> Für Produktionsbetrieb: Datenbankzugang und Passwörter über K8s-Secrets verwalten!

---

## 3. Build & Push Docker Images

```bash
# Im Projektverzeichnis
docker build -t <user>/user-service:latest ./user-service
docker build -t <user>/order-service:latest ./order-service
docker push <user>/user-service:latest
docker push <user>/order-service:latest
```

Siehe auch `.github/workflows/ci.yml` für automatisiertes CI/CD.

---

## 4. Keycloak einrichten

- Keycloak läuft auf Port 8080 (`http://localhost:8080`).
- Admin-Login: **admin / admin**
- Realm & User werden beim ersten Start importiert (`./keycloak/realm-export.json`).
- Test-User: `alice` / `alicepass`
- Beispiel-Client: `order-service` (OIDC-Flow)

---

## 5. Beispiel-Requests

- Siehe Datei `example-requests.http` für Beispiel-APIs (z.B. nutzbar im VS Code oder IntelliJ HTTP-Client).
- Authentifizierung via OIDC/JWT.

---

## 6. Hinweise & Best Practices

- AWS S3 Integration benötigt gültige AWS Credentials (`aws/README-aws-demo.md`).
- Für andere DBs (Postgres, MongoDB) einfach Pom + Deployments anpassen.
- **Sicherheit:** Passwörter, Tokens und Secrets NIEMALS im Klartext! Nutze K8s-Secrets, Vault oder AWS Secrets Manager.
- Skalierung: Anzahl der Replikas in Deployments variieren.
- Feedback, Fragen oder PRs willkommen!

---

## 7. Nützliche Links

- [Spring Boot Doku](https://spring.io/projects/spring-boot)
- [Keycloak Docs](https://www.keycloak.org/documentation)
- [Kubernetes Getting Started](https://kubernetes.io/docs/tutorials/)
- [GitHub Actions](https://docs.github.com/actions)

---

**Stand:** 2025-06  
(c) user.ID Plattform Demo | [thanhtuanh](https://github.com/thanhtuanh)
