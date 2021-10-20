package com.example.flickerbrowser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide

class ViewImageActivity : AppCompatActivity() {

    var imgUrl = ""
    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_image)

        //actionbar
        val actionbar = supportActionBar!!
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
        imageView = findViewById(R.id.img_viewImage)
        imgUrl = intent.extras!!.getString("imgUrl").toString()
        Glide.with(this).load(imgUrl).into(imageView)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}