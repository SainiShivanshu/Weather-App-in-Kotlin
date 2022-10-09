package com.example.weatherkt


import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.weatherkt.MainActivity
import com.example.weatherkt.databinding.ActivitySplashBinding
import com.google.android.gms.location.*


class SplashActivity : AppCompatActivity() {
    var imageView: ImageView? = null
    var charSequence: CharSequence? = null
    var index = 0
    var delay: Long = 200
    var handler = Handler()
    var textView: TextView? = null
    var binding:ActivitySplashBinding?=null
    var myRequestCode=1010;
 lateinit var mfusedlocation:FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        supportActionBar?.hide()

        mfusedlocation = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        YoYo.with(Techniques.ZoomIn).duration(500).repeat(0).playOn(binding!!.imageView)


    }
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
      if(chkPermission()){
          if(LocationEnable()){

              mfusedlocation.lastLocation.addOnCompleteListener{

                task->
                  var location : Location? = task.result
                  if(location==null){
                      NewLocation()
                  }
                  else{
                      Handler(Looper.myLooper()!!).postDelayed({

               val intent= Intent(
                    this@SplashActivity,
                    MainActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("lat",location.latitude.toString())
                          intent.putExtra("long",location.longitude.toString())

            startActivity(intent)
            finish()
        }, 500)
                  }
              }
          }
          else{
              Toast.makeText(this,"please Turn on your GPS location",Toast.LENGTH_LONG).show()

          }
      }
        else{
           RequestPermission()
      }
    }

    @SuppressLint("MissingPermission")
    private fun NewLocation() {
        var locationRequest=LocationRequest()
        locationRequest.priority=LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval=0
        locationRequest.fastestInterval=0
        locationRequest.numUpdates=1
        mfusedlocation=LocationServices.getFusedLocationProviderClient(this)
        mfusedlocation.requestLocationUpdates(locationRequest,locationCallback,Looper.myLooper())
    }

    private val locationCallback= object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
         var lastLocation: Location? =p0.lastLocation
        }
    }
    
    private fun LocationEnable(): Boolean {
     var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun RequestPermission() {
     ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION ),myRequestCode)
    }

    private fun chkPermission(): Boolean {
        if(
            ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED||
            ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED

        ){
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==myRequestCode){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                 getLastLocation()
            }
        }
    }
}


