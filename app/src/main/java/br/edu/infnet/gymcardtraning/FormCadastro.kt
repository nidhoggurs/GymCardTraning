package br.edu.infnet.gymcardtraning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.edu.infnet.gymcardtraning.databinding.ActivityFormCadastroBinding
import br.edu.infnet.gymcardtraning.databinding.ActivityFormLoginBinding
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class FormCadastro : AppCompatActivity() {

    lateinit var binding: ActivityFormCadastroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()
        Toobar()

        binding.btCadastro.setOnClickListener {
            val nome = binding.editNome.text.toString()
            val email = binding.editEmail.text.toString()
            val telefone = binding.editTelefone.text.toString()
            val idade = binding.editIdade.text.toString()
            val altura = binding.editAltura.text.toString()
            val peso = binding.editPeso.text.toString()
            val senha = binding.editSenha.text.toString()
            val mensagem_erro = binding.mensagemErro
            if (nome.isEmpty() ||
                email.isEmpty()||
                telefone.isEmpty() ||
                idade.isEmpty() ||
                altura.isEmpty() ||
                peso.isEmpty() ||
                senha.isEmpty()){

                mensagem_erro.setText("Preencha todos os campos!")
            } else {
                CadastrarUsuario()
            }




        }



    }

    private fun CadastrarUsuario() {

        val email = binding.editEmail.text.toString()

        val senha = binding.editSenha.text.toString()
        val mensagem_erro = binding.mensagemErro

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(this, "Usuario cadastrado com sucesso!",Toast.LENGTH_LONG).show()
                binding.editNome.setText("")
                binding.editEmail.setText("")
                binding.editTelefone.setText("")
                binding.editIdade.setText("")
                binding.editAltura.setText("")
                binding.editPeso.setText("")
                binding.editSenha.setText("")
            }
        }.addOnFailureListener {
            var erro = it

            when{
                erro is FirebaseAuthWeakPasswordException -> mensagem_erro.setText("Digite uma senha com no minimo 6 caracteres.")
                erro is FirebaseAuthUserCollisionException -> mensagem_erro.setText("Esta conta ja foi cadastrada.")
                erro is FirebaseNetworkException -> mensagem_erro.setText("Sem conexão na internet.")
                else -> mensagem_erro.setText("Erro ao cadastrar usuario.")
            }


        }
    }

    private fun Toobar(){
        val toolbar = binding.toobarCadastro
        toolbar.setBackgroundColor(getColor(R.color.black))
        toolbar.setNavigationIcon(getDrawable(R.drawable.logo_inicial))
    }
}