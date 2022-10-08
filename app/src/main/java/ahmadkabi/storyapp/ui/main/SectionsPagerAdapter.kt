package ahmadkabi.storyapp.ui.main

import ahmadkabi.storyapp.R
import ahmadkabi.storyapp.ui.main.account.AccountFragment
import ahmadkabi.storyapp.ui.main.story.StoryFragment
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
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