package com.example.kpp_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewParent
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {

    //var BaseCurrency = "USD"
    var convertedToCurrency1 = "EUR"
    var convertedToCurrency2 = "GPB"
    var convertedToCurrency3 = "UAH"

    var conversionRate = 0f

    val et_firstConversion = findViewById<EditText>(R.id.et_firstConversion)
    val et_secondConversion = findViewById<EditText>(R.id.et_firstConversion)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerSetup()
        textChanged()
    }

    private fun textChanged(){
        et_firstConversion.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(p0: Editable?) {
                try {
                    getApiRes()
                }catch (e:Exception){
                    Log.d("Main","$e")
                }
            }

        })
    }

    private fun getApiRes(){


        if(et_firstConversion != null && et_firstConversion.text.isNotEmpty() && et_firstConversion.text.isNotBlank()){

            val api = "https://api.currencyfreaks.com/v2.0/rates/latest?apikey=7e384fae44b6422e931fc1f0e2fad391&symbols=$convertedToCurrency1,$convertedToCurrency2,$convertedToCurrency3"
            GlobalScope.launch(Dispatchers.IO) {
                try{
                    val apiRes = URL(api).readText()
                    val jsonObject = JSONObject(apiRes)

                    conversionRate = jsonObject.getJSONObject("rates").getString(convertedToCurrency1).toFloat()

                    Log.d("Main","$conversionRate")
                    Log.d("Main",apiRes)

                    withContext(Dispatchers.Main){
                        val text = (et_firstConversion.text.toString().toFloat() * conversionRate).toString()
                        et_secondConversion?.setText(text)
                    }
                } catch (e:Exception){
                    Log.d("Main","$e")
                }
            }
        }
    }

    private fun spinnerSetup(){
        //val spinner1 = findViewById<Spinner>(R.id.spinner_firstConversion)
        /* ArrayAdapter.createFromResource(
            this,
            R.array.currencies,
            android.R.layout.simple_spinner_item
        ).also{adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner1.adapter = adapter
        } */

        val spinner2 = findViewById<Spinner>(R.id.spinner_secondConversion)
        ArrayAdapter.createFromResource(
            this,
            R.array.currencies2,
            android.R.layout.simple_spinner_item
        ).also{adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner2.adapter = adapter
        }

        spinner2.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                convertedToCurrency1 = parent?.getItemAtPosition(position).toString()
                getApiRes()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        })

    }



}