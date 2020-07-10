package com.confessionapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class WriteConfessionActivity : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance()
    var genderSpinner: Spinner? = null
    var yearSpinner: Spinner? = null
    var branchSpinner: Spinner? = null
    var confessionEditText: TextInputEditText? = null
    var postNumber: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_confession)

        genderSpinner = findViewById(R.id.genderSpinner)
        yearSpinner = findViewById(R.id.yearSpinner)
        branchSpinner = findViewById(R.id.branchSpinner)
        confessionEditText = findViewById(R.id.confessionEditText)

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.Gender,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            genderSpinner?.adapter = adapter
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.Year,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            yearSpinner?.adapter = adapter
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.Branch,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            branchSpinner?.adapter = adapter
        }


        FirebaseDatabase.getInstance().getReference().child("confessions").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                postNumber = dataSnapshot.childrenCount.toInt() + 1
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })




    }



    fun postClicked(view: View){

        val confession = confessionEditText?.text.toString()

        if (confession.isNotBlank()) {

            try {
                val gender: String = genderSpinner?.selectedItem.toString()
                val year: String = yearSpinner?.selectedItem.toString()
                val branch: String = branchSpinner?.selectedItem.toString()
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val currentDate = sdf.format(Date())

                val postID = UUID.randomUUID().toString()


                val confessionMap: Map<String, String> = mapOf(
                    "confession" to confession,
                    "year" to year,
                    "branch" to branch,
                    "gender" to gender,
                    "timestamp" to currentDate,
                    "postNumber" to postNumber.toString(),
                    "postID" to postID
                )
                FirebaseDatabase.getInstance().getReference().child("confessions").push()
                    .setValue(confessionMap).addOnSuccessListener {

                        Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainFeedActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)

                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed! Please try again!", Toast.LENGTH_SHORT).show()
                    }
            } catch (e: Exception) {
                Toast.makeText(this, "Some Problem occured!", Toast.LENGTH_SHORT).show()
            }

        }


    }


    fun exitClicked(view: View){
        val intent = Intent(this, MainFeedActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

    }


    override fun onBackPressed() {
        val intent = Intent(this, MainFeedActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

    }


}