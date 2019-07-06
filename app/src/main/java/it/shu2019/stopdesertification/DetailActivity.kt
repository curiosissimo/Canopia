package it.shu2019.stopdesertification

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_item_detail.*
import android.R.attr.path
import android.net.Uri
import java.io.File
import android.provider.MediaStore
import com.google.android.material.appbar.CollapsingToolbarLayout


/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [ItemListActivity].
 */
class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        setSupportActionBar(detail_toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Inserito tra i preferiti", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        var toolbar: CollapsingToolbarLayout = findViewById(R.id.toolbar_layout)
        var appbar: AppBarLayout = findViewById(R.id.app_bar)
        toolbar.title = intent.extras.get("title").toString();
        val f = File(getRealPathFromURI(Uri.parse(intent.extras.get("imageUri").toString())))
        appbar.background = Drawable.createFromPath(f.absolutePath)

        val descriptionDetail = intent.extras.get("description").toString()
        description.text = descriptionDetail

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_layout.setOnClickListener(this::clickToolbar)
    }

    fun clickToolbar(view: View){
        val intent = Intent(this,ImageActivity::class.java)
        startActivity(intent)
    }
    private fun getRealPathFromURI(contentURI: Uri): String {
        val cursor = contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath()
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            return cursor.getString(idx)
        }
    }
}
