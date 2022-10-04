package ahmadkabi.storyapp.ui.main

import ahmadkabi.storyapp.DetailActivity
import ahmadkabi.storyapp.R
import ahmadkabi.storyapp.databinding.FragmentStoryBinding
import ahmadkabi.storyapp.network.ApiConfig
import ahmadkabi.storyapp.network.GetStoriesResponse
import ahmadkabi.storyapp.network.Story
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        buildRv()

        storyViewModel.text.observe(viewLifecycleOwner) {
//            binding.sectionLabel.text = it
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    override fun onItemClickListener(position: Int, item: Story) {
        startActivity(DetailActivity.newIntent(requireContext()))
    }


    private fun getStories() {
        val service = ApiConfig().getApiService().getStories(
            "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXBnMUloQlNqdG5BbUx2MG8iLCJpYXQiOjE2NjQ0MjMzMTJ9.ejFVl6IqyVJmbV6uNw723MWCskr9HcVhqeIiWPGrb3k",
        )

        service.enqueue(object : Callback<GetStoriesResponse> {
            override fun onResponse(
                call: Call<GetStoriesResponse>,
                response: Response<GetStoriesResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        Toast.makeText(
                            requireContext(),
                            responseBody.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    adapter.addItems(response.body()?.listStory)

                } else {
                    Toast.makeText(
                        requireContext(),
                        response.message(),
                        Toast.LENGTH_SHORT
                    ).show()
//                        progressDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<GetStoriesResponse>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Gagal instance Retrofit",
                    Toast.LENGTH_SHORT
                ).show()
//                    progressDialog.dismiss()

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