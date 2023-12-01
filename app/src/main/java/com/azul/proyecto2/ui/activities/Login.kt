package com.azul.proyecto2.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.azul.proyecto2.R
import android.content.Intent
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.azul.proyecto2.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private var email: String = ""
    private var contrasenia: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Para firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        if(firebaseAuth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }


        with(binding){
            binding.btnLogin.setOnClickListener {
                if(!validaCampos()) return@setOnClickListener

                //autenticamos al usuario
                autenticaUsuario(email, contrasenia)
            }

            binding.btnRegistrarse.setOnClickListener {
                if(!validaCampos()) return@setOnClickListener

                //registramos al usuario
                registraUsuario(email, contrasenia)
            }

            binding.tvRestablecerPassword.setOnClickListener {
                val resetEmail = EditText(it.context)
                resetEmail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

                val passwordResetDialog = AlertDialog.Builder(it.context)
                    .setTitle("Restablecer contraseña")
                    .setMessage("Ingresa el correo para restablecer su contraseña")
                    .setView(resetEmail)
                    .setPositiveButton("Enviar"){ _, _ ->
                        val mail = resetEmail.text.toString()
                        if(mail.isNotEmpty()){
                            firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener {
                                Toast.makeText(this@Login, "@strings/corr_contra_send", Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener {
                                Toast.makeText(this@Login, "@strings/mail_not_sent", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(this@Login, "@strings/enter_mail", Toast.LENGTH_SHORT).show()
                        }
                    }.setNegativeButton("Cancelar"){ dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }
    }

    private fun validaCampos(): Boolean{
        email = binding.tietEmail.text.toString().trim() //para que quite espacios en blanco
        contrasenia = binding.tietContrasenia.text.toString().trim()

        if(email.isEmpty()){
            binding.tietEmail.error = "Se requiere el correo"
            binding.tietEmail.requestFocus()
            return false
        }

        if(contrasenia.isEmpty() || contrasenia.length < 6){
            binding.tietContrasenia.error = "Se requiere una contraseña o la contraseña no tiene por lo menos 6 caracteres"
            binding.tietContrasenia.requestFocus()
            return false
        }

        return true
    }

    private fun manejaErrores(task: Task<AuthResult>){
        var errorCode = ""

        try{
            errorCode = (task.exception as FirebaseAuthException).errorCode
        }catch(e: Exception){
            e.printStackTrace()
        }

        //Texto de comparación no se almacena dentro de los strings, de otra forma
        //No funciona el programa
        when(errorCode){
            "ERROR_INVALID_EMAIL" -> {
                Toast.makeText(this, "@strings/mail_error_format", Toast.LENGTH_SHORT).show()
                binding.tietEmail.error = "Error: El correo electrónico no tiene un formato correcto"
                binding.tietEmail.requestFocus()
            }
            "ERROR_WRONG_PASSWORD" -> {
                Toast.makeText(this, "@strings/invalid_password", Toast.LENGTH_SHORT).show()
                binding.tietContrasenia.error = "Error: La contraseña no es válida"
                binding.tietContrasenia.requestFocus()
                binding.tietContrasenia.setText("")

            }
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> {
                //An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.
                Toast.makeText(this, "@strings/dif_account", Toast.LENGTH_SHORT).show()
            }
            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                Toast.makeText(this, "@strings/another_account", Toast.LENGTH_LONG).show()
                binding.tietEmail.error = ("Error: el correo electrónico ya está en uso con otra cuenta")
                binding.tietEmail.requestFocus()
            }
            "ERROR_USER_TOKEN_EXPIRED" -> {
                Toast.makeText(this, "@strings/session_exp", Toast.LENGTH_LONG).show()
            }
            "ERROR_USER_NOT_FOUND" -> {
                Toast.makeText(this, "@strings/no_user", Toast.LENGTH_LONG).show()
            }
            "ERROR_WEAK_PASSWORD" -> {
                Toast.makeText(this, "@strings/invalid_pass", Toast.LENGTH_LONG).show()
                binding.tietContrasenia.error = "La contraseña porporcionada es inválida"
                binding.tietContrasenia.requestFocus()
            }
            "NO_NETWORK" -> {
                Toast.makeText(this, "@strings/no_network_av", Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(this, "Error. No se pudo autenticar exitosamente", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun autenticaUsuario(usr: String, psw: String){
        binding.progressBar.visibility = View.VISIBLE

        firebaseAuth.signInWithEmailAndPassword(usr, psw).addOnCompleteListener { authResult ->
            binding.progressBar.visibility = View.GONE

            if(authResult.isSuccessful){
                Toast.makeText(this, "@strings/correct_auth", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                manejaErrores(authResult)
            }
        }
    }

    private fun registraUsuario(usr: String, psw: String){
        binding.progressBar.visibility = View.VISIBLE

        firebaseAuth.createUserWithEmailAndPassword(usr, psw).addOnCompleteListener { authResult ->

            if(authResult.isSuccessful){

                val user_fb = firebaseAuth.currentUser

                user_fb?.sendEmailVerification()?.addOnSuccessListener {
                    Toast.makeText(this, "@strings/ver_email_sent", Toast.LENGTH_SHORT).show()
                }?.addOnFailureListener {
                    Toast.makeText(this, "@strings/mail_error", Toast.LENGTH_SHORT).show()
                }

                Toast.makeText(this, "@strings/user_create", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                manejaErrores(authResult)
            }


        }
    }
}