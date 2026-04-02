# ChâTop - Portail de location saisonniere (Front Angular + API Spring Boot)

Ce depot contient deux projets :
- `front/` : application Angular (fournie, non modifiee dans le cadre du projet).
- `api/` : API REST Spring Boot developpee pour alimenter le front.

Projet realise dans le cadre de la formation OpenClassrooms - Developpeur Full-Stack Java/Angular.

## Objectifs du projet

- Implementer une API REST en architecture en couches (Controller / Service / Repository / DTO).
- Securiser les routes avec JWT (sauf authentification et Swagger).
- Exposer une documentation API avec Swagger (OpenAPI).
- Connecter l'API au front Angular sans modifier le front existant.

## Structure du repository

```text
.
|-- api/        # Back-end Spring Boot + Spring Security + JPA + Swagger
|-- front/      # Front-end Angular (starter OpenClassrooms)
`-- docs/       # Scripts SQL et documents utiles
```

## Prerequis

### Outils

- Java 17
- Angular 20 (CLI ou via npm)
- Node.js 22
- npm (inclus avec Node.js)
- MySQL 8+

### Verifications rapides (Windows / PowerShell)

```powershell
java -version
node -v
npm -v
mysql --version
```

## 1) Installation de la base de donnees

Le script SQL principal se trouve dans `docs/chatop.sql`.

### Creer et initialiser la base

```powershell
mysql -u <utilisateur_mysql> -p < "docs/chatop.sql"
```

> Le script cree la base `chatop` et les tables `users`, `rentals`, `messages`.

### Creer un utilisateur SQL dedie (recommande)

Connectez-vous a MySQL avec un compte ayant les droits d'administration, puis executez :

```sql
CREATE USER IF NOT EXISTS 'chatop_user'@'localhost' IDENTIFIED BY 'change_me';
GRANT ALL PRIVILEGES ON chatop.* TO 'chatop_user'@'localhost';
FLUSH PRIVILEGES;
```

> Ces droits s'appliquent uniquement a `chatop.*`.

## 2) Configuration de l'API (variables d'environnement)

L'API charge automatiquement `api/src/main/resources/env-dev.yml` (profil `dev`).
Ce fichier est **local**, **ignore par Git** (`api/.gitignore` -> `env-*.yml`) et doit etre cree par l'utilisateur.

### Etape 2.1 - Creer le fichier local

Chemin attendu : `api/src/main/resources/env-dev.yml`

### Etape 2.2 - Renseigner les variables attendues

Utilisez ce modele :

```yaml
DB_URL: localhost:3306/chatop
DB_USER: chatop_user
DB_PASSWORD: change_me
JWT_SECRET: change_me_with_a_long_random_secret
PICTURE_BASE_URL: http://localhost:8080
MAIN_DOMAIN: http://localhost:4200
```

Variables attendues :
- `DB_URL` : hote, port et nom de base MySQL
- `DB_USER` : utilisateur MySQL
- `DB_PASSWORD` : mot de passe MySQL
- `JWT_SECRET` : secret de signature JWT
- `PICTURE_BASE_URL` : URL publique de l'API pour les images
- `MAIN_DOMAIN` : domaine autorise en CORS (front Angular)

### Etape 2.3 - Regle de securite

Ne versionnez jamais de credentials ou secrets reels (mot de passe BDD, cle JWT, etc.).

## 3) Lancer l'API Spring Boot

Depuis le dossier `api/` :

```powershell
cd api
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```
ou
```bash
cd api
./mvnw.cmd clean install
./mvnw.cmd spring-boot:run
```

API disponible sur :
- `http://localhost:8080`

Swagger UI :
- `http://localhost:8080/swagger-ui/index.html`

Spec OpenAPI JSON :
- `http://localhost:8080/v3/api-docs`

## 4) Lancer le front Angular (sans modifications)

Depuis le dossier `front/` :

```powershell
cd front
npm install
npm run start
```

Front disponible sur :
- `http://localhost:4200`

Le proxy Angular (`front/src/proxy.config.json`) redirige `/api/**` vers `http://localhost:8080`.

## Authentification et securite

- Routes publiques :
  - `POST /api/auth/register`
  - `POST /api/auth/login`
  - routes Swagger (`/swagger-ui/**`, `/v3/api-docs/**`)
- Toutes les autres routes API sont protegees par token Bearer JWT.

## Commandes utiles

### API

```powershell
cd api
.\mvnw.cmd test
```
ou
```bash
cd api
./mvnw.cmd test
```

### Front

```powershell
cd front
npm run test
npm run lint
npm run build
```
