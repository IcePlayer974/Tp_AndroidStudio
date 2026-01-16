Voici une proposition de contenu pour votre fichier `README.md`, structurée sous forme de rapport comme demandé.

Vous pouvez copier-coller ce contenu directement dans votre fichier `README.md`.

---

# Rapport de Projet - Application E-commerce Android

Ce projet est une application mobile de e-commerce développée dans le cadre du module "Dev Mobile Android" (2025-2026). Elle permet aux utilisateurs de consulter des produits, de gérer un panier et de simuler des commandes, en se basant sur les données de l'API publique [FakeStoreAPI](https://fakestoreapi.com/).

## 1. Le Fonctionnel de l'application

L'application offre un parcours utilisateur complet pour un site de vente en ligne, structuré autour de quatre écrans principaux et d'un système de navigation fluide.

### Consultation des produits

* **Liste des produits :** Dès l'ouverture, l'application affiche la liste complète des produits disponibles récupérés depuis le serveur.
* **Filtrage par catégorie :** Un menu latéral (Drawer) permet à l'utilisateur de filtrer les produits par catégorie (ex: Électronique, Bijoux, Vêtements homme/femme).
* **Tri :** Une fonctionnalité de tri est disponible pour organiser les produits par prix (croissant/décroissant) ou par note (rating).
* **Détails du produit :** En cliquant sur un article, l'utilisateur accède à une fiche détaillée présentant l'image en grand format, la description complète, le prix et la note moyenne des avis.

### Gestion du Panier

* **Ajout au panier :** Depuis la fiche produit, l'utilisateur peut ajouter l'article à son panier.
* **Modification des quantités :** Dans l'écran "Panier", il est possible d'augmenter ou de diminuer la quantité de chaque article, ou de les supprimer.
* **Calcul automatique :** Le montant total du panier est recalculé en temps réel à chaque modification.
* **Persistance :** Le contenu du panier est sauvegardé localement. Si l'utilisateur quitte l'application, il retrouve son panier intact au prochain lancement.

### Historique et Commandes

* **Simulation d'achat :** Un bouton "Valider la commande" permet de finaliser l'achat (simulation). Cela vide le panier et enregistre la transaction.
* **Historique :** Un écran dédié permet de consulter l'historique des commandes passées, affichant la date, le montant total et le nombre d'articles pour chaque commande.

## 2. L'Implémentation Technique

L'application respecte les standards modernes du développement Android natif recommandés par Google.

### Architecture et Langage

* **Langage :** Kotlin (100%).
* **Architecture :** MVVM (Model-View-ViewModel). Cette architecture permet de séparer clairement la logique métier (ViewModels) de l'interface utilisateur (Compose), facilitant la maintenance et les tests.
* **UI Toolkit :** **Jetpack Compose**. L'interface est entièrement déclarative, construite sans fichiers XML classiques, et utilise les composants **Material3** pour un design moderne et responsive.

### Bibliothèques et Outils utilisés

* **Réseau (API REST) :**
* **Retrofit :** Utilisé pour effectuer les requêtes HTTP vers `fakestoreapi.com`.
* **Gson :** Pour la sérialisation/désérialisation des données JSON en objets Kotlin (`Product`, `Category`).
* **Coroutines :** Pour gérer les appels réseau de manière asynchrone sans bloquer le fil principal (Main Thread).


* **Persistance des données (Base de données locale) :**
* **Room Database :** Utilisé pour stocker localement le panier et l'historique des commandes. Cela permet à l'application de fonctionner partiellement hors ligne et de conserver les données entre les sessions.
* Deux entités principales sont gérées : `CartEntity` (articles du panier) et `OrderEntity` (historique des commandes).


* **Gestion de l'état et Flux de données :**
* **StateFlow :** Utilisé dans les ViewModels (`ProductViewModel`, `CartViewModel`) pour exposer les données à l'interface utilisateur de manière réactive. L'UI se met à jour automatiquement dès que les données changent (ex: mise à jour du total du panier).


* **Chargement d'images :**
* **Coil :** Bibliothèque légère et performante pour charger et afficher les images des produits depuis leurs URLs de manière asynchrone.


* **Navigation :**
* **Navigation Compose :** Gestion de la navigation entre les différents écrans (`productList`, `productDetail`, `cart`, `history`) au sein d'une seule Activité (`MainActivity`).



### Structure du Code

Le code est organisé par fonctionnalités dans le package `fr.delplanque.tp_androidstudio` :

* `ui/` : Contient les thèmes et les configurations de couleur.
* `MainActivity.kt` : Point d'entrée contenant la configuration du `NavHost`.
* `*Screen.kt` : Les fichiers composables pour chaque écran (Vue).
* `*ViewModel.kt` : La logique métier faisant le lien entre les données et la vue.
* `AppDatabase.kt` / `*Entity.kt` : La couche de données locale (Room).
* `ApiService.kt` : La couche de données distante (Retrofit).
