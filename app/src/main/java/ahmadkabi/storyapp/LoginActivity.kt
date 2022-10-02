package ahmadkabi.storyapp

import ahmadkabi.storyapp.databinding.ActivityLoginBinding
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.txWanna.setOnClickListener {
            startActivity(RegisterActivity.newIntent(this))
        }

        binding.btnLogin.setOnClickListener {
            startActivity(HomeActivity.newIntent(this))
        }

    }


    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

}