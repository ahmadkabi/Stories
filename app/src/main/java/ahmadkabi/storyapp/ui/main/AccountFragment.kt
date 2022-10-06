package ahmadkabi.storyapp.ui.main

import ahmadkabi.storyapp.AddStoryActivity
import ahmadkabi.storyapp.LoginActivity
import ahmadkabi.storyapp.databinding.FragmentAccountBinding
import ahmadkabi.storyapp.helper.UserPreference
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class AccountFragment : Fragment() {

    private lateinit var accountViewModel: AccountViewModel
    private var _binding: FragmentAccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java].apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMake.setOnClickListener {
            startActivity(AddStoryActivity.newIntent(requireContext()))
        }
        binding.btnLogout.setOnClickListener {
            val userPreference = UserPreference(requireContext())
            userPreference.clear()

            startActivity(LoginActivity.newIntent(requireContext()))
            activity?.finish()
        }

        accountViewModel.text.observe(viewLifecycleOwner) {
//            binding.sectionLabel.text = it
        }

        val userPreference = UserPreference(requireContext())
        val userName = userPreference.getUserName() ?: ""
        binding.txAvatar.text = userName[0].toString().uppercase()
        binding.txName.text = userName

    }

    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(): AccountFragment {
            return AccountFragment().apply {
                arguments = Bundle().apply {
//                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}