package br.edu.infnet.gymcardtraning

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import br.edu.infnet.gymcardtraning.Model.Dados
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_cadastro_exercicios.*
import java.util.*


class CadastroExercicios : AppCompatActivity() {
    private var SelecionarUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_exercicios)

        bt_selecionar_foto.setOnClickListener {
            SelecionarFotoDaGaleria()
        }
        bt_cadastrar_exercicio_firebase.setOnClickListener {
            SalvarDadosNoFirebase()
        }


    }

    private fun SalvarDadosNoFirebase() {
        val nomeArquivo = UUID.randomUUID().toString()
        val referencia = FirebaseStorage.getInstance().getReference(
            "/imagens/${nomeArquivo}" )

        SelecionarUri?.let {
            referencia.putFile(it)
                .addOnSuccessListener {
                    referencia.downloadUrl.addOnSuccessListener {
                        val url = it.toString()
                        val nomeExercicio = edit_nome_exercicio.text.toString()
                        val tempoExercicio = edit_qtd_exercicio.text.toString()
                        val descansoExercicio = edit_descanso_exercicio.text.toString()
                        val uid = FirebaseAuth.getInstance().uid

                        val Exercicios = Dados(url, nomeExercicio,tempoExercicio,descansoExercicio)

                        FirebaseFirestore.getInstance().collection("Exercicios")
                            .add(Exercicios).addOnSuccessListener {

                            }.addOnFailureListener {
                                Toast.makeText(this, "Exercicio cadastrado com sucesso!", Toast.LENGTH_LONG).show()
                            }


                    }
                }
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0){
            SelecionarUri = data?.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, SelecionarUri)
            foto_exercicio.setImageBitmap(bitmap)
            bt_selecionar_foto.alpha = 0f
        }
    }

    private fun SelecionarFotoDaGaleria() {
        val intent = Intent(Intent.ACTION_PICK)
        startActivityForResult(intent,0)
    }


}