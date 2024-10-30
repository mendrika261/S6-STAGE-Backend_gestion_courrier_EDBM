# Information générale

## Pré-requis

Avant de procéder au déploiement, assurez-vous que les versions suivantes sont installées sur le serveur :

- **PostgreSQL** : version 16
- **Docker** : version 20.10.24 ou supérieure
- **Docker Compose** : version 2 ou supérieure

## Structure des Répertoires

La structure du projet est organisée comme suit pour faciliter la gestion du code et la clarté des fonctionnalités :

- **src/** : Contient le code source du projet.
    - **analysis/** : Classes et méthodes dédiées à l'analyse de données.
    - **config/** : Validations des paramètres spécifiques dans `application.properties`.
    - **controller/** : Contrôleurs qui gèrent les requêtes et réponses.
    - **dto/** : Objets utilisés pour transférer des données entre les couches de l'application.
    - **entity/** : Entités représentant les tables de la base de données.
    - **filter/** : Filtres pour traiter les requêtes entrantes et sortantes, comme la vérification du token.
    - **repository/** : Interfaces et classes gérant l'accès aux données.
    - **security/** : Configurations et classes liées à la sécurité, comme l'authentification et l'autorisation.
    - **service/** : Classes contenant la logique métier et opérations de traitement des données.
    - **utils/** : Classes utilitaires et fonctions réutilisables, comme le formatage des données.
- **resources/**
    - **templates/** : Contenus `.html` pour les emails envoyés.
    - **application.properties** : Configurations modifiables du projet, comme la connexion à la base de données.
- **.env** : Contient les valeurs de configurations utilisées dans `application.properties`.

## Les services

L’application et ses services utilisent les ports suivants :

- **Application Spring Boot - OpenMapService v2** : `8082`
- **Application Spring Boot - Courrier** : `8083` 

> Veuillez éditer `docker-compose.yml` pour changer ces ports

# Installation et démarrage

## Configuration des Variables d’Environnement

Toutes les configurations de l’application doivent être définies dans le fichier `.env`. Assurez-vous de vérifier les éléments suivants :

- **Connexion à la Base de Données** : Vérifiez les informations de connexion (hôte, port, utilisateur, mot de passe) pour garantir l’accès à la base PostgreSQL.
- **Serveur SMTP** : Assurez-vous que les configurations SMTP sont correctes pour l'envoi d'e-mails.
- **Autres Configurations** : Types de fichiers autorisés, Durée des jetons d'authentification...

## Construction des Conteneurs

> **Note** : Les droits administrateurs peuvent être nécessaires pour exécuter cette commande.

Pour construire les images Docker des services, exécutez la commande suivante :

```bash
docker-compose build
```

Cette étape prépare les conteneurs avec les dépendances nécessaires pour le bon fonctionnement de l'application.

## Gestion de la Base de Données

Avant de lancer l’application, il est nécessaire de restaurer les données minimales d’utilisation dans la base de données PostgreSQL. Utilisez la commande `pg_restore` comme suit :

```bash
pg_restore -U utilisateur -h host -p 5432 -d nom_base -v dump.sql
```

Cette étape initialise la base de données avec les informations de base nécessaires pour le démarrage, changer en rapport avec les informations de connexion.

## Lancement de l’Application

> **Note** : Après le lancement du conteneur, si le serveur redémarre, il est automatiquement relancé.


Pour démarrer l’application en arrière-plan, exécutez la commande suivante :

```bash
docker-compose up -d
```

### Affichage des Logs

Pour vérifier les journaux en temps réel, utilisez la commande suivante :

```bash
docker-compose logs -f
```

Cela permet de surveiller l’état des services et de s’assurer que l’application fonctionne correctement après le démarrage.

