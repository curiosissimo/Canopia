package it.shu2019.stopdesertification

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.os.Build.*
import android.R.attr.bitmap
import android.content.ContentValues
import android.content.Context
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import android.widget.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_create.*
import java.io.ByteArrayOutputStream


class CreateActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var fileUri: Uri? = null
    var category = 0

    companion object {
        //image pick code
        private val IMAGE_CAPTURE_CODE = 1000;
        private val IMAGE_PICK_CODE = 1001;
        //Permission code
        private val PERMISSION_EXT_STORAGE_CODE = 1010;
        private val PERMISSION_WRITE_CODE = 1011;
    }

    fun createSpinner(){

        ArrayAdapter.createFromResource(
            this,
            R.array.categories_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categories.adapter = adapter
        }

        categories.onItemSelectedListener = this
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        val string = parent?.getItemAtPosition(position).toString()
        when(string){
            "Solutions"-> category=0
            "Problems"-> category=1
            "Organization"-> category=2
            "Users alerts"-> category=3
            "Fire prevention"-> category=4
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        createSpinner()

        val imageView: ImageView = findViewById(R.id.taken_picture)
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_enhance_black_24dp))

        val saveMarkerBtn: Button = findViewById(R.id.save_marker)
        saveMarkerBtn.setOnClickListener {
            val titleTxt: EditText = findViewById(R.id.markerTitle)
            val descTxt: EditText = findViewById(R.id.markerDescription)
            val output = Intent()
            output.putExtra("title", titleTxt.text)
            output.putExtra("description", descTxt.text)
            output.putExtra("imageUri", fileUri)
            output.putExtra("category", category)
            setResult(1, output)
            finish()
        }

        val takePictureBtn: Button = findViewById(R.id.take_picture)
        takePictureBtn.setOnClickListener {
            if (VERSION.SDK_INT >= VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_WRITE_CODE);
                } else {
                    //permission already granted
                    takePicture();
                }
            } else {
                //system OS is < Marshmallow
                takePicture();
            }
        }

        val openGalleryBtn: Button = findViewById(R.id.open_gallery)
        openGalleryBtn.setOnClickListener {
            if (VERSION.SDK_INT >= VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_EXT_STORAGE_CODE);
                } else {
                    //permission already granted
                    pickImageFromGallery();
                }
            } else {
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }
    }

    fun takePicture() {
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            takePictureIntent.resolveActivity(packageManager)?.also {
//                startActivityForResult(takePictureIntent, IMAGE_CAPTURE_CODE)
//            }
//        }
        val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        fileUri = contentResolver
            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(packageManager) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, IMAGE_CAPTURE_CODE)
        }
    }

    fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_EXT_STORAGE_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            PERMISSION_WRITE_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    takePicture()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == RESULT_OK) {
            val imageView: ImageView = findViewById(R.id.taken_picture)
//            val bitmap = data?.extras?.get("data") as Bitmap

            val ei = ExifInterface(getRealPathFromURI(fileUri))
            val orientation = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )

            var orientationValue: Float = 0f;

            when (orientation) {

                ExifInterface.ORIENTATION_ROTATE_90 -> orientationValue = 90f

                ExifInterface.ORIENTATION_ROTATE_180 -> orientationValue = 180f

                ExifInterface.ORIENTATION_ROTATE_270 -> orientationValue = 270f
            }
//            imageView.setImageURI(fileUri)
//            imageView.setImageBitmap(rotatedBitmap)
            Picasso.get().load(fileUri).rotate(orientationValue).into(imageView)

        } else if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {
            val imageView: ImageView = findViewById(R.id.taken_picture)
            imageView.setImageURI(data?.data)
            fileUri = data?.data;
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
        return Uri.parse(path)
    }

    fun getRealPathFromURI(uri: Uri?): String {
        var path = ""
        if (contentResolver != null) {
            val cursor = contentResolver.query(uri, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }

}
