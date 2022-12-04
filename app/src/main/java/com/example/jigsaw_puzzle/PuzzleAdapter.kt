package com.example.jigsaw_puzzle

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage


class PuzzleAdapter(private val puzzleList: ArrayList<String>, private val mContext: Context) : RecyclerView.Adapter<PuzzleAdapter.MyViewHolder>(){


    private val TAG = "PuzzleAdapater"
    private val storageRef = FirebaseStorage.getInstance().reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val puzzleItemView = LayoutInflater.from(mContext).inflate(R.layout.grid_puzzle_item, parent, false)


        return MyViewHolder(puzzleItemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var currPuzzle: String = puzzleList[position]

        Log.d(TAG, currPuzzle)

        holder.puzzleImageView.setOnClickListener { view ->
            Toast.makeText(
                mContext,
                "this is number: $position Image Selected",
                Toast.LENGTH_SHORT
            ).show()
        }


        storageRef.child(currPuzzle).downloadUrl
            .addOnSuccessListener {
                Glide.with(mContext)
                    .load(it)
                    .fitCenter()
                    .centerCrop()
                    .into(holder.puzzleImageView)
            }


    }

    override fun getItemCount(): Int {
        return puzzleList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val puzzleImageView: ImageView = itemView.findViewById(R.id.puzzleImageView)



    }



}