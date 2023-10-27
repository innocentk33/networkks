package fr.istic.mob.networkKS.models

import com.google.gson.annotations.SerializedName
import fr.istic.mob.networkKS.Connexion
import java.io.Serializable

data class Graph(
    @SerializedName("Objets")
    var objets: ArrayList<Objet>,
    @SerializedName("Connexions")
    var connexions: ArrayList<Connexion>,
) : Serializable
