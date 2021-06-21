package br.edu.infnet.gymcardtraning

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import br.edu.infnet.gymcardtraning.databinding.ActivityFormLoginBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.android.synthetic.main.activity_form_login.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class FormLogin : AppCompatActivity() {

    lateinit var binding: ActivityFormLoginBinding
    var firebaseAuth: FirebaseAuth?=null
    var callbackManager: CallbackManager?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //printKeyHash()

        firebaseAuth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()

        btn_loginFace.setReadPermissions("email")
        btn_loginFace.setOnClickListener{
            signIn()
        }

        supportActionBar!!.hide()
        VerificarUsuarioLogado()

        binding.txtTelaCadastro.setOnClickListener{
            val intent = Intent(this,FormCadastro::class.java)
            startActivity(intent)
        }
        binding.btEntrar.setOnClickListener {
            val email  = binding.editEmail.text.toString()
            val senha  = binding.editSenha.text.toString()
            val mensagem_erro = binding.mensagemErro

            if (email.isEmpty() || senha.isEmpty()){
                mensagem_erro.setText("Preencha todos os campos!")

            } else{
                AutenticarUsuario()
            }
        }

        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        adView.visibility = View.GONE

        adView.adListener = object : AdListener(){
            override fun onAdLoaded() {
                adView.visibility = View.VISIBLE
                super.onAdLoaded()
            }
        }

    }

    private fun signIn() {
        btn_loginFace.registerCallback(callbackManager,object: FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {
                handleFacebookAccessToken(result!!.accessToken)
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException?) {

            }

        })
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken?) {
        //get Credencial

        val credential = FacebookAuthProvider.getCredential(accessToken!!.token)
        firebaseAuth!!.signInWithCredential(credential)
            .addOnFailureListener { e->
                Toast.makeText(this, e.message,Toast.LENGTH_LONG).show()
            }
            .addOnSuccessListener {
                Toast.makeText(this, "Login efetuado com Sucesso!" ,Toast.LENGTH_LONG).show()
                IrParaTelaHome()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode,resultCode,data)
    }

    /*private fun printKeyHash() {
        try {
            val info = packageManager.getPackageInfo("br.edu.infnet.gymcardtraning", PackageManager.GET_SIGNATURES)
            for(signature in info.signatures)
            {
                val md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray())
                Log.e("KEYHASH", Base64.encodeToString(md.digest(),Base64.DEFAULT))
            }
        }
        catch (e:PackageManager.NameNotFoundException)
        {

        }
        catch (e:NoSuchAlgorithmException)
        {

        }
    }*/

    private fun AutenticarUsuario() {
        val email  = binding.editEmail.text.toString()
        val senha  = binding.editSenha.text.toString()
        val mensagem_erro = binding.mensagemErro
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,senha).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(this, "Login efetuado com Sucesso!", Toast.LENGTH_LONG).show()
                IrParaTelaHome()
            }

        }.addOnFailureListener {

            var erro = it

            when{
                erro is FirebaseAuthInvalidCredentialsException -> mensagem_erro.setText("E-mail ou senha estão incorretos.")
                erro is FirebaseNetworkException -> mensagem_erro.setText("Sem conexão na internet.")
                else -> mensagem_erro.setText("Erro ao logar usuario")
            }
        }
    }

    private fun VerificarUsuarioLogado(){
        val usuarioLogado = FirebaseAuth.getInstance().currentUser
        if (usuarioLogado != null ){
            IrParaTelaHome()
        }
    }

    private fun IrParaTelaHome(){
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }
}