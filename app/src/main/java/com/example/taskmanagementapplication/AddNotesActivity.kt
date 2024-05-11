package com.example.taskmanagementapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskmanagementapplication.databinding.ActivityAddNotesBinding
import com.example.taskmanagementapplication.databinding.ActivityMainBinding

class AddNotesActivity : AppCompatActivity() {

    //Setting up Binding
    private lateinit var binding: ActivityAddNotesBinding

    private lateinit var db: NoteDatabaseHelper



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initializing on database

        db = NoteDatabaseHelper(this)

        //Getting User Inputs

        binding.saveBtn.setOnClickListener{
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
            val note = Note(0,title,content)
            db.insertNote(note) //Inserting values into the database
            finish() //Close the Activity
            Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show()
        }

    }
}