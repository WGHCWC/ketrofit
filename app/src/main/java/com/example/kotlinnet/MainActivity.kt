package com.example.kotlinnet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wghcwc.ketrofit.net.api
import java.net.URL

class MainActivity : AppCompatActivity() {
    private val TAG = "TAG"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        api { Apis.test() } down {

        }

        genApi(Apis.test2()).error {

        } down {

        }


        Apis.test2().err {

        }.start()


    }


}