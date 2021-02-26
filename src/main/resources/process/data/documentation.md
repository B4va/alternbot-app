**COMMANDES ALTERN'BOT**

**Emploi du temps :**

- `$edt` : Obtenir l'emploi du temps du jour.
- `$edt -d <JJ-MM-AAAA>` : Obtenir l'emploi du temps à la date indiquée *(ex : `$edt -d 01-01-2021` pour récupérer l'emploi du temps du 01-01-2021)*.
- `$edt -p <n>` : Obtenir l'emploi du temps pour le n-ème prochain jour *(ex : `$edt -p 1` pour récupérer l'emploi du temps de demain)*.
- `$edt -e` : Obtenir l'emploi du temps sous la forme d'un fichier exportable.

**Travaux/Tâches :**

- `$task` : Obtenir la liste des tâches en cours.
- `$tâche -c [ <description> ] <date prévue JJ-MM-AAAA> <horaire prévu HH:MM>` : Créer une tâche. Cette opération est réservée aux utilisateurs ayant le rôle `TasksAdmin`. *Ex : `$task -c [ Nouvelle tâche ] 01-01-2000 10:00`*.
- `$tâche -u <id> [ <description> ] <date prévue JJ-MM-AAAA> <horaire prévu HH:MM>` : Modifier une tâche. Cette opération est réservée aux utilisateurs ayant le rôle `TasksAdmin`. *Ex : `$task -u 1 [ Tâche renomée ] 01-01-2000 10:00`*.
- `$tâche -d <id>` : Supprimer une tâche. Cette opération est réservée aux utilisateurs ayant le rôle `TasksAdmin`. *Ex : `$task -d 1`*.