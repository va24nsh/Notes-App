package com.example.mynotes

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), INotesRVAdapter {

    private lateinit var viewModel: NotesViewModel

    @SuppressLint("MissingInflatedId", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        val recyclerV = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerV.layoutManager = LinearLayoutManager(this)
        val adapter = NotesRVAdapter(this, this)
        recyclerV.adapter = adapter

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NotesViewModel::class.java)
        viewModel.allNotes.observe(this, Observer {list ->
            list?.let {
                adapter.updateList(it)
            }
        })

        val submitB = findViewById<Button>(R.id.addButton)
        val inputT = findViewById<EditText>(R.id.input)
        submitB.setOnClickListener {
            val noteText = inputT.text.toString()
            if(noteText.isNotEmpty()) {
                viewModel.insertNote(Note(noteText))
                inputT.hint = "Add another?"
                Toast.makeText(this, "'$noteText' was added to your Notes", Toast.LENGTH_LONG).show()
            }
        }

//        // Adding Modes to the app
//        val switcher: Switch = findViewById<Switch>(R.id.switcher)
//        // Using SharedPreference to save the mode if we exit the app and open it again
//        val sharedP: SharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE)
//        val nightM: Boolean = sharedP.getBoolean("night", false) // Setting Light Mode as default
//        if (nightM) {
//            switcher.isChecked
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        }
//        switcher.setOnClickListener {
//            val editor: SharedPreferences.Editor
//            if (nightM) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                editor = sharedP.edit()
//                editor.putBoolean("night", false)
//            } else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                editor = sharedP.edit()
//                editor.putBoolean("night", true)
//            }
//            editor.apply()
//        }

    }

    override fun onItemClicked(note: Note) {
        viewModel.deleteNote(note)
        val n = note.text
        Toast.makeText(this, "'$n' was removed from your Notes", Toast.LENGTH_LONG).show()
    }
}