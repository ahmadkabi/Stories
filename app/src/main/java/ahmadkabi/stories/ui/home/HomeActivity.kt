package ahmadkabi.stories.ui.home

import ahmadkabi.stories.R
import ahmadkabi.stories.databinding.ActivityHomeBinding
import ahmadkabi.stories.ui.map.MapsActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        binding.viewPager.adapter = SectionsPagerAdapter(this, supportFragmentManager)
        binding.tabs.setupWithViewPager(binding.viewPager)

        binding.imgMap.setOnClickListener {
            startActivity(MapsActivity.newIntent(this))
        }

    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }

}

/**
 * TODO
 *  - implement spotless
 * */