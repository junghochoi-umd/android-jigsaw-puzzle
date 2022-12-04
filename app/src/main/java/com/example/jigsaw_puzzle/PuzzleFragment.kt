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
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.net.URI
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.typeOf


/**
 * A simple [Fragment] subclass.
 * Use the [PuzzleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PuzzleFragment : Fragment() {

    private val TAG = "PuzzleFragment"

    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var db: FirebaseFirestore




    private lateinit var resultLaunchPicture: ActivityResultLauncher<Intent>


    private lateinit var userRef: DocumentReference
    private lateinit var userDocument: DocumentSnapshot


    private lateinit var mUploads: ArrayList<PuzzleUpload>
    private lateinit var mAdapter: PuzzleAdapter

    private lateinit var puzzleRecyclerView: RecyclerView
    private lateinit var puzzleList: ArrayList<String>
    private lateinit var puzzleAdapter: PuzzleAdapter

    private val USER_ID = "GnipAmsiqAE8NzhUN48x"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        db = FirebaseFirestore.getInstance()


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

        puzzleRecyclerView = view?.findViewById(R.id.puzzleRecyclerView)!!
        puzzleAdapter = PuzzleAdapter(arrayListOf(), view.context)
        puzzleRecyclerView.adapter = puzzleAdapter

        resultLaunchPicture =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val imageURI: Uri? = result.data?.data

                    var filePath: String = "images/" + UUID.randomUUID().toString()
                    var ref: StorageReference = storageRef.child(filePath)

                    if (imageURI != null) {
                        ref.putFile(imageURI).addOnSuccessListener {
                            var puzzleDocument: Map<String, String> = createPuzzleMap(filePath, USER_ID)

                            db.collection("puzzles")
                                .add(puzzleDocument)
                                .addOnSuccessListener {
                                    Log.d(TAG, "Puzzle added with ID: " + it.id)
                                    getPosts(view)
                                    puzzleAdapter.notifyDataSetChanged()
                                }
                                .addOnFailureListener {
                                    Log.d(TAG, "Error adding document", it)
                                }
                        }
                    }
                }
            }

        addPuzzleBtn.setOnClickListener {
            val picIntent: Intent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            resultLaunchPicture.launch(picIntent)
        }

        getPosts(view)

    }


    private fun createPuzzleMap(imageFilePath: String, userId: String): HashMap<String, String> {
        return hashMapOf(
            "image_file_path" to imageFilePath,
            "user_id" to userId
        )
    }

    private fun getPosts(view: View) {
        db.collection("puzzles").whereEqualTo("user_id", USER_ID).get()
            .addOnSuccessListener { documents ->
                val puzzles = ArrayList<String>()
                for (doc in documents) {
                    var imageFilePath: String = doc.get("image_file_path") as String
                    puzzles.add(imageFilePath)
                }
                initRecyclerView(puzzles, view)
            }
    }

    private fun initRecyclerView(puzzleList: ArrayList<String>, view: View) {

        puzzleRecyclerView.layoutManager = LinearLayoutManager(view.context)
        puzzleRecyclerView.setHasFixedSize(true)
        puzzleRecyclerView.adapter = PuzzleAdapter(puzzleList, view.context)
    }


}