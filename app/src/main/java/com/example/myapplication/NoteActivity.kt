package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.room.Room
import com.example.myapplication.models.AppDatabase
import com.example.myapplication.models.note.NoteDAO
import com.example.myapplication.models.note.NoteEntity

class NoteActivity : ActivityWithoutBack() {
    lateinit var cancelNoteBtn: View
    lateinit var noteHeader: EditText
    lateinit var noteText: EditText
    lateinit var saveNoteBtn: Button
    private lateinit var db: AppDatabase
    private lateinit var noteDAO: NoteDAO

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "db"
        ).build()
        noteDAO = db.noteDAO()

        cancelNoteBtn = findViewById(R.id.cancelNoteBtn)
        cancelNoteBtn.setOnClickListener {
            finish()
        }

        noteHeader = findViewById(R.id.noteHeader)
        noteText = findViewById(R.id.noteText)

        noteHeader.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s?.contains('\n') ?: false) {
                    noteHeader.setText(noteHeader.text.toString().replace("\n", ""))
                    noteText.requestFocus()
                }
            }
        })

        saveNoteBtn = findViewById(R.id.saveNoteBtn)
        saveNoteBtn.setOnClickListener {
            val note = NoteEntity(
                id=1,
                text = noteText.text.toString(),
                title = noteHeader.text.toString()
            )
            noteDAO.insertAll(note)
            finish()
        }


    }
}