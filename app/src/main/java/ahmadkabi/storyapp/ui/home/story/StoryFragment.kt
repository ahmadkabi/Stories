package ahmadkabi.storyapp.ui.home.story

import ahmadkabi.storyapp.R
import ahmadkabi.storyapp.data.source.remote.model.Story
import ahmadkabi.storyapp.databinding.FragmentStoryBinding
import ahmadkabi.storyapp.helper.*
import ahmadkabi.storyapp.ui.add.AddStoryActivity
import ahmadkabi.storyapp.ui.detail.DetailActivity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.activity.viewModels

class StoryFragment : Fragment(), StoryAdapter2.ItemListener {

    private lateinit var viewModel: StoryViewModel
    private lateinit var binding: FragmentStoryBinding

    private val progressDialog: Dialog by lazy { DialogUtils.setProgressDialog(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelFactory(requireContext()).create(StoryViewModel::class.java).apply {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentStoryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()
        buildRv()

        getData()

        progressDialog.show()
//        viewModel.fetchStories()

        binding.btnMake.setOnClickListener {
            if (activity != null) {
                val intent = AddStoryActivity.newIntent(requireActivity())
                launcherIntentGallery.launch(intent)
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            progressDialog.show()

//            viewModel.fetchStories()

        }

    }

    private lateinit var adapter: StoryAdapter2
    private fun buildRv() {
        adapter = StoryAdapter2()
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

        binding.recyclerView.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        binding.recyclerView.adapter = adapter

    }

    private fun observe() {
        viewModel.stories.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
            binding.txEmpty.gone()
            binding.imgEmpty.gone()

            progressDialog.dismiss()
            if (binding.swipeRefresh.isRefreshing) binding.swipeRefresh.isRefreshing = false
        }

//        viewModel.stories.observe(viewLifecycleOwner) { result ->
//            when (result.status) {
//                StatusResponse.SUCCESS -> {
//                    if(result.body != null){
//                        adapter.submitData(lifecycle, result.body)
//                        binding.txEmpty.gone()
//                        binding.imgEmpty.gone()
//
//                    }
//                }
//                StatusResponse.EMPTY -> {
//                    binding.txEmpty.visible()
//                    binding.imgEmpty.visible()
//                }
//                StatusResponse.ERROR -> {
//                    showToast(getString(R.string.sorry_something_went_wrong))
//                }
//            }
//
//            progressDialog.dismiss()
//            if (binding.swipeRefresh.isRefreshing) binding.swipeRefresh.isRefreshing = false
//        }

    }


    private fun getData() {
        val adapter = QuoteListAdapter()
        binding.recyclerView.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.quote.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)

            binding.txEmpty.gone()
            binding.imgEmpty.gone()

            progressDialog.dismiss()
            if (binding.swipeRefresh.isRefreshing) binding.swipeRefresh.isRefreshing = false

        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (
            result.resultCode == AppCompatActivity.RESULT_OK &&
            result.data?.getBooleanExtra(extraIsSuccess, false) == true
        ) {

            progressDialog.show()
//            viewModel.fetchStories() todo

        }
    }

    override fun onItemClickListener(item: Story, optionsCompat: ActivityOptionsCompat) {
        val intent = DetailActivity.newIntent(requireContext())
        intent.putExtra(extraUserName, item.name)
        intent.putExtra(extraImageUrl, item.photoUrl)
        intent.putExtra(extraDescription, item.description)
        startActivity(intent, optionsCompat.toBundle())

    }

    companion object {

        fun newInstance(): StoryFragment {
            return StoryFragment()
        }
    }

}