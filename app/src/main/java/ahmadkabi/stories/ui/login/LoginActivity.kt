package ahmadkabi.stories.ui.login

import ahmadkabi.stories.R
import ahmadkabi.stories.databinding.ActivityLoginBinding
import ahmadkabi.stories.helper.DialogUtils
import ahmadkabi.stories.helper.UserPreference
import ahmadkabi.stories.helper.showToast
import ahmadkabi.stories.ui.home.HomeActivity
import ahmadkabi.stories.ui.home.story.ViewModelFactory
import ahmadkabi.stories.ui.register.RegisterActivity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import source.remote.StatusResponse

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by lazy { ViewModelFactory(this).create(LoginViewModel::class.java) }

    private val progressDialog: Dialog by lazy { DialogUtils.setProgressDialog(this) }

    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userPreference = UserPreference(this)
        if (userPreference.getUserName() != null) {
            startActivity(HomeActivity.newIntent(this@LoginActivity))
            finish()

        } else {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

            observe()

            binding.imgSetting.setOnClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
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

        viewModel.login(
            model.LoginBody(
                binding.edLoginEmail.text.toString(),
                binding.edLoginPassword.text.toString()
            )
        )
    }

    private fun observe() {
        viewModel.login.observe(this) { result ->
            when (result.status) {
                StatusResponse.SUCCESS -> {
                    if (result.body != null) {
                        val userName = result.body!!.loginResult.name

                        showToast("${getString(R.string.welcome)} $userName")

                        userPreference.setUser(
                            userName,
                            result.body!!.loginResult.token
                        )
                        startActivity(HomeActivity.newIntent(this@LoginActivity))
                        finish()

                    } else {
                        showToast(getString(R.string.operation_is_failed))
                    }
                }
                StatusResponse.ERROR -> {
                    showToast(getString(R.string.sorry_something_went_wrong))
                }
                StatusResponse.EMPTY -> {}
            }

            progressDialog.dismiss()
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

}