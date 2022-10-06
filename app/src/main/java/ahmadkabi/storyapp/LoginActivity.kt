package ahmadkabi.storyapp

import ahmadkabi.storyapp.helper.UserPreference
import ahmadkabi.storyapp.databinding.ActivityLoginBinding
import ahmadkabi.storyapp.network.ApiConfig
import ahmadkabi.storyapp.network.LoginBody
import ahmadkabi.storyapp.network.LoginResponse
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val progressDialog: Dialog by lazy { DialogUtils.setProgressDialog(this) }

    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userPreference = UserPreference(this)
        if (userPreference.getUserName() != null) {
            startActivity(HomeActivity.newIntent(this@LoginActivity))

        } else {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

            progressDialog.setCancelable(false)

            binding.txWanna.setOnClickListener {
                startActivity(RegisterActivity.newIntent(this))
            }
            binding.btnLogin.setOnClickListener {
                login()
            }

        }

    }

    private fun login() {
        progressDialog.show()

        val body = LoginBody(
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString(),
        )
        val service = ApiConfig().getApiService().login(body)

        service.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        Toast.makeText(
                            this@LoginActivity,
                            responseBody.message,
                            Toast.LENGTH_SHORT
                        ).show()

                        userPreference.setUser(
                            responseBody.loginResult.name,
                            responseBody.loginResult.token
                        )
                        startActivity(HomeActivity.newIntent(this@LoginActivity))
                        finish()

                    }

                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        response.message(),
                        Toast.LENGTH_SHORT
                    ).show()
                    progressDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(
                    this@LoginActivity,
                    "Failed",
                    Toast.LENGTH_SHORT
                ).show()
                progressDialog.dismiss()

            }
        })
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

}