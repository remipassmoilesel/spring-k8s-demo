# ROADMAP

1 - Créer un outil de communication entre micro services simple

- ~~Meilleur gestion des erreurs~~
- ~~Ajout d'un object message avec getAsString(int index), getAsInt(int index), ...~~
- ~~Ajout d'un système de log~~

1 - Monter les deux micro-services: gateway et signature

- Remplacer MySQL par Mongodb
- Créer un serveur pour signature
- Créer un paquet de clients
- Mise en place health check
- Réfléxion sur la dépendance d'injections: component scan pour les libs ? Ou création de bean dupliquée ?

1 - Améliorer les outils de dev

- Script python de contrôle des docker ?
- Image docker intégrée au docker compose avec command gradle bootRun ?

1 - Trouver un moyen convenable de configuration par environement (Chart Helm ?)
1 - Monter un pipeline CI / CD
