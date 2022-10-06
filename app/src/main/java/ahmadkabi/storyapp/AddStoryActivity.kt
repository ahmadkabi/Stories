package ahmadkabi.storyapp

import ahmadkabi.storyapp.databinding.ActivityAddStoryBinding
import ahmadkabi.storyapp.helper.*
import ahmadkabi.storyapp.network.ApiConfig
import ahmadkabi.storyapp.network.AddStoryResponse
import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    private val progressDialog: Dialog by lazy { DialogUtils.setProgressDialog(this) }

    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    private val REQUEST_CODE_PERMISSIONS = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_story)

        binding.imgBack.setOnClickListener { finish() }
        binding.btnCamera.setOnClickListener { startTakePhoto() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnMake.setOnClickListener { uploadImage() }
        binding.imgCancel.setOnClickListener {
            getFile = null
            binding.imgStory.setImageBitmap(null)
            binding.imgCancel.gone()

            binding.btnCamera.visible()
            binding.btnGallery.visible()
            binding.btnMake.gone()
        }


        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }


    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "ahmadkabi.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)

            binding.imgStory.setImageBitmap(result)
            binding.imgCancel.visible()
            binding.btnCamera.gone()
            binding.btnGallery.gone()
            binding.btnMake.visible()
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this@AddStoryActivity)

            getFile = myFile

            binding.imgStory.setImageURI(selectedImg)
            binding.imgCancel.visible()
            binding.btnCamera.gone()
            binding.btnGallery.gone()
            binding.btnMake.visible()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.no_camera_permission),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }


    private fun uploadImage() {
        if (getFile != null) {
            binding.imgStory

            progressDialog.show()

            val file = reduceFileImage(getFile as File)

            val description = binding.etDescription.text
                .toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            val userPreference = UserPreference(this)

            val service = ApiConfig().getApiService().uploadImage(
                "Bearer ${userPreference.getToken()}",
                imageMultipart,
                description
            )

            service.enqueue(object : Callback<AddStoryResponse> {
                override fun onResponse(
                    call: Call<AddStoryResponse>,
                    response: Response<AddStoryResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            Toast.makeText(
                                this@AddStoryActivity,
                                responseBody.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        finish()
                    } else {
                        Toast.makeText(
                            this@AddStoryActivity,
                            response.message(),
                            Toast.LENGTH_SHORT
                        ).show()
                        progressDialog.dismiss()
                    }
                }

                override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                    Toast.makeText(
                        this@AddStoryActivity,
                        "Gagal instance Retrofit",
                        Toast.LENGTH_SHORT
                    ).show()
                    progressDialog.dismiss()

                }
            })
        } else {
            Toast.makeText(
                this@AddStoryActivity,
                "Silakan masukkan berkas gambar terlebih dahulu.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, AddStoryActivity::class.java)
        }
    }

}