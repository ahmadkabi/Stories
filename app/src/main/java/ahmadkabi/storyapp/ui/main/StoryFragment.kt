package ahmadkabi.storyapp.ui.main

import ahmadkabi.storyapp.*
import ahmadkabi.storyapp.databinding.FragmentStoryBinding
import ahmadkabi.storyapp.helper.UserPreference
import ahmadkabi.storyapp.helper.gone
import ahmadkabi.storyapp.helper.visible
import ahmadkabi.storyapp.network.ApiConfig
import ahmadkabi.storyapp.network.GetStoriesResponse
import ahmadkabi.storyapp.network.Story
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryFragment : Fragment(), StoryAdapter.ItemListener {

    private lateinit var storyViewModel: StoryViewModel
    private var _binding: FragmentStoryBinding? = null

    private val progressDialog: Dialog by lazy { DialogUtils.setProgressDialog(requireContext()) }

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storyViewModel = ViewModelProvider(this)[StoryViewModel::class.java].apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
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


        storyViewModel.text.observe(viewLifecycleOwner) {
//            binding.sectionLabel.text = it
        }

        buildRv()

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true

            getStories()

        }

        getStories()

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

    override fun onItemClickListener(item: Story) {
//        val intent = DetailActivity.newIntent(requireContext())
//        intent.putExtra(extraUserName, item.name)
//        intent.putExtra(extraImageUrl, item.photoUrl)
//        intent.putExtra(extraDescription, item.description)
//        startActivity(intent)

    }


    private fun getStories() {
        progressDialog.show()

        val userPreference = UserPreference(requireContext())

        val service = ApiConfig().getApiService().getStories(
            "Bearer ${userPreference.getToken()}",
        )

        service.enqueue(object : Callback<GetStoriesResponse> {
            override fun onResponse(
                call: Call<GetStoriesResponse>,
                response: Response<GetStoriesResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {

                        if(responseBody.listStory.isNotEmpty()){
                            adapter.resetItems(response.body()?.listStory)

                            binding.txEmpty.gone()
                            binding.imgEmpty.gone()
                        }else{
                            binding.txEmpty.visible()
                            binding.imgEmpty.visible()
                        }

                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.loading_stories_is_failed_please_try_again_later),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                progressDialog.dismiss()
                binding.swipeRefresh.isRefreshing = false

            }

            override fun onFailure(call: Call<GetStoriesResponse>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.sorry_something_went_wrong_please_try_again_later),
                    Toast.LENGTH_SHORT
                ).show()
                progressDialog.dismiss()

            }
        })
    }

    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"

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