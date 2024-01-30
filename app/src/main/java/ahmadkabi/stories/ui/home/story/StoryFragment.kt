package ahmadkabi.stories.ui.home.story

import ahmadkabi.stories.R
import ahmadkabi.stories.data.source.remote.model.Story
import ahmadkabi.stories.databinding.FragmentStoryBinding
import ahmadkabi.stories.helper.*
import ahmadkabi.stories.ui.add.AddStoryActivity
import ahmadkabi.stories.ui.detail.DetailActivity
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


class StoryFragment : Fragment(), StoryListAdapter.ItemListener {

    private lateinit var viewModel: StoryViewModel
    private lateinit var binding: FragmentStoryBinding

    private val progressDialog: Dialog by lazy { DialogUtils.setProgressDialog(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelFactory(requireContext()).create(StoryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentStoryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buildRv()
        observe()

        progressDialog.show()

        binding.btnAdd.setOnClickListener {
            if (activity != null) {
                val intent = AddStoryActivity.newIntent(requireActivity())
                launcherIntentGallery.launch(intent)
            }
        }

    }

    private lateinit var adapter: StoryListAdapter
    private fun buildRv() {
        adapter = StoryListAdapter()
        adapter.listener = this

        val itemDecorVertical = ItemDecorVertical(
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


        binding.recyclerView.adapter = adapter.withLoadStateFooter(footer = LoadingStateAdapter {
            adapter.retry()
        })

    }

    private fun observe() {

        viewModel.getStories().observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)

            binding.txEmpty.gone()
            binding.imgEmpty.gone()

            progressDialog.dismiss()

            binding.recyclerView.layoutManager?.scrollToPosition(0)

        }

    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK && result.data?.getBooleanExtra(
                extraIsSuccess,
                false
            ) == true
        ) {

            progressDialog.show()
            adapter.refresh()
            observe()
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