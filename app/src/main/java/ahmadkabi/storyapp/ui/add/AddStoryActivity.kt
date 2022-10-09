package ahmadkabi.storyapp.ui.add

import ahmadkabi.storyapp.R
import ahmadkabi.storyapp.data.source.remote.StatusResponse
import ahmadkabi.storyapp.data.source.remote.model.AddStoryBody
import ahmadkabi.storyapp.databinding.ActivityAddStoryBinding
import ahmadkabi.storyapp.helper.*
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
import androidx.lifecycle.ViewModelProvider
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var viewModel: AddStoryViewModel

    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    private val progressDialog: Dialog by lazy { DialogUtils.setProgressDialog(this) }

    private val requiredPermission = arrayOf(Manifest.permission.CAMERA)
    private val requestCodePermission = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_story)
        viewModel = ViewModelProvider(this)[AddStoryViewModel::class.java].apply {
            token = UserPreference(this@AddStoryActivity).getToken()!!
        }

        observe()

        binding.imgBack.setOnClickListener { onBackPressed() }
        binding.btnCamera.setOnClickListener { startTakePhoto() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.buttonAdd.setOnClickListener { addStory() }
        binding.imgCancel.setOnClickListener {
            getFile = null
            binding.imgStory.setImageBitmap(null)
            binding.imgCancel.gone()

            binding.btnCamera.visible()
            binding.btnGallery.visible()
            binding.buttonAdd.gone()
        }


        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                requiredPermission,
                requestCodePermission
            )
        }


    }


    private fun allPermissionsGranted() = requiredPermission.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_a_picture))
        launcherIntentGallery.launch(chooser)
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        intent.resolveActivity(packageManager)

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
            binding.buttonAdd.visible()
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
            binding.buttonAdd.visible()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodePermission) {
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

    private fun addStory() {
        progressDialog.show()

        val file = reduceFileImage(getFile as File)
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        )

        val description = binding.edAddDescription.text .toString()
            .toRequestBody("text/plain".toMediaType())

        viewModel.body.value = AddStoryBody(imageMultipart, description)
    }

    private fun observe() {
        viewModel.addStory.observe(this) { result ->
            when (result.status) {
                StatusResponse.SUCCESS -> {
                    if (result.body != null) {
                        showToast(getString(R.string.new_story_has_been_made))
                        finish()

                    } else {
                        showToast(getString(R.string.failed_to_add_new_story))
                    }
                }
                StatusResponse.ERROR -> {
                    showToast(getString(R.string.sorry_something_went_wrong))
                }
                StatusResponse.EMPTY -> {}
            }

            progressDialog.dismiss()
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, AddStoryActivity::class.java)
        }
    }

}