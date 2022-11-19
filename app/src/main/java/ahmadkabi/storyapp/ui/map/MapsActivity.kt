package ahmadkabi.storyapp.ui.map

import ahmadkabi.storyapp.R
import ahmadkabi.storyapp.data.source.remote.StatusResponse
import ahmadkabi.storyapp.databinding.ActivityMapsBinding
import ahmadkabi.storyapp.helper.*
import ahmadkabi.storyapp.helper.UserPreference
import ahmadkabi.storyapp.ui.home.story.StoryViewModel
import ahmadkabi.storyapp.ui.home.story.ViewModelFactory
import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModel: MapViewModel

    private val progressDialog: Dialog by lazy { DialogUtils.setProgressDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelFactory(this).create(MapViewModel::class.java)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        observe()

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        progressDialog.show()
        viewModel.getMappedStories()

    }


    private fun observe() {

        viewModel.stories.observe(this) { result ->
            when (result.status) {
                StatusResponse.SUCCESS -> {

                    result.body?.forEach {
                        if (it.lat != null && it.lon != null) {
                            val latLng = LatLng(it.lat.toDouble(), it.lon.toDouble())
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
                }
            }

            progressDialog.dismiss()

        }

    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MapsActivity::class.java)
        }
    }
}