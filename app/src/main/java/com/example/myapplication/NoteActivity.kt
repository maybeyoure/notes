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

class NoteActivity : ActivityWithoutBack() {
    lateinit var cancelNoteBtn: View
    lateinit var noteHeader: EditText
    lateinit var noteText: EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

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


    }
}