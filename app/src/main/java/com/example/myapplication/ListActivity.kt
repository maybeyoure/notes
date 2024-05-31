package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.example.myapplication.models.AppDatabase
import com.example.myapplication.views.NoteFragment
import java.util.concurrent.Callable
import java.util.concurrent.Executors


class ListActivity : ActivityWithoutBack() {
    lateinit var addNoteBtn: Button
    lateinit var scrollNotes: LinearLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        addNoteBtn = findViewById(R.id.addNoteBtn)
        addNoteBtn.setOnClickListener {
            val intent = Intent(this, NoteActivity::class.java)
            startActivity(intent)
        }

        scrollNotes = findViewById(R.id.scrollColors)
        updateListNotes()
    }

    override fun onResume() {
        super.onResume()
        updateListNotes()
    }

    public fun updateListNotes() {
        val dao = AppDatabase.getDatabase(application).noteDAO()
        val callable = Callable { dao.getAll() }
        val future = Executors.newSingleThreadExecutor().submit(callable)
        val notes = future!!.get()
        scrollNotes.removeAllViews()
        for (i in 0 until notes.size) {
            val frame = FrameLayout(this)
            scrollNotes.addView(frame)
            frame.id = View.generateViewId()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.add(frame.id, NoteFragment.newInstance(notes[i]))
            fragmentTransaction.commit()
        }
    }
}