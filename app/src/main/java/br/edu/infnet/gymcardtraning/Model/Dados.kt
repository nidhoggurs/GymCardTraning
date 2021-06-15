package br.edu.infnet.gymcardtraning.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Dados(
    val uid: String = "",
    val nomeExercicio: String = "",
    val tempoExercicio: String = "",
    val descansoExercicio: String = "",
    val url: String = ""


):Parcelable