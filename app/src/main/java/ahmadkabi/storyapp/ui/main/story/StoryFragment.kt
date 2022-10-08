package ahmadkabi.storyapp.ui.main.story

import ahmadkabi.storyapp.*
import ahmadkabi.storyapp.data.source.remote.StatusResponse
import ahmadkabi.storyapp.data.source.remote.model.Story
import ahmadkabi.storyapp.databinding.FragmentStoryBinding
import ahmadkabi.storyapp.helper.*
import ahmadkabi.storyapp.helper.UserPreference
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StoryFragment : Fragment(), StoryAdapter.ItemListener {

    private lateinit var viewModel: StoryViewModel
    private var _binding: FragmentStoryBinding? = null

    private val progressDialog: Dialog by lazy { DialogUtils.setProgressDialog(requireContext()) }

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[StoryViewModel::class.java].apply {
            token = UserPreference(requireContext()).getToken()!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStoryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()
        buildRv()

        progressDialog.show()
        viewModel.fetchStories()

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            progressDialog.show()

            viewModel.fetchStories()

        }

    }

    private lateinit var adapter: StoryAdapter
    private fun buildRv() {
        adapter = StoryAdapter()
        adapter.listener = this

        val itemDecorVertical =
            ItemDecorVertical(
                resources.getDimension(R.dimen.dp_90).toInt(),
                resources.getDimension(R.dimen.dp_16).toInt(),
                resources.getDimension(R.dimen.dp_20).toInt(),
                resources.getDimension(R.dimen.dp_16).toInt(),
                resources.getDimension(R.dimen.dp_20).toInt()
            )
        binding.recyclerView.addItemDecoration(itemDecorVertical)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerView.setHasFixedSize(false)
        binding.recyclerView.adapter = adapter

    }

    private fun observe(){

        viewModel.stories.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                StatusResponse.SUCCESS -> {
                    adapter.resetItems(result.body)

                    binding.txEmpty.gone()
                    binding.imgEmpty.gone()
                }
                StatusResponse.EMPTY -> {
                    binding.txEmpty.visible()
                    binding.imgEmpty.visible()
                }
                StatusResponse.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        result.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            progressDialog.dismiss()
            if (binding.swipeRefresh.isRefreshing) binding.swipeRefresh.isRefreshing = false
        }

    }

    override fun onItemClickListener(item: Story) {
//        val intent = DetailActivity.newIntent(requireContext())
//        intent.putExtra(extraUserName, item.name)
//        intent.putExtra(extraImageUrl, item.photoUrl)
//        intent.putExtra(extraDescription, item.description)
//        startActivity(intent)

    }

    companion object {

        @JvmStatic
        fun newInstance(): StoryFragment {
            return StoryFragment().apply {
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