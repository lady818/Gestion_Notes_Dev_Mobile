#  Gestionnaire de Notes

Application Android développée en Java permettant à l'utilisateur de créer, modifier et gérer ses notes personnelles. Les données sont sauvegardées localement afin d'être conservées même après la fermeture de l'application.

##  Fonctionnalités

- Création de notes
- Modification des notes
- Recherche de notes par titre
- Ajout et suppression des favoris
- Attribution d'une couleur aux notes
- Affichage de toutes les notes
- Filtre des notes favorites
- Persistance locale des données

## Technologies utilisées

- Java
- Android Studio
- RecyclerView
- SharedPreferences / Persistance locale

## Structure du projet

```
app/
├── java/
│   ├── activities/
│   ├── adapters/
│   ├── models/
│   ├── utils/
│   └── ...
├── res/
│   ├── layout/
│   ├── drawable/
│   ├── values/
│   └── ...
```

## Installation

1. Cloner le dépôt :

```bash
git clone https://github.com/lady818/Gestion_Notes_Dev_Mobile.git
```
2. Ouvrir le projet avec Android Studio.
3. Synchroniser Gradle.
4. Exécuter l'application sur un émulateur ou un appareil Android.

## Fonctionnement

### Création d'une note

- Cliquer sur le bouton d'ajout.
- Choisir une couleur.
- Saisir le titre et le contenu.
- Enregistrer la note.

### Modification d'une note

- Sélectionner une note.
- Modifier ses informations.
- Sauvegarder les changements.

### Gestion des favoris

- Double-cliquer sur une note pour l'ajouter ou la retirer des favoris.

### Recherche

- Saisir un mot-clé dans la barre de recherche.
- Les notes correspondantes sont affichées automatiquement.

## 👥 Auteurs

- **Mame Sadio Guisse**

## 📚 Cadre du projet

Projet réalisé dans le cadre du module Introduction au Développement Android à l'ESP (École Supérieure Polytechnique), année académique 2025-2026
