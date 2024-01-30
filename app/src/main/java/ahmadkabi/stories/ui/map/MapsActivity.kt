package ahmadkabi.stories.ui.map

import ahmadkabi.stories.R
import ahmadkabi.stories.data.source.remote.StatusResponse
import ahmadkabi.stories.databinding.ActivityMapsBinding
import ahmadkabi.stories.helper.DialogUtils
import ahmadkabi.stories.helper.showToast
import ahmadkabi.stories.ui.home.story.ViewModelFactory
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
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

        setMapStyle()

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

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MapsActivity::class.java)
        }
    }
}