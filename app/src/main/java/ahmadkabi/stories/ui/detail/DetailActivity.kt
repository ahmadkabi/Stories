package ahmadkabi.stories.ui.detail

import ahmadkabi.stories.R
import ahmadkabi.stories.databinding.ActivityDetailBinding
import ahmadkabi.stories.helper.extraDescription
import ahmadkabi.stories.helper.extraImageUrl
import ahmadkabi.stories.helper.extraUserName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        binding.imgBack.setOnClickListener { onBackPressed() }

        Glide
            .with(this)
            .load(intent.getStringExtra(extraImageUrl))
            .into(binding.ivDetailPhoto)

        val userName: String = intent.getStringExtra(extraUserName) ?: ""
        binding.txAvatar.text = userName[0].toString().uppercase()
        binding.tvDetailName.text = userName

        binding.tvDetailDescription.text = intent.getStringExtra(extraDescription)

    }


    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, DetailActivity::class.java)
        }
    }

}