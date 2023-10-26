package fr.istic.mob.networkKS.models

import fr.istic.mob.networkKS.Connexion

data class Graph(

    var objets: MutableList<Objet> = mutableListOf(),
    var connexions: MutableList<Connexion> = mutableListOf(),


)
