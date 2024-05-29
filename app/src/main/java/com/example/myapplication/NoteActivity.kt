package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.myapplication.models.AppDatabase
import com.example.myapplication.models.note.NoteDAO
import com.example.myapplication.models.note.NoteEntity
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class NoteActivity : ActivityWithoutBack() {
    companion object {
        val NOTE_ID = "noteId"
        val NOTE_OLD_TITLE = "noteOldTitle"
        val NOTE_OLD_TEXT = "noteOldText"

        val NO_HAS_NOTE_ID = -1
    }

    lateinit var cancelNoteBtn: View
    lateinit var noteHeader: EditText
    lateinit var noteText: EditText
    lateinit var saveNoteBtn: Button
    private lateinit var noteDAO: NoteDAO

    private var oldId: Int = NO_HAS_NOTE_ID

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        noteDAO = AppDatabase.getDatabase(application).noteDAO()

        cancelNoteBtn = findViewById(R.id.cancelNoteBtn)
        cancelNoteBtn.setOnClickListener {
            finish()
        }

        noteHeader = findViewById(R.id.noteHeader)
        noteText = findViewById(R.id.noteText)

        oldId = intent.getIntExtra(NOTE_ID, NO_HAS_NOTE_ID)
        if (oldId != NO_HAS_NOTE_ID) {
            noteHeader.setText(intent.getStringExtra(NOTE_OLD_TITLE) ?: "")
            noteText.setText(intent.getStringExtra(NOTE_OLD_TEXT) ?: "")
        }

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
        saveNoteBtn.setOnClickListener { saveNote() }
    }

    private fun saveNote() {
        if (oldId == NO_HAS_NOTE_ID) {
            saveNewNote()
        } else {
            updateNote()
        }
        finish()
    }

    private fun saveNewNote() {
        val callable = Callable {
            val note = NoteEntity(
                text = noteText.text.toString(),
                title = noteHeader.text.toString()
            )
            noteDAO.insertAll(note)
        }
        val future = Executors.newSingleThreadExecutor().submit(callable)
        future!!.get()
    }

    private fun updateNote() {
        val callable1 = Callable { noteDAO.get(oldId) }
        var future = Executors.newSingleThreadExecutor().submit(callable1)
        val note = future!!.get()!!
        note.title = noteHeader.text.toString()
        note.text = noteText.text.toString()
        val callable2 = Callable {
            noteDAO.update(note)
            note
        }
        future = Executors.newSingleThreadExecutor().submit(callable2)
        future!!.get()!!
    }
}