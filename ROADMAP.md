# ROADMAP

1 - Créer un outil de communication entre micro services simple

- ~~Meilleur gestion des erreurs~~
- ~~Ajout d'un object message avec getAsString(int index), getAsInt(int index), ...~~
- ~~Ajout d'un système de log~~

1 - Monter les deux micro-services: gateway et signature

- ~~Remplacer MySQL par Mongodb~~
- ~~Créer un serveur pour signature~~
- ~~Créer un paquet client signature~~
- Mise en place health check
- Réfléxion sur la dépendance d'injections: component scan pour les libs ? Ou création de bean dupliquée ?
- ~~Fixer la suppression de documents~~
- S'assurer que les tests utilisent bien un profil, une bdd et un contexte spécifiques
- Tests Gateway avec Mockito
- Tests Cypress ?
- Restaurer les messages d'erreurs en cas de problème de connexion (client http)
- Empêcher la connexion mongo de gateway due à l'import de dépendances
- Installer JUnit 5, instaurer un tag "only"

1 - Améliorer les outils de dev

- Script python de contrôle des docker ? Start: build + start, Restart: build + restart, ...
- Image docker intégrée au docker compose avec command gradle bootRun ?
- Exposer un port debug par application ? Voir LiveReload server is running on port 35729 https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-devtools.html

1 - Trouver un moyen convenable de configuration par environement (Chart Helm ?)
1 - Monter un pipeline CI / CD

- Dépôt d'artefacts (JFrog ? Sonatype Nexus ?)
- Gitlab CI
- Docker registry