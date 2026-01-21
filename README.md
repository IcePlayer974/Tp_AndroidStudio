# ArnacShop - Application E-Commerce Android

Ce projet est une application Android de e-commerce moderne développée avec **Jetpack Compose**. Elle permet de parcourir des produits, de gérer un panier local et de consulter un historique de commandes.

## 🚀 Fonctionnalités

- **Navigation fluide** : Utilisation d'un `ModalNavigationDrawer` pour naviguer entre la boutique, le panier et l'historique.
- **Catalogue de produits** : Récupération dynamique des données depuis l'API [FakeStoreAPI](https://fakestoreapi.com/).
- **Filtrage et Tri** : Possibilité de filtrer par catégorie et de trier par prix ou par note.
- **Détails produits** : Vue détaillée avec description, image haute résolution et avis.
- **Gestion du Panier** : Ajout/suppression de produits et modification des quantités avec persistance locale.
- **Historique des commandes** : Consultation des achats passés (date et montant total).

## 🛠️ Stack Technique

- **Langage** : Kotlin
- **Interface Utilisateur** : Jetpack Compose (Material 3)
- **Architecture** : MVVM (Model-View-ViewModel)
- **Gestion Réseau** : Retrofit & Coil (chargement d'images)
- **Base de données locale** : Room Database
- **Injection de dépendances** : Architecture standard Android avec ViewModels
- **Navigation** : Compose Navigation

## 🏗️ Architecture et Choix Techniques

### Architecture MVVM
L'application est découpée en couches pour assurer la maintenabilité :
- **Model** : Représentation des données (`Product`, `CartEntity`, `OrderEntity`).
- **ViewModel** : Gestion de la logique métier (`ProductViewModel`, `CartViewModel`) et exposition des données via `StateFlow`.
- **View** : Composables déclaratifs observant l'état fourni par les ViewModels.

### Persistance des données
Le choix s'est porté sur **Room** pour le panier et l'historique afin de garantir que les données de l'utilisateur ne soient pas perdues lors de la fermeture de l'application ou en cas de perte de réseau.

### Gestion du réseau
L'utilisation de **Retrofit** couplée à un `ApiService` permet une gestion propre des appels asynchrones. Les images sont gérées de manière performante grâce à **Coil**, qui gère le cache et le chargement en arrière-plan.

## 🚧 Points de Blocage et Solutions



## 📁 Structure du Projet

- `fr.delplanque.tp_androidstudio`
    - `ApiService.kt` : Configuration de Retrofit.
    - `CartDatabase.kt` : Configuration de Room.
    - `CartViewModel.kt` & `ProductViewModel.kt` : Logique applicative.
    - `MainActivity.kt` : Point d'entrée et gestion de la navigation.
    - `screens/` : Contient les différents écrans de l'application (List, Detail, Cart, History).
    - `ui.theme/` : Définition des couleurs, polices et thèmes Material 3.
 
## Aperçu du projet
<img src="img/acceuil.jpg" width="30%"/>
<img src="img/categorie.jpg" width="30%"/>
<img src="img/tri.jpg" width="30%"/>
<img src="img/detail_produit_achat.jpg" width="30%"/>
<img src="img/panier.jpg" width="30%"/>
<img src="img/confirmation.jpg" width="30%"/>
<img src="img/historique.jpg" width="30%"/>

---
*Projet réalisé par DELPLANQUE Julien et VERGNIOLE Yohann dans le cadre du module de Développement Mobile (Android) (2025-2026).*
