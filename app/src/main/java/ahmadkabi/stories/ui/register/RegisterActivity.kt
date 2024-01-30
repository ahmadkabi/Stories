package ahmadkabi.stories.ui.register

import ahmadkabi.stories.R
import ahmadkabi.stories.data.source.remote.StatusResponse
import ahmadkabi.stories.data.source.remote.model.RegisterBody
import ahmadkabi.stories.databinding.ActivityRegisterBinding
import ahmadkabi.stories.helper.DialogUtils
import ahmadkabi.stories.helper.showToast
import ahmadkabi.stories.ui.home.story.ViewModelFactory
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    private val progressDialog: Dialog by lazy { DialogUtils.setProgressDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        viewModel = ViewModelFactory(this).create(RegisterViewModel::class.java)

        observe()

        binding.imgSetting.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        binding.txHave.setOnClickListener {
            finish()
        }
        binding.btnRegister.setOnClickListener {
            register()
        }

    }

    private fun register() {
        progressDialog.show()

        viewModel.register(
            RegisterBody(
                binding.edRegisterName.text.toString(),
                binding.edRegisterEmail.text.toString(),
                binding.edRegisterPassword.text.toString()
            )
        )

    }

    private fun observe() {
        viewModel.register.observe(this) { result ->
            when (result.status) {
                StatusResponse.SUCCESS -> {
                    if (result.body?.error == false) {
                        showToast(getString(R.string.your_account_has_been_made))
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
            return Intent(context, RegisterActivity::class.java)
        }
    }

}