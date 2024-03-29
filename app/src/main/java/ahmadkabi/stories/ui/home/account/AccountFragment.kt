package ahmadkabi.stories.ui.home.account

import ahmadkabi.stories.databinding.FragmentAccountBinding
import ahmadkabi.stories.helper.UserPreference
import ahmadkabi.stories.ui.login.LoginActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAccountBinding.inflate(inflater, container, false)

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

}