# MSPR Java

### Présentation

Go Securi est une entreprise qui intervient dans le cadre de la sécuurité, du gardiennage, de la protection et de la surveillance. Leur mission consiste à assurer la sécurité des activités, déplacements et manifestations de leur client.
La société compte 64 salariés dont 53 agents de sécurité.

Les agents partagent un local sécurisé où est stocké du matériel parfois sensible. Aujourd'hui, il n'existe pas de contrôle d'accès au local automatisé, les agents remplissent une fiche d'affection de leur matériel tous les jours avant leur service. Cette pratique est obsolète, et avec le nombre grandissant de collaborateurs, de plus en plus compliquée  à gérer.

### Besoin

Le but est de concevoir une solution permettant de vérifier l'accès au local sécurisé autorisant l'accès au personne dont la photo est présente en base de données.

La réalistation de ce projet nécéssite de concevoir est développer une solution intelligente permettant l'extraction et la comparaison des photos prises par la webcam et celles présentes sur un serveur. 

La photo prise et le résultat de l'accès positif ou négatif s'affichera sur une interface utilisateur. En cas d'accès positif, un écran s'affichera permettant à l'utilisateur de cocher le matériel qu'il utilisera durant sa journé d'intervention.

Le choix de la solution se portera sur le développement d'un application développée en Java.


#### Les objectifs

* La prise de photo d'un agent de sécurité via une webcam
* La comparaison de cette photo avec un ensemble de photo d'identité déjà présentent sur un serveur distant
* Configuration d'une base de données Google Firebase intégrant notamment la liste des agents ainsi que le matériel
* Développer une application Java qui réalisera l'acquisition d'image, la comparaison de deux images, ainsi que l'affichage d'une interface utilisateur qui servira à la validation de l'accès au local ainsi qu'à l'affichage d'un formulaire de saisie du matériel
* Communication entre la base de données Firebase et l'application Java

#### Les cibles

L'application sera utilisée par l'ensemble des agents de sécurité de la société

#### Le Type de Solution

Un système sur-mesure de vérification de l'accès à un local, ainsi qu'à l'enregistrement de l'affection du matériel présent aux agents identifiés.

#### Les Besoins Fonctionnels

* Acquisition d'information : photo par webcam
* Vérification : controle d'identité via personne enregistré dans le système
* Réponse : rapport envoyé a l'application
* Enregistrement d'information : après identification, possiblité d'enregistrer le matériel qui sera utilisé

#### Les Besoins Non Fonctionnels

* Disponibilité permannente
* Performance
* Intégrité 
* Ergonomie

### Cahier des Charges
[Cahier des Charges](https://gitlab.com/MaximeMohandi/mspr-java/blob/master/2018-EPSI-TPRE511-1.0.pdf)