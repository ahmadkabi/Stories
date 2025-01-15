package ahmadkabi.stories.ui.map

import ahmadkabi.stories.R
import ahmadkabi.stories.core.data.source.remote.StatusResponse
import ahmadkabi.stories.databinding.ActivityMapsBinding
import ahmadkabi.stories.domain.model.Story
import ahmadkabi.stories.helper.DialogUtils
import ahmadkabi.stories.helper.ItemDecorHorizontal
import ahmadkabi.stories.helper.showToast
import ahmadkabi.stories.ui.home.story.LoadingStateAdapter
import ahmadkabi.stories.ui.home.story.ViewModelFactory
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, MapStoryAdapter.ItemListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel: MapViewModel by lazy { ViewModelFactory(this).create(MapViewModel::class.java) }

    private val progressDialog: Dialog by lazy { DialogUtils.setProgressDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        buildRv()
        observe()

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        setMapStyle()

        progressDialog.show()
        viewModel.getMappedStories()

    }

    private fun observe() {

        viewModel.stories.observe(this) { result ->
            when (result.status) {
                StatusResponse.SUCCESS -> {

                    val stories: PagingData<Story> = PagingData.from(
                        result.body?.map {
                            Story(
                                id = it.id,
                                name = it.name,
                                description = it.description,
                                photoUrl = it.photoUrl,
                                createdAt = it.createdAt,
                                lat = it.lat,
                                lon = it.lon,
                            )
                        } as List<Story>
                    )
                    adapter.submitData(lifecycle, stories)

                    result.body?.forEach {
                        if (it.lat != null && it.lon != null) {
                            val latLng = LatLng(it.lat!!.toDouble(), it.lon!!.toDouble())
                            mMap.addMarker(
                                MarkerOptions()
                                    .position(latLng)
                                    .title(it.name)
                            )
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                        }
                    }

                    mMap.moveCamera(CameraUpdateFactory.zoomTo(4f))

                }

                StatusResponse.EMPTY -> {
                    showToast(getString(R.string.data_is_empty))
                }

                StatusResponse.ERROR -> {
                    showToast(getString(R.string.sorry_something_went_wrong))
                    finish()
                }
            }
            progressDialog.dismiss()
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private lateinit var adapter: MapStoryAdapter
    private fun buildRv() {
        adapter = MapStoryAdapter()
        adapter.listener = this

        val itemDecorHorizontal = ItemDecorHorizontal(
            resources.getDimension(R.dimen.dp_20).toInt(),
            resources.getDimension(R.dimen.dp_16).toInt(),
            resources.getDimension(R.dimen.dp_20).toInt(),
            resources.getDimension(R.dimen.dp_16).toInt(),
            resources.getDimension(R.dimen.dp_20).toInt()
        )

        binding.recyclerView.addItemDecoration(itemDecorHorizontal)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recyclerView.setHasFixedSize(false)


        binding.recyclerView.adapter = adapter.withLoadStateFooter(footer = LoadingStateAdapter {
            adapter.retry()
        })

    }


    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MapsActivity::class.java)
        }
    }

    override fun onItemClickListener(item: Story, optionsCompat: ActivityOptionsCompat) {
//        todo move selected recyclerview when map icon is clicked
        mMap.animateCamera(
            CameraUpdateFactory.newLatLng(
                LatLng(
                    item.lat!!.toDouble(),
                    item.lon!!.toDouble()
                )
            )
        )

        lifecycleScope.launch {
            adapter.setSelectedStory(item)
        }
        binding.recyclerView.smoothScrollToPosition(adapter.getItemPosition(item))
    }
}