package br.edu.infnet.gymcardtraning

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import br.edu.infnet.gymcardtraning.databinding.ActivityHomeBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.paypal.android.sdk.payments.*
import java.math.BigDecimal

class Home : AppCompatActivity() {

    private val REQUEST_CODE_PAYPAL = 1001
    private lateinit var binding: ActivityHomeBinding
    lateinit var toggle: ActionBarDrawerToggle

    private val client_id: String = "ASZmg8ClJj0XYq2MZkqgJI_tpxQ9JdK7qxrSDhA4SCvjxRsasANSHk3ToeIDgxZrGu1j_BRef2pAqKOh"

    private lateinit var payPalConfiguration: PayPalConfiguration
    private lateinit var payPalService: Intent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpPayPal()

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {

            when(it.itemId){
                R.id.nav_home -> Toast.makeText(applicationContext, "Clicou Home",Toast.LENGTH_SHORT).show()
                R.id.nav_exercicios -> Toast.makeText(applicationContext, "Clicou Exercicios",Toast.LENGTH_SHORT).show()
                R.id.nav_mensalidade -> Toast.makeText(applicationContext, "Clicou Mensalidade",Toast.LENGTH_SHORT).show()
                R.id.nav_deslogar ->{
                    FirebaseAuth.getInstance().signOut()
                    VoltarTelaLogin()
                }
            }

            true
        }

    }

    fun setUpPayPal() {
        //Conf do solicitador/conector
        payPalConfiguration = PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(client_id)

        // A Intente do solicitador/conector
        payPalService = Intent(
            this, PayPalService::class.java
        ).apply {
            putExtra(
                PayPalService.EXTRA_PAYPAL_CONFIGURATION,
                payPalConfiguration
            )
        }
        // Iniciando serviço
        startService(payPalService)
    }

    override fun onDestroy() {
        stopService(payPalService)
        super.onDestroy()
    }

    fun startpayPalProcess(view: View){
        //produto
        val price = BigDecimal(49.90)
        val product = "Mensalidade"

        //configuração mensalidade
        val payPayPalPayment = PayPalPayment(
            price,
            "BRL",
            product,
            PayPalPayment.PAYMENT_INTENT_SALE

        )

        //Activity para chamar o paypal
        val paymentIntent = Intent (
            this, PaymentActivity::class.java
                ).apply {
                    putExtra(
                        PayPalService.EXTRA_PAYPAL_CONFIGURATION,
                        payPalConfiguration
                    )
            putExtra(
                PaymentActivity.EXTRA_PAYMENT,
                payPayPalPayment
            )
        }

        startActivityForResult(paymentIntent, REQUEST_CODE_PAYPAL)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode){
            REQUEST_CODE_PAYPAL -> {
                when (resultCode){
                    Activity.RESULT_OK -> {
                        val paymentConfirmation: PaymentConfirmation? =
                            data?.getParcelableExtra(
                                PaymentActivity.EXTRA_RESULT_CONFIRMATION
                            )
                        if (paymentConfirmation != null){
                            val details = paymentConfirmation.toJSONObject()
                            Log.i("PayPalConfirm", "$details")
                        }
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun VoltarTelaLogin() {
        val intent = Intent(this, FormLogin::class.java)
        startActivity(intent)
        finish()
    }
}