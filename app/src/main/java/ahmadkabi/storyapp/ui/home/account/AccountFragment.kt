package ahmadkabi.storyapp.ui.home.account

import ahmadkabi.storyapp.databinding.FragmentAccountBinding
import ahmadkabi.storyapp.helper.UserPreference
import ahmadkabi.storyapp.ui.login.LoginActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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