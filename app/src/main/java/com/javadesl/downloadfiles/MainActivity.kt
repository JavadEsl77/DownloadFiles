package com.javadesl.downloadfiles

import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import com.javadesl.downloadfiles.databinding.ActivityMainBinding
import java.net.URL
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    private val STORAGE_PERMISSION_CODE: Int = 500
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDownload.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    //permission denied , request it
                    requestPermissions(
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        STORAGE_PERMISSION_CODE
                    )
                } else {
                    startDownloading()
                    //permission already granted , perform download
                }
            } else {
                startDownloading()
                //system os is less then
            }
        }
    }

    private fun startDownloading() {
        //get url from editText
        val url = binding.edtLink.text.toString()
        //download request
        val request  =  DownloadManager.Request(Uri.parse(url))
        //allow type of network to download file by default both are allowed
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("")
        request.setDescription("")
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"${System.currentTimeMillis()}")

        //get download service , and enqueue file
        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

    //handler permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission from popup was granted , perform download
                    startDownloading()
                } else {
                    //permission from popup was denied . show message
                    Toast.makeText(
                        this,
                        "permission denied!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }

}