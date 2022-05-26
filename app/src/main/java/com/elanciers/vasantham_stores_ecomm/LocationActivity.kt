package com.elanciers.vasantham_stores_ecomm

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_order_detail.*
import com.google.android.gms.maps.model.LatLng

import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.FusedLocationProviderClient

import com.google.android.gms.maps.model.PolylineOptions

import org.json.JSONObject

import android.os.AsyncTask
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.elanciers.vasantham_stores_ecomm.Common.Appconstands
import com.elanciers.vasantham_stores_ecomm.Common.Connection
import com.elanciers.vasantham_stores_ecomm.Common.Utils
import com.elanciers.vasantham_stores_ecomm.DataClass.LocationObject
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_order_detail.track
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL





class LocationActivity : AppCompatActivity(),OnMapReadyCallback {
    private val TAG = "LocationActivity"
    var lstPlaces: ListView? = null
    //private val mPlacesClient: PlacesClient? = null
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var mLastKnownLocation: Location? = null

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private val mDefaultLocation = LatLng(-33.8523341, 151.2106085)
    private val DEFAULT_ZOOM = 15
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private var mLocationPermissionGranted = false

    // Used for selecting the Current Place.
    private val M_MAX_ENTRIES = 5
    lateinit var mLikelyPlaceNames: Array<String>
    lateinit var mLikelyPlaceAddresses: Array<String>
    lateinit var mLikelyPlaceAttributions: Array<String>
    lateinit var mLikelyPlaceLatLngs: Array<LatLng>
    var mMap: GoogleMap? = null

    //var locationTrack: LocationTrack? = null
    var longitude : Double? = null
    var latitude : Double? = null

    var custlongitude : Double = 78.121719
    var custlatitude : Double = 9.939093

    lateinit var locationManager : LocationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        val mToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(mToolbar);
        val ab = supportActionBar
        ab!!.title = "Tracking"
        ab!!.setDisplayShowHomeEnabled(true)
        ab!!.setDisplayHomeAsUpEnabled(true)
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_ios_24px)
        if (intent.hasExtra("lng")) {
            custlongitude = if (intent.hasExtra("lng")) intent.getStringExtra("lng").toString()
                .toDouble() else 0.0
            custlatitude = if (intent.hasExtra("lat")) intent.getStringExtra("lat").toString()
                .toDouble() else 0.0
        }else{
            Deliveriboyloaction().execute()
        }
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(object : OnMapReadyCallback{
            override fun onMapReady(googleMap: GoogleMap?) {
                mMap = googleMap
                markEndingLocationOnMap(null,null)
            }

        })
        //mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //getLocationPermission()

        /*locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);*/

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; activity adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.map, menu)


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.getItemId()
        when(id){
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.refresh -> {
                Deliveriboyloaction().execute()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        mLocationPermissionGranted = false
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            mLocationPermissionGranted = true
            println("mLocationPermissionGranted : "+mLocationPermissionGranted)
            pickCurrentPlace();
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        pickCurrentPlace();
        // Add a marker in Sydney and move the camera
        /*val sydney = LatLng(-34, 151)
        mMap!!.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/

        //
        // PASTE THE LINES BELOW THIS COMMENT
        //

        // Enable the zoom controls for the map

        //
        // PASTE THE LINES BELOW THIS COMMENT
        //

        // Enable the zoom controls for the map
        //mMap!!.uiSettings.isZoomControlsEnabled = true

        // Prompt the user for permission.

        // Prompt the user for permission.
        getLocationPermission()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mLocationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    mLocationPermissionGranted = true
                    pickCurrentPlace();
                }
                println("mLocationPermissionGranted : "+mLocationPermissionGranted)
            }
        }
    }

    private fun pickCurrentPlace() {
        if (mMap == null) {
            return
        }
        if (mLocationPermissionGranted) {
            getDeviceLocation()
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.")

            // Add a default marker, because the user hasn't selected a place.
            /*mMap!!.addMarker(
                MarkerOptions()
                    .title("my location")
                    .position(mDefaultLocation)
                    //.snippet(getString(R.string.default_info_snippet))
            )*/

            // Prompt the user for permission.
            getLocationPermission()
        }
    }

    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                val locationResult = mFusedLocationProviderClient!!.lastLocation
                locationResult.addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = task.result
                        Log.d(TAG, "Latitude: " + mLastKnownLocation!!.latitude)
                        Log.d(TAG, "Longitude: " + mLastKnownLocation!!.longitude)
                        /*mMap!!.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    mLastKnownLocation!!.latitude,
                                    mLastKnownLocation!!.longitude
                                ), DEFAULT_ZOOM.toFloat()
                            )
                        )*/
                        markStartingLocationOnMap(null,null)
                        markEndingLocationOnMap(null,null)

                        /*// Getting URL to the Google Directions API
                        val url = getDirectionsUrl(LatLng(
                            mLastKnownLocation!!.latitude,
                            mLastKnownLocation!!.longitude
                        ), LatLng(
                            custlatitude,
                            custlongitude
                        ))

                        val downloadTask = DownloadTask()
                        // Start downloading json data from Google Directions API
                        downloadTask.execute(url)*/
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        /*mMap!!.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM.toFloat())
                        )*/
                    }
                    //getCurrentPlaceLikelihoods()
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message.toString())
        }
    }
    override fun onResume() {
        super.onResume()
        /*locationTrack = LocationTrack(this)
        if (locationTrack!!.canGetLocation()) {
            longitude = locationTrack!!.longitude
            latitude = locationTrack!!.latitude
            *//*Toast.makeText(applicationContext, """
                 Longitude:${java.lang.Double.toString(longitude)}
                 Latitude:${java.lang.Double.toString(latitude)}
                 """.trimIndent(), Toast.LENGTH_SHORT).show()*//*
            Log.d("location",latitude.toString()+" : "+longitude.toString())
            if (longitude!=null && latitude!=null) {
                if (longitude!=0.0) {
                    //SendLocation()
                }else{
                    onResume()
                }
            }

        } else {
            locationTrack!!.showSettingsAlert()
        }*/

    }

    private fun markStartingLocationOnMap(mapObject: GoogleMap?, location: LatLng?) {
        mMap!!.clear()
        mMap!!.addMarker(
            MarkerOptions().position(LatLng(
                mLastKnownLocation!!.latitude,
                mLastKnownLocation!!.longitude
            )).title("Your here").icon(
            /*bitmapDescriptorFromVector(this,R.drawable.ic_single_motorbike)*/
            BitmapDescriptorFactory.fromResource(R.mipmap.bicycle)
        ))
        //mapObject.moveCamera(CameraUpdateFactory.newLatLng(location))
        val cameraPosition = CameraPosition.Builder()
            .target(location)
            .zoom(16f)
            .build()
        mMap!!.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    mLastKnownLocation!!.latitude,
                    mLastKnownLocation!!.longitude
                ), DEFAULT_ZOOM.toFloat()
            )
        )
        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun markEndingLocationOnMap(mapObject: GoogleMap?, location: LatLng?) {
        mMap!!.clear()
        mMap!!.addMarker(
            MarkerOptions().position(LatLng(
                custlatitude,
                custlongitude
            )).title("Delivery boy is here").icon(
                /*bitmapDescriptorFromVector(this,R.drawable.ic_single_motorbike)*/
                BitmapDescriptorFactory.fromResource(R.mipmap.bicycle)
            ))
        /*if (track.text.toString().toUpperCase()==("Accept").toUpperCase()) {
            Appconstands.forprint("acc : "+location.latitude+" : "+location.longitude)
            mapObject!!.addMarker(
                MarkerOptions().position(location).title(shopnm.text.toString()).icon(
                    *//* bitmapDescriptorFromVector(this,R.drawable.ic_shop)*//*
                    BitmapDescriptorFactory.fromResource(R.mipmap.store)
                )
            )
            //mapObject.moveCamera(CameraUpdateFactory.newLatLng(location))
        }else if (track.text.toString().toUpperCase()==("Set Order Delivered").toUpperCase()) {
            Appconstands.forprint("deli : "+location.latitude+" : "+location.longitude)
            mapObject!!.addMarker(
                MarkerOptions().position(location).title(user_nm).icon(
                    *//*bitmapDescriptorFromVector(this,R.drawable.ic_customer)*//*
                    BitmapDescriptorFactory.fromResource(R.mipmap.placeholder)
                )
            )
            //mapObject.moveCamera(CameraUpdateFactory.newLatLng(location))
        }*/
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(
                custlatitude,
                custlongitude
            ))
            .zoom(16f)
            .build()
        mMap!!.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng( custlatitude,
                    custlongitude
                ), DEFAULT_ZOOM.toFloat()
            )
        )
        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    fun bitmapDescriptorFromVector(context: Context, vectorDrawableResourceId :/* @DrawableRes*/ Int) : BitmapDescriptor {
        val background = ContextCompat.getDrawable(context, vectorDrawableResourceId)
        background!!.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable!!.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        val bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        val canvas = Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private fun getPoints(mLocations: List<LocationObject>?): List<LatLng> {
        val points: MutableList<LatLng> = ArrayList()
        for (mLocation in mLocations!!) {
            points.add(LatLng(mLocation.Latitude!!, mLocation.Longitude!!))
        }
        return points
    }

    private fun startPolyline(map: GoogleMap?, location: LatLng) {
        if (map == null) {
            Appconstands.forlog("loct", "Map object is not null")
            return
        }
        val options = PolylineOptions().width(5f).color(Color.BLUE).geodesic(true)
        options.add(location)
        val polyline = map.addPolyline(options)
        val cameraPosition = CameraPosition.Builder()
            .target(location)
            .zoom(16f)
            .build()
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun drawRouteOnMap(
        map: GoogleMap?,
        positions: List<LatLng>
    ) {
        /*val options = PolylineOptions().width(5f).color(Color.BLUE).geodesic(true)
        options.addAll(positions)
        val polyline = map!!.addPolyline(options)*/
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(positions[0].latitude, positions[0].longitude))
            .zoom(17f)
            .bearing(90f)
            .tilt(40f)
            .build()
        map!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun refreshMap(mapInstance: GoogleMap?) {
        mapInstance!!.clear()
    }


    /*inner class DownloadTask : AsyncTask<String?, String?, String?>() {
        override fun doInBackground(vararg url: String?): String? {
            var data = ""
            try {
                data = downloadUrl(url[0]!!)
            } catch (e: Exception) {
                Log.d("Background Task", e.toString())
            }
            return data
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val parserTask = ParserTask()
            parserTask.execute(result)
        }

        @Throws(IOException::class)
        fun downloadUrl(strUrl: String): String {
            var data = ""
            var iStream: InputStream? = null
            var urlConnection: HttpURLConnection? = null
            try {
                val url = URL(strUrl)
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.connect()
                iStream = urlConnection.getInputStream()
                val br = BufferedReader(InputStreamReader(iStream))
                val sb = StringBuffer()
                var line: String? = ""
                while (br.readLine().also { line = it } != null) {
                    sb.append(line)
                }
                data = sb.toString()
                br.close()
            } catch (e: Exception) {
                Log.d("Exception", e.toString())
            } finally {
                iStream!!.close()
                urlConnection!!.disconnect()
            }
            return data
        }

    }

    inner class ParserTask :
        AsyncTask<String?, Int?, List<List<HashMap<String,String>>>?>() {
        // Parsing the data in non-ui thread
        override fun doInBackground(vararg jsonData: String?): List<List<HashMap<String,String>>>? {
            val jObject: JSONObject
            var routes: List<List<HashMap<String,String>>>? = null
            try {
                jObject = JSONObject(jsonData[0])
                val parser = DirectionsJSONParser()
                routes = parser.parse(jObject)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return routes
        }

        override fun onPostExecute(result: List<List<HashMap<String,String>>>?) {
            var points: ArrayList<LatLng>? = null
            var lineOptions: PolylineOptions? = null
            val markerOptions = MarkerOptions()
            for (i in result!!.indices) {
                points = ArrayList()
                lineOptions = PolylineOptions()
                val path: List<HashMap<String,String>> = result[i]
                for (j in path.indices) {
                    val point: HashMap<String,String> = path[j]
                    val lat: Double = point.get("lat")!!.toDouble()
                    val lng: Double = point.get("lng")!!.toDouble()
                    val position = LatLng(lat, lng)
                    points.add(position)
                }
                lineOptions.addAll(points)
                lineOptions.width(12f)
                lineOptions.color(Color.RED)
                lineOptions.geodesic(true)
            }

// Drawing polyline in the Google Map for the i-th route
            if (lineOptions!=null) {
                mMap!!.addPolyline(lineOptions)
            }
        }
    }

    fun getDirectionsUrl(origin: LatLng, dest: LatLng): String? {

        // Origin of route
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude

        // Destination of route
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude

        // Sensor enabled
        val sensor = "sensor=false"
        val mode = "mode=driving"

        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$sensor&$mode"

        // Output format
        val output = "json"

        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters"
    }*/


    inner class Deliveriboyloaction : AsyncTask<String?, String?, String?>() {
        lateinit var pDialog: Dialog
        override fun onPreExecute() {
            pDialog = Dialog(this@LocationActivity)
            Appconstands.loading_show(this@LocationActivity,pDialog).show()
        }

        protected override fun doInBackground(vararg strings: String?): String? {
            var result: String? = null
            val con = Connection()
            try {
                val jobj = JSONObject()
                jobj.put("userid",Utils(this@LocationActivity).due_id())
                Log.i("rewardinput", Appconstands.paymentlist + "    " + jobj.toString())
                result = con.sendHttpPostjson2(Appconstands.paymentlist, jobj, "")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(resp: String?) {
            Log.e("resp",resp.toString())
            pDialog.dismiss()
            try {
                if (resp != null) {

                    val jobj = JSONArray(resp)
                    if (jobj.getJSONObject(0).getString("Status") == "Success") {
                        val lat=jobj.getJSONObject(0).getString("lat")
                        val lng=jobj.getJSONObject(0).getString("lng")
                        if (lat.isNotEmpty()&&lng.isNotEmpty()){
                            custlatitude = lat.toString().toDouble()
                            custlongitude = lng.toString().toDouble()
                            markEndingLocationOnMap(null,null)
                        }
                    }
                }
            }catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

}