package com.elanciers.vasantham_stores_ecomm

import android.app.Activity
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import android.view.View
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands
import kotlinx.android.synthetic.main.activity_payment__reciept.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class Payment_Reciept : AppCompatActivity() {
    var names=""
    var mobile=""
    var cards=""
    var amout=""
    var rec=""
    var due=""
    val RequestPermissionCode = 7
    private var mNotifyManager: NotificationManager? = null
    private var mBuilder: NotificationCompat.Builder? = null
    var id = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment__reciept)
        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        val intent=intent.extras
        names=intent!!.getString("name").toString()
        mobile=intent!!.getString("mobile").toString()
        cards=intent!!.getString("card").toString()
        amout=intent!!.getString("amout").toString()
        rec=intent!!.getString("rec").toString()
        due=intent!!.getString("due").toString()
        val times=intent!!.getString("date").toString()

        paydt.setText(times)
        time.setText(due)
        name.setText(names)
        card.setText(cards)
        ordid.setText(rec)
        textView8.setText(Appconstands.rupees + amout)

        if(CheckingPermissionIsEnabledOrNot(this)){

        }
        else{
            RequestMultiplePermission(this)
        }

        button2.setOnClickListener {

            if(button2.text=="DOWNLOAD RECEIPT") {
                mNotifyManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                mBuilder = NotificationCompat.Builder(this)
                mBuilder!!.setContentTitle("File Download")
                    .setContentText("Download in progress")
                    .setSmallIcon(R.drawable.logo)
                // Start a the operation in a background thread
                // Start a the operation in a background thread
                Thread {
                    val scrl: ScrollView = findViewById(R.id.scr)
                    val bitmap = takeScreenShot(
                        scrl,
                        scrl.getChildAt(0).getHeight(), scrl.getChildAt(0).width
                    )
                    val random = Random()
                    val values = random.nextInt(10000)
                    val filename = "Receipt_$names$due" + ".png"

                    val dirPath =
                        Environment.getExternalStorageDirectory().absolutePath + "/Vasantham_Stores"
                    val dir = File(dirPath)
                    if (!dir.exists()) dir.mkdirs()
                    val file = File(dirPath, filename)
                    try {
                        val fOut = FileOutputStream(file)
                        bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
                        fOut.flush()
                        fOut.close()
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                    // When the loop is finished, updates the notification
                    mBuilder!!.setContentText("Download completed") // Removes the progress bar
                        .setProgress(0, 0, false)
                    mNotifyManager!!.notify(id, mBuilder!!.build())

                    runOnUiThread{
                        button2.setText("Open")

                    }


                }.start()
            }
            else{
                val filename = "Receipt_$names$due" + ".png"

                val dirPath =
                    Environment.getExternalStorageDirectory().absolutePath + "/Vasantham_Stores"
                val dir = File(dirPath)
                if (!dir.exists()) dir.mkdirs()
                val file = File(dirPath, filename)
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                val uri = Uri.parse("file://" + file.absolutePath)
                intent.setDataAndType(uri, "image/*")
                startActivity(intent)
            }
        }

        imageButton.setOnClickListener {
            finish()
        }

        share.setOnClickListener {
            try {
                val scrl: ScrollView =findViewById(R.id.scr)
                val bitmap = takeScreenShot(
                    scrl,
                    scrl.getChildAt(0).height,
                    scrl.getChildAt(0).width
                )
                // Log.e("timenull",feedextra);

                //if(!time.equals("null")) {
                Log.e("bitmap", bitmap.height.toString())
                saveBitmap(bitmap)
                //}
                // else{
                //  saveBitmap(bitmap, feedextra.toString(), download, textView88);

                ///}
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("filerr", e.toString())
            }
        }
    }
    private fun takeScreenShot(view: View, height: Int, width: Int): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) bgDrawable.draw(canvas)
        else canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return returnedBitmap
        //val screenView: View = rootView.getRootView()

        //  return bitmap
    }

    fun CheckingPermissionIsEnabledOrNot(context: Activity):Boolean {
        val ACCESS_NETWORK_STATEt = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val ACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return ACCESS_NETWORK_STATEt == PackageManager.PERMISSION_GRANTED &&
                ACCESS_COARSE_LOCATION == PackageManager.PERMISSION_GRANTED
    }


    private fun RequestMultiplePermission(context: Activity) {
        ActivityCompat.requestPermissions(
            context, arrayOf<String>(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), RequestPermissionCode
        )

    }


    fun saveBitmap(bitmap: Bitmap) {
        //   Log.e("timenull",time);
        //   String times=time.replaceAll("-","_");
        //  String times1=times.replaceAll(":","_");
        //  String time2=times1.trim();
        val random = Random()
        val values = random.nextInt(10000)
        val filename = "Receipt_$names$due"+".png"

        val dirPath = Environment.getExternalStorageDirectory().absolutePath + "/Vasantham_Stores"
        val dir = File(dirPath)
        if (!dir.exists()) dir.mkdirs()
        val file = File(dirPath, filename)
        try {
            val fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.flush()
            fOut.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        //  Log.e("timenxx",time2);
        /*  val directory = File(Environment.getExternalStorageDirectory().toString() + File.separator + "MPOS/mos images")
          if (!directory.exists()) {
              directory.mkdirs()
          }
          val imagePath = File(Environment.getExternalStorageDirectory().absoluteFile.toString()
                  + "/MPOS/mpos images/" + filename + ".png")
          var fos: FileOutputStream*/
        try {
            //  fos = FileOutputStream(imagePath)
            //bitmap.compress(Bitmap.CompressFormat.PNG, 50, fos)
            //fos.flush()
            //fos.close()
            val imgUri = Uri.parse("file://" + file.absolutePath)
            println("imguri" + imgUri.toString())
            val whatsappIntent = Intent(Intent.ACTION_SEND)
            whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri)
            whatsappIntent.type = "image/jpeg"
            whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            try {
                this.startActivity(whatsappIntent)
            } catch (ex: ActivityNotFoundException) {
                //Toast.makeText(this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT)
            }
            // Uri uri = Uri.fromFile(new File(file.getAbsolutePath()));
            //performCrop(uri);
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("err", e.toString())
        }
        try {
            /* fos = FileOutputStream(imagePath)
             bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
             Log.e("Screenshot", "saved successfully")
             fos.flush()
             fos.close()*/

            // button.setText("DOWNLOADED");
            // tt.setVisibility(View.VISIBLE);
            // tt.setText("Downloaded Path : "+"storage/MPOS/mpos images/"+time2+utils.loadName()+".png");
            //save();
            /*   button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(button.getText().toString().equals("Open")){
                        openScreenshot(imagePath);
                    }
                }
            });
*/
        } catch (e: FileNotFoundException) {
            Log.e("GREC", e.message, e)
        } catch (e: IOException) {
            Log.e("GREC", e.message, e)
        }
    }

}