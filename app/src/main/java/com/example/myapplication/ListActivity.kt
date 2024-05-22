package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.myapplication.models.AppDatabase
import com.example.myapplication.models.note.NoteDAO
import com.example.myapplication.models.note.NoteEntity
import com.example.myapplication.models.note.NoteRepository

class ListActivity : ActivityWithoutBack() {
    lateinit var addNoteBtn: Button

    private lateinit var repository: NoteRepository
    private lateinit var allNotes: LiveData<List<NoteEntity>>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        addNoteBtn = findViewById(R.id.addNoteBtn)
        addNoteBtn.setOnClickListener {
            val intent = Intent(this, NoteActivity::class.java)
            startActivity(intent)
        }

        updateListNotes()
    }

    private fun updateListNotes() {
        val dao = AppDatabase.getDatabase(application).noteDAO()
        repository = NoteRepository(dao)
        allNotes = repository.allNotes

    }
}