package com.example.jigsaw_puzzle

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.net.URI
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [PuzzleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PuzzleFragment : Fragment() {

    private val TAG = "PuzzleFragment"

    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_puzzle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addPuzzleBtn: Button = view.findViewById(R.id.addPuzzleBtn)

        var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
             if(result.resultCode== Activity.RESULT_OK){
                 val imageURI: Uri? = result.data?.data


//                 var progressBar: ProgressBar = ProgressBar(context)
//                 progressBar.showContextMenu()


                 var ref: StorageReference = storageRef.child("images/" + UUID.randomUUID().toString())


                 if (imageURI != null) {
                     ref.putFile(imageURI)
                 }


                 Log.d(TAG, imageURI.toString())


             }
        }

        addPuzzleBtn.setOnClickListener(View.OnClickListener { view ->
            val picIntent: Intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            resultLauncher.launch(picIntent)
        })
    }



}