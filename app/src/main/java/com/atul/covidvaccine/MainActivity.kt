package com.atul.covidvaccine

import android.app.DatePickerDialog
import android.app.VoiceInteractor
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var searchButton : Button
    lateinit var pinCodeEdt : EditText
    lateinit var centerRV : RecyclerView
    lateinit var loadingPB : ProgressBar
    lateinit var centerList : List<CenterRVModel>
    lateinit var centerRVAdapter : CenterRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchButton = findViewById(R.id.idSearchBtn)
        pinCodeEdt = findViewById(R.id.idEditPinCode)
        centerRV = findViewById(R.id.idRecyclerView)
        loadingPB = findViewById(R.id.idPBLoading)
        centerList = ArrayList<CenterRVModel>()

        searchButton.setOnClickListener {
            val pincode = pinCodeEdt.text.toString()

            if(pincode.length != 6){
                Toast.makeText(this,"Please enter a valid pin code",Toast.LENGTH_SHORT).show()
            }else{
                (centerList as ArrayList<CenterRVModel>).clear()

                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    loadingPB.visibility = View.VISIBLE
                    val dateStr: String = """$dayOfMonth-${month+1}-$year"""

                    getAppointmentDetails(pincode,dateStr)

                },year,month,day)
                dpd.show()
            }
        }
    }

    private fun getAppointmentDetails(pinCode:String, date:String){
        val url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=" + pinCode + "&date=" + date

        val request = JsonObjectRequest(Request.Method.GET,url,null,
            Response.Listener { response ->
                loadingPB.visibility = View.GONE
                try {
                    val centersArray = response.getJSONArray("centers")
                    if(centersArray.length().equals(0)){
                        Toast.makeText(this,"No Vaccination Center Available!",Toast.LENGTH_SHORT).show()
                    }
                    for(i in 0 until centersArray.length()){
                        val centerObj = centersArray.getJSONObject(i)
                        val centerName = centerObj.getString("name")
                        val centerAddress = centerObj.getString("address")
                        val centerFromTime = centerObj.getString("from")
                        val centerToTime = centerObj.getString("to")
                        val fee_type = centerObj.getString("fee_type")

                        val sessionsObj = centerObj.getJSONArray("sessions").getJSONObject(0)
                        val vaccineName = sessionsObj.getString("vaccine")
                        val age_limit = sessionsObj.getInt("min_age_limit")
                        val available_capacity = sessionsObj.getInt("available_capacity")

                        val center = CenterRVModel(centerName,centerAddress,centerFromTime,centerToTime,fee_type,age_limit,vaccineName,available_capacity)
                        (centerList as ArrayList<CenterRVModel>).add(center)

                    }
                    centerRV.layoutManager = LinearLayoutManager(this)
                    centerRVAdapter = CenterRVAdapter(centerList)
                    centerRV.adapter = centerRVAdapter
                }catch (e: JSONException){
                    e.printStackTrace()
                }
            },
            Response.ErrorListener {error ->
                Toast.makeText(this,"Failed to get the data!",Toast.LENGTH_SHORT).show()
            }
            )

        MySingleton.getInstance(this).addToRequestQueue(request)

    }
}