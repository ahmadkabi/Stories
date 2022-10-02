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
            .load("https://story-api.dicoding.dev/images/stories/photos-1664615087142_m3gc6pE4.jpg")
//            .transform(
//                CenterCrop(),
//                RoundedCorners(resources.getDimensionPixelSize(R.dimen.dp_30))
//            )
            .into(binding.imgStory)

    }


    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, DetailActivity::class.java)
        }
    }

}