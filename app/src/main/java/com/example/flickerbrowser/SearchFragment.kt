package com.example.flickerbrowser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class SearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    lateinit var searchView: SearchView
    lateinit var searchPromptTextView: TextView
    lateinit var numberOfImagesTextView: TextView
    lateinit var loadImageProgressBar: ProgressBar

    private val activationKey = "805545f2fa8dcc6e2fec2c7110f83895"

    var imagesList = arrayListOf<FlickerImage>()
    var numberOfImages = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_search, container, false)

        searchPromptTextView = fragmentView.findViewById(R.id.tv_searchPrompt)
        numberOfImagesTextView = fragmentView.findViewById(R.id.tv_numberOfImages)
        searchView = fragmentView.findViewById(R.id.sv_search)
        recyclerView = fragmentView.findViewById(R.id.rv_search)
        loadImageProgressBar = fragmentView.findViewById(R.id.progress_searchLoadImages)

        numberOfImagesTextView.text = numberOfImages.toString()
        numberOfImagesTextView.setOnClickListener {
            numberOfImages = when(numberOfImages){
                5 -> 10
                10 -> 20
                20 -> 5
                else -> 10
            }
            numberOfImagesTextView.text = numberOfImages.toString()
        }

        searchPromptTextView.isVisible = imagesList.isEmpty()
        loadImageProgressBar.isVisible = false


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                if(query.isNotEmpty()) {
                    searchPromptTextView.isVisible = false
                    search(query)
                    imagesList.clear()
                }
                return false
            }

        })
        //initialize recyclerView
        recyclerView = fragmentView.findViewById(R.id.rv_search)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = ImagesAdapter(imagesList, context)

        return fragmentView
    }

    private fun search(query: String) {
        CoroutineScope(IO).launch {
            val data = async {
                requestData(query)
            }.await()
            if (data.isNotEmpty()) {
                println(data)
                showPhotos(data)
            } else {
                searchPromptTextView.isVisible = true
                searchPromptTextView.text = "No Results :(!"
            }
        }
    }

    private suspend fun requestData(query: String): String {
        var data = ""
        try {
            val okHttpClient = OkHttpClient()
            val request = Request.Builder()
                .url("https://www.flickr.com/services/rest/?method=flickr.photos.search&api_key=$activationKey&tags=$query&format=json&nojsoncallback=1")
                .build()
            val response =
                withContext(Dispatchers.Default) {
                    okHttpClient.newCall(request).execute()
                }
            if (response != null) {
                if (response.code == 200) {
                    data = JSONObject(response.body!!.string()).toString()
                }
            }

        } catch (e: Exception) {
            println("ISSUE: $e")
        }
        return data
    }

    private suspend fun showPhotos(data: String) {
        withContext(Main) {
            val jsonObject = JSONObject(data)
            val photosArray = jsonObject.getJSONObject("photos").getJSONArray("photo")
            var length = if (photosArray.length() < numberOfImages) {
                photosArray.length()
            } else {
                numberOfImages
            }
            for (index in 0 until length) {
                val server = photosArray.getJSONObject(index).getString("server")
                val id = photosArray.getJSONObject(index).getString("id")
                val secret = photosArray.getJSONObject(index).getString("secret")
                val url =
                    "https://live.staticflickr.com/${server}/${id}_${secret}_w.jpg"
                val image = FlickerImage(id, url, false)
                if (!imagesList.contains(image)) {
                    imagesList.add(image)
                    recyclerView.adapter!!.notifyItemInserted(imagesList.size - 1)
                }
            }
        }
    }
}