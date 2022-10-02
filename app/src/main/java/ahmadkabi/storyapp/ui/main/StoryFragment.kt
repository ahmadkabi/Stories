package ahmadkabi.storyapp.ui.main

import ahmadkabi.storyapp.DetailActivity
import ahmadkabi.storyapp.R
import ahmadkabi.storyapp.databinding.FragmentStoryBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

    private lateinit var adapter: StoryAdapter
    private fun buildRv() {
        adapter = StoryAdapter()
        adapter.addItems(
            resources.getStringArray(R.array.recyclerview_items).toCollection(ArrayList())
        )
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

    override fun onItemClickListener(position: Int, item: String) {
        startActivity(DetailActivity.newIntent(requireContext()))
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