package com.example.weatherkt

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val lat = intent.getStringExtra("lat")
        val long = intent.getStringExtra("long")

        getJsonData(lat,long)


        search.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val city = cityEdt.text.toString()
                if (city.isEmpty()) {
                    Toast.makeText(this@MainActivity, "Please enter city Name", Toast.LENGTH_SHORT)
                        .show()
                } else {
        getWeatherInfo(city)
                }
            }
        })
    }

    private fun getWeatherInfo(city: String) {
        val API_KEY = "d0f056b51d4becc728d4a078dd2d1002"
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.openweathermap.org/data/2.5/weather?q=${city}&appid=${API_KEY}"
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            Response.Listener { response ->
                setValues(response)
            },
            Response.ErrorListener { Toast.makeText(this,"Please restart the app again!",Toast.LENGTH_LONG).show() })

// Add the request to the RequestQueue.
        queue.add(jsonRequest)

    }

    private fun getJsonData(lat: String?, long: String?) {
val API_KEY = "d0f056b51d4becc728d4a078dd2d1002"
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=${API_KEY}"


        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            Response.Listener { response ->
                setValues(response)
            },
            Response.ErrorListener { Toast.makeText(this,"Please restart the app again!",Toast.LENGTH_LONG).show() })

// Add the request to the RequestQueue.
        queue.add(jsonRequest)
    }

    private fun setValues(response: JSONObject?) {
        city.text=response?.getString("name")

        Thread(Runnable {
            this.runOnUiThread(java.lang.Runnable {
                var code = response?.getJSONArray("weather")?.getJSONObject(0)?.getString("icon")
                var url ="https://openweathermap.org/img/wn/${code}@4x.png"
                Glide.with(this@MainActivity).load(url).into(img)
                var lat = response?.getJSONObject("coord")?.getString("lat")
                var long=response?.getJSONObject("coord")?.getString("lon")
                coordinates.text="${lat} , ${long}"
                weather.text=response?.getJSONArray("weather")?.getJSONObject(0)?.getString("main")
                var tempr=response?.getJSONObject("main")?.getString("temp")
                tempr=((((tempr)?.toFloat()!!-273.15)).toInt()).toString()
                temp.text="${tempr}°C"


                var mintemp=response?.getJSONObject("main")?.getString("temp_min")
                mintemp=((((mintemp)?.toFloat()!!-273.15)).toInt()).toString()
                min_temp.text=mintemp+"°C"+" -"
                var maxtemp=response?.getJSONObject("main")?.getString("temp_max")
                maxtemp=((ceil((maxtemp)?.toFloat()!! -273.15)).toInt()).toString()
                max_temp.text=maxtemp+"°C"

                pressure.text=response?.getJSONObject("main")?.getString("pressure")
                humidity.text=response?.getJSONObject("main")?.getString("humidity")+"%"
                wind.text=response?.getJSONObject("wind")?.getString("speed")+" m/s"
                degree.text="Degree : "+response?.getJSONObject("wind")?.getString("deg")+"°"
                gust.text="Gust : "+response?.getJSONObject("wind")?.getString("gust")+"°"
            })
        }).start()






    }





    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu,menu)

        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
             R.id.About-> {

                var intent = Intent(this, AboutUs::class.java)

                startActivity(intent)
            }
             }
        return super.onOptionsItemSelected(item)
    }




}

