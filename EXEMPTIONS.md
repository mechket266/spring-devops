# Procédure d'exemption (faux positifs bloquants)

## Principe
Un contrôle de sécurité bloquant ne peut être contourné que par une
exemption **justifiée, validée par le binôme, limitée dans le temps et
tracée dans Git**. Aucune désactivation globale d'un outil n'est autorisée.

## Procédure
1. Analyser le finding (rapport en artifact du pipeline / onglet Security).
2. Confirmer qu'il s'agit d'un faux positif ou d'un risque accepté
   (ex. : CVE sans correctif, composant non utilisé au runtime).
3. Créer une branche `exemption/<outil>-<id>`.
4. Ajouter l'exemption dans le fichier de l'outil concerné, avec un
   commentaire : justification, auteur, date d'expiration.
5. Ajouter une ligne dans le registre ci-dessous.
6. Ouvrir une Pull Request : relecture et approbation obligatoires
   par l'autre membre du binôme.
7. À la date d'expiration, l'exemption est réévaluée (supprimée ou renouvelée).

## Où déclarer une exemption

| Outil | Fichier / mécanisme |
|---|---|
| Trivy (SCA, image) | `.trivyignore` |
| Gitleaks | `.gitleaksignore` (fingerprint du rapport) |
| Semgrep | commentaire `// nosemgrep: <rule-id>` sur la ligne concernée |
| SonarCloud | marquer le finding *Safe* / *Accepted* dans l'interface, avec commentaire |
| OWASP ZAP | non bloquant : pas d'exemption nécessaire |

## Registre des exemptions

| Date | Outil | Identifiant | Justification | Validé par | Expiration |
|---|---|---|---|---|---|
| — | — | — | Aucune exemption active | — | — |
