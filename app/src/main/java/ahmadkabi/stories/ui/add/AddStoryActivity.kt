package ahmadkabi.stories.ui.add

import ahmadkabi.stories.R
import ahmadkabi.stories.databinding.ActivityAddStoryBinding
import ahmadkabi.stories.helper.DialogUtils
import ahmadkabi.stories.helper.createCustomTempFile
import ahmadkabi.stories.helper.extraIsSuccess
import ahmadkabi.stories.helper.gone
import ahmadkabi.stories.helper.reduceFileImage
import ahmadkabi.stories.helper.showToast
import ahmadkabi.stories.helper.uriToFile
import ahmadkabi.stories.helper.visible
import ahmadkabi.stories.ui.home.story.ViewModelFactory
import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import ahmadkabi.stories.core.data.source.remote.StatusResponse
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private val viewModel: AddStoryViewModel by lazy { ViewModelFactory(this).create(AddStoryViewModel::class.java) }

    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    private val progressDialog: Dialog by lazy { DialogUtils.setProgressDialog(this) }

    private val requiredPermission = arrayOf(Manifest.permission.CAMERA)
    private val requestCodePermission = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_story)

        observe()

        binding.imgBack.setOnClickListener { finish() }
        binding.btnCamera.setOnClickListener { askPermissionOrCapture() }
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

    private fun askPermissionOrCapture() {
        if (allPermissionsGranted()) {
            capturePhoto()
        } else {
            ActivityCompat.requestPermissions(
                this,
                requiredPermission,
                requestCodePermission
            )
        }
    }

    private fun capturePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "ahmadkabi.stories",
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

            with(binding) {
                imgStory.setImageBitmap(result)
                imgCancel.visible()
                btnCamera.gone()
                btnGallery.gone()
                buttonAdd.visible()
            }

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
            if (allPermissionsGranted()) {
                capturePhoto()
            } else {
                showToast(getString(R.string.please_grant_camera))
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

        val description = binding.edAddDescription.text.toString()
            .toRequestBody("text/plain".toMediaType())

        viewModel.addStory(imageMultipart, description)

    }

    private fun observe() {
        viewModel.addStory.observe(this) { result ->
            when (result.status) {
                StatusResponse.SUCCESS -> {
                    if (result.body?.isSuccess == true) {
                        showToast(getString(R.string.new_story_has_been_made))

                        val resultIntent = Intent()
                        resultIntent.putExtra(extraIsSuccess, true)
                        setResult(RESULT_OK, resultIntent)
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

// todo : break down repository