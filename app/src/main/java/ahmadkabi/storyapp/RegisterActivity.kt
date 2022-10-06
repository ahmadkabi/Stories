package ahmadkabi.storyapp

import ahmadkabi.storyapp.databinding.ActivityRegisterBinding
import ahmadkabi.storyapp.network.ApiConfig
import ahmadkabi.storyapp.network.RegisterBody
import ahmadkabi.storyapp.network.RegisterResponse
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

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val progressDialog: Dialog by lazy { DialogUtils.setProgressDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        progressDialog.setCancelable(false)

        binding.txHave.setOnClickListener {
            finish()
        }
        binding.btnRegister.setOnClickListener {
            register()
        }

    }

    private fun register() {
        progressDialog.show()

        val body = RegisterBody(
            binding.etName.text.toString(),
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString()
        )
        val service = ApiConfig().getApiService().register(body)

        service.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        Toast.makeText(
                            this@RegisterActivity,
                            getString(R.string.new_user_has_been_made),
                            Toast.LENGTH_SHORT
                        ).show()

                        finish()

                    }

                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        response.message(),
                        Toast.LENGTH_SHORT
                    ).show()
                    progressDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Failed",
                    Toast.LENGTH_SHORT
                ).show()
                progressDialog.dismiss()

            }
        })
    }


    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, RegisterActivity::class.java)
        }
    }

}