package ahmadkabi.storyapp.ui.map

import ahmadkabi.storyapp.R
import ahmadkabi.storyapp.data.source.remote.StatusResponse
import ahmadkabi.storyapp.databinding.ActivityMapsBinding
import ahmadkabi.storyapp.helper.DialogUtils
import ahmadkabi.storyapp.helper.UserPreference
import ahmadkabi.storyapp.helper.showToast
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
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

        viewModel = ViewModelProvider(this)[MapViewModel::class.java].apply {
            token = UserPreference(this@MapsActivity).getToken()!!
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        observe()

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        progressDialog.show()
        viewModel.fetchStories()

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