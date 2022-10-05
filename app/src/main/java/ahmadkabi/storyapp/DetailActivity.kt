package ahmadkabi.storyapp

import ahmadkabi.storyapp.databinding.ActivityDetailBinding
import ahmadkabi.storyapp.databinding.ActivityHomeBinding
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        binding.imgBack.setOnClickListener { finish() }

        Glide
            .with(this)
            .load(intent.getStringExtra(extraImageUrl))
            .into(binding.imgStory)

        val userName: String  = intent.getStringExtra(extraUserName) ?: ""
        binding.txAvatar.text = userName[0].toString().uppercase()
        binding.txName.text = userName

        binding.txDescription.text = intent.getStringExtra(extraDescription)

    }


    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, DetailActivity::class.java)
        }
    }

}