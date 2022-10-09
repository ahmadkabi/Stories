package ahmadkabi.storyapp.ui.home

import ahmadkabi.storyapp.R
import ahmadkabi.storyapp.ui.home.account.AccountFragment
import ahmadkabi.storyapp.ui.home.story.StoryFragment
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    private val titles = arrayOf(
        R.string.stories,
        R.string.account
    )

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 ->{ StoryFragment.newInstance()}
            1 ->{ AccountFragment.newInstance() }
            else -> { Fragment() }
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(titles[position])
    }

    override fun getCount(): Int {
        return 2
    }
}