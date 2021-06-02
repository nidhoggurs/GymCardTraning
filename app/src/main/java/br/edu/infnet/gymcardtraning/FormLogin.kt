package br.edu.infnet.gymcardtraning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.infnet.gymcardtraning.databinding.ActivityFormLoginBinding

class FormLogin : AppCompatActivity() {

    lateinit var binding: ActivityFormLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()

        binding.txtTelaCadastro.setOnClickListener{
            val intent = Intent(this,FormCadastro::class.java)
            startActivity(intent)
        }

    }
}