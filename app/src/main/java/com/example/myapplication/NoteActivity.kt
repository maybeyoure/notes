package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.example.myapplication.models.AppDatabase
import com.example.myapplication.models.note.NoteDAO
import com.example.myapplication.models.note.NoteEntity
import com.example.myapplication.views.ColorCircleView
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
    lateinit var scrollColors: LinearLayout
    lateinit var backgroundBtn: Button
    lateinit var scrollColorsView: View
    var scrollColorsViewShowed = false
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
        scrollColors = findViewById(R.id.scrollColors)

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

        drawColorCircles()

        backgroundBtn = findViewById(R.id.backgroundBtn)
        scrollColorsView = findViewById(R.id.scrollColorsView)
        backgroundBtn.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this,
                if (scrollColorsViewShowed) R.anim.color_selector_hide else R.anim.color_selector_show)
            scrollColorsViewShowed = !scrollColorsViewShowed
            scrollColorsView.startAnimation(animation)
        }
    }

    private fun drawColorCircles() {
        intArrayOf(
            getColor(R.color.white),
            getColor(R.color.light_blue),
            getColor(R.color.light_green),
            getColor(R.color.light_pink),
            getColor(R.color.beige),
            getColor(R.color.dark_beige),
            getColor(R.color.khaki),
        ).map {
            val colorCircle = ColorCircleView(this, it)
            scrollColors.addView(colorCircle)


            val colorSize = resources.getDimension(R.dimen.color_circle_size).toInt()
            val offset = (resources.getDimension(R.dimen.color_selector_height).toInt() - colorSize) / 2
            colorCircle.layoutParams.height = colorSize
            colorCircle.layoutParams.width = colorSize
            val param = colorCircle.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(offset, offset, offset, offset)
            colorCircle.layoutParams = param
        }
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