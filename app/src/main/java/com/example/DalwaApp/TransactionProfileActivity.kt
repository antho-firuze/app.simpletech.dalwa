package com.example.DalwaApp

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_transaction_profile.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class TransactionProfileActivity : AppCompatActivity() {

    private val GALLERY = 1
    private val CAMERA = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_profile)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        getIncomingIntent()

        btn_upload.setOnClickListener { showPictureDialog() }
        btn_cancel.setOnClickListener {
            Toast.makeText(this, "Cancel", Toast.LENGTH_LONG).show()
            finish()
//            val builder =  AlertDialog.Builder(this)
//            builder.setTitle("Info")
//            builder.setMessage("Batal Redeem ?")
//            builder.setPositiveButton("YES"){ dialog, which ->
//                // Do something when user press the positive button
//                finish()
//            }
//            builder.setNeutralButton("Cancel"){ dialog, which -> }
//            builder.create().show()
        }
        btn_submit.setOnClickListener {
            Toast.makeText(this, "Submit", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(intent, CAMERA)
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, CAMERA)
            }
        }
    }

    fun getIncomingIntent() {
        val name = intent.getStringExtra("name")
        val status = intent.getStringExtra("status")
        val date = intent.getStringExtra("date")
        val type = intent.getStringExtra("type")
        val curr = intent.getStringExtra("curr")
        val value_amount = intent.getStringExtra("value_amount")
        val cost = intent.getStringExtra("cost")
        val navperunit = intent.getStringExtra("navperunit")
        val total = intent.getStringExtra("total")
        val unit = intent.getStringExtra("unit")
        val metode_pemb = intent.getStringExtra("metode_pemb")
//        if (intent.hasExtra("PortfolioID")) {
//            val PortfolioID = intent.getIntExtra("PortfolioID", 0)
//        }
        txt_name.text = name
//        txt_asset_type.text = "Pasar Uang"
//        txt_risk.text = "Konservatif"
        txt_date.text = date
        txt_trans_type.text = type
        txt_curr.text = "(${curr})"
        txt_status.text = status
        txt_amount.setText(value_amount)
        txt_charge_amount.setText(cost)
        txt_total_amount.setText(total)
        txt_navperunit.setText(navperunit)
        txt_unit.setText(unit)
        txt_payment_type.setText(metode_pemb)
    }

    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                val contentURI = data!!.data
                try
                {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    val path = saveImage(bitmap)
                    Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show()
                    imageview!!.setImageBitmap(bitmap)

                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        }
        else if (requestCode == CAMERA)
        {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            imageview!!.setImageBitmap(thumbnail)
            saveImage(thumbnail)
            Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveImage(myBitmap: Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        Log.d("fee",wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists())
        {

            wallpaperDirectory.mkdirs()
        }

        try
        {
            Log.d("heel",wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                .getTimeInMillis()).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this,
                arrayOf(f.getPath()),
                arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

            return f.getAbsolutePath()
        }
        catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }

    companion object {
        private val IMAGE_DIRECTORY = "/demonuts"
    }
}
