package com.test.testmup

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), LocationListener {

    private val api = Api.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_check.setOnClickListener {
            sendGeo()
        }
    }

    private fun sendGeo() {

        val location = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val permissionCode = 1
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), permissionCode)
            return
        }

        location.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, Looper.getMainLooper())
    }

    override fun onLocationChanged(location: Location?) {
        val geo = LatLong(location?.latitude.toString(), location?.longitude.toString())
        text_lat.text = geo.lat
        text_long.text = geo.long
        Log.i("geo", geo.lat + ", " + geo.long)
        api.writeToDb(geo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({result ->
                Log.i("geo", result.code().toString())
            }, {err ->
                Log.e("geo", err.localizedMessage)
            })
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {

    }
}
