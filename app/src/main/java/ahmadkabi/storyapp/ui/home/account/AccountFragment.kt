package ahmadkabi.storyapp.ui.home.account

import ahmadkabi.storyapp.R
import ahmadkabi.storyapp.ui.add.AddStoryActivity
import ahmadkabi.storyapp.ui.login.LoginActivity
import ahmadkabi.storyapp.databinding.FragmentAccountBinding
import ahmadkabi.storyapp.helper.*
import ahmadkabi.storyapp.helper.UserPreference
import ahmadkabi.storyapp.ui.home.HomeActivity
import ahmadkabi.storyapp.ui.home.SectionsPagerAdapter
import ahmadkabi.storyapp.ui.home.story.StoryFragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.actionLogout.setOnClickListener {
            val userPreference = UserPreference(requireContext())
            userPreference.clear()

            startActivity(LoginActivity.newIntent(requireContext()))
            activity?.finish()
        }

        val userPreference = UserPreference(requireContext())
        val userName = userPreference.getUserName() ?: ""
        binding.txAvatar.text = userName[0].toString().uppercase()
        binding.txName.text = userName

    }

    companion object {

        fun newInstance(): AccountFragment {
            return AccountFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}