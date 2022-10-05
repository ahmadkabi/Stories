package ahmadkabi.storyapp

import ahmadkabi.storyapp.databinding.ActivityLoginBinding
import ahmadkabi.storyapp.network.ApiConfig
import ahmadkabi.storyapp.network.LoginBody
import ahmadkabi.storyapp.network.LoginResponse
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.txWanna.setOnClickListener {
            startActivity(RegisterActivity.newIntent(this))
        }

        binding.btnLogin.setOnClickListener {
            login()
        }

    }


    private fun login() {
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

                        startActivity(HomeActivity.newIntent(this@LoginActivity))

                    }

                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        response.message(),
                        Toast.LENGTH_SHORT
                    ).show()
//                        progressDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(
                    this@LoginActivity,
                    "Gagal instance Retrofit",
                    Toast.LENGTH_SHORT
                ).show()
//                    progressDialog.dismiss()

            }
        })
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

}