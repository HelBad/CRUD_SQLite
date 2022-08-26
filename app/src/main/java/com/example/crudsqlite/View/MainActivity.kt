package com.example.crudsqlite.View

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crudsqlite.Adapter.OnItemClickListener
import com.example.crudsqlite.Adapter.KaryawanListAdapter
import com.example.crudsqlite.Database.DatabaseQueryClass
import com.example.crudsqlite.Model.Karyawan
import com.example.crudsqlite.R
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_add_person.*
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity(), OnItemClickListener {
    lateinit var dialog: Dialog
    val REQUEST_CODE_GALLERY = 999

    private val mAdapter by lazy { KaryawanListAdapter(this, this) }
    private val databaseQueryClass = DatabaseQueryClass(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dialog = Dialog(this)

        setSupportActionBar(toolbar)
        rvShowList.layoutManager = LinearLayoutManager(this)
        rvShowList.setHasFixedSize(true)
        rvShowList.adapter = mAdapter

        getData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> {
                openDialog()
            }
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openDialog() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_add_person)
        dialog.show()

        dialog.imgView.setOnClickListener(View.OnClickListener {
            ActivityCompat.requestPermissions(
                this@MainActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_GALLERY
            )
        })

        dialog.btnInsertData.setOnClickListener {
            val namaString = dialog.namaAdd.text.toString()
            val alamatString = dialog.alamatAdd.text.toString()
            val telpString = dialog.telpAdd.text.toString()
            val imageString = imageViewToByte(dialog.imgView)

            if (checkValidation(namaString, alamatString, telpString)) {
                val karyawan = Karyawan(-1, namaString, alamatString, telpString, imageString)
                val databaseQueryClass = DatabaseQueryClass(this)
                val id = databaseQueryClass.insertKaryawan(karyawan)
                if (id > 0) {
                    karyawan.id = id
                    getData()
                    dialog.dismiss()
                }
            }
        }
        dialog.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun checkValidation(namaString: String, alamatString: String, telpString: String): Boolean {
        return when {
            namaString.isEmpty() && alamatString.isEmpty() && telpString.isEmpty() -> {
                Toast.makeText(this, "Data masih kosong", Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                true
            }
        }
    }

    private fun getData() {
        mAdapter.setList(databaseQueryClass.allKaryawanLists)
    }

    override fun onEditClicked(pos: Int, karyawan: Karyawan) {
        onUpdateData(pos, karyawan.id)
    }

    private fun onUpdateData(pos: Int, registrationNumber: Long) {
        val mKaryawan = databaseQueryClass.getKaryawanById(registrationNumber)
        if (mKaryawan != null) {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.dialog_add_person)
            dialog.btnInsertData.text = "Update Data"
            dialog.namaAdd.setText(mKaryawan.nama)
            dialog.alamatAdd.setText(mKaryawan.alamat)
            dialog.telpAdd.setText(mKaryawan.telp)
            dialog.show()

            dialog.btnInsertData.setOnClickListener {
                val namaString = dialog.namaAdd.text.toString()
                val alamatString = dialog.alamatAdd.text.toString()
                val telpString = dialog.telpAdd.text.toString()
                val imageString = imageViewToByte(dialog.imgView)

                if (checkValidation(namaString, alamatString, telpString)) {
                    mKaryawan.nama = namaString
                    mKaryawan.alamat = alamatString
                    mKaryawan.telp = telpString
                    mKaryawan.image = imageString

                    val id = databaseQueryClass.updateKaryawan(mKaryawan)
                    if (id > 0) {
                        mAdapter.update(pos, mKaryawan)
                        dialog.dismiss()

                    }
                }
            }
            dialog.btnCancel.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    override fun onDeleteClicked(adapterPosition: Int, karyawanBean: Karyawan) {
        val count = databaseQueryClass.deleteKaryawanById(karyawanBean.id)
        if (count > 0) {
            mAdapter.removeAt(adapterPosition)
            Toast.makeText(this, "Berhasil Dihapus", Toast.LENGTH_SHORT).show()
        } else
            Toast.makeText(this, "Gagal DIhapus", Toast.LENGTH_SHORT).show()
    }

    fun imageViewToByte(image: ImageView): ByteArray? {
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //gallery intent
                val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
                galleryIntent.type = "image/*"
                startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY)
            } else {
                Toast.makeText(
                    this,
                    "Don't have permission to access file location",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            val imageUri = data!!.data
            CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON) //enable image guidlines
                .setAspectRatio(1, 1) // image will be square
                .start(this)
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result: CropImage.ActivityResult = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri: Uri = result.uri
                dialog.imgView.setImageURI(resultUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error: Exception = result.error
            }
        }
    }
}
