package com.example.flickerbrowser

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.image_card.view.*
import okhttp3.internal.notify

class ImagesAdapter(private val imagesList: ArrayList<FlickerImage>, val context: Context?) :
    RecyclerView.Adapter<ImagesAdapter.UserViewHolder>() {
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.img_flickerImage
        val favoriteImageView: ImageView = itemView.img_favorite
        val progressCircle: ProgressBar = itemView.progress_loadImage
//        val pkTextView: TextView = itemView.tv_pk
//        val userItemLinearLayout : LinearLayout = itemView.ll_userItem

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.image_card,
            parent,
            false
        )
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val imgUrl = imagesList[position].url!!
        var favorite = imagesList[position].favorite!!

        Glide.with(context!!).load(imgUrl).into(holder.imageView)
        if (favorite) {
            holder.favoriteImageView.setImageResource(R.drawable.ic_round_favorite_50)
        } else {
            holder.favoriteImageView.setImageResource(R.drawable.ic_round_favorite_border_50)
        }
        holder.imageView.setOnClickListener {
            val intent = Intent(context, ViewImageActivity::class.java)
            intent.putExtra("imgUrl", imgUrl)
            context.startActivity(intent)
        }
        holder.favoriteImageView.setOnClickListener {
            favorite = !favorite
            imagesList[position].favorite = favorite
            if (favorite) {
                holder.favoriteImageView.setImageResource(R.drawable.ic_round_favorite_50)
            } else {
                holder.favoriteImageView.setImageResource(R.drawable.ic_round_favorite_border_50)
            }

        }


    }


    override fun getItemCount() = imagesList.size
}