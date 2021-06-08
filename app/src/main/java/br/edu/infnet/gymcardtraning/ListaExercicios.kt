package br.edu.infnet.gymcardtraning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater

import br.edu.infnet.gymcardtraning.databinding.ActivityListaExerciciosBinding

class ListaExercicios : AppCompatActivity() {

    private lateinit var binding: ActivityListaExerciciosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaExerciciosBinding.inflate(LayoutInflater)
        setContentView(binding.root)
    }
}