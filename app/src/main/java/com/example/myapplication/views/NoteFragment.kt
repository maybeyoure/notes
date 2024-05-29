package com.example.myapplication.views

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.example.myapplication.ListActivity
import com.example.myapplication.NoteActivity
import com.example.myapplication.R
import com.example.myapplication.models.AppDatabase
import com.example.myapplication.models.note.NoteDAO
import com.example.myapplication.models.note.NoteEntity
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.concurrent.Callable
import java.util.concurrent.Executors

private const val ARG_PARAM_TITLE = "title"
private const val ARG_PARAM_DATE = "data_created"
private const val ARG_PARAM_ID = "id"


class NoteFragment : Fragment() {
    private lateinit var noteTitle: TextView
    private lateinit var noteDate: TextView
    private lateinit var noteDeleteBtn: View

    private lateinit var paramTitle: String
    private var paramDate: Long = 0
    private var paramId: Int = 0

    private lateinit var noteDAO: NoteDAO
    public var note: NoteEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramTitle = it.getString(ARG_PARAM_TITLE)!!
            paramDate = it.getLong(ARG_PARAM_DATE)
            paramId = it.getInt(ARG_PARAM_ID)
        }
        noteDAO = AppDatabase.getDatabase(requireActivity().application).noteDAO()
        val callable = Callable {
            noteDAO.get(paramId)
        }
        val future = Executors.newSingleThreadExecutor().submit(callable)
        note = future!!.get()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note, container, false)
        noteTitle = view.findViewById(R.id.fragment_note_title)
        noteDate = view.findViewById(R.id.fragment_note_date)
        noteTitle.text = paramTitle
        val dateFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT);
        noteDate.text = dateFormat.format(LocalDateTime.ofEpochSecond(paramDate / 1000, 0, ZonedDateTime.now().getOffset()))

        noteDeleteBtn = view.findViewById(R.id.note_delete_btn)
        noteDeleteBtn.setOnClickListener() { removeNote() }
        view.setOnClickListener() { editNote() }
        return view
    }

    private fun removeNote() {
        AlertDialog.Builder(context)
            .setMessage(R.string.note_delete_message)
            .setPositiveButton(R.string.note_delete_message_yes) { _, _ ->
                val callable = Callable {
                    noteDAO.delete(note!!)
                }
                val future = Executors.newSingleThreadExecutor().submit(callable)
                future!!.get()
                (requireActivity() as ListActivity).updateListNotes()
            }
            .setNegativeButton(R.string.note_delete_message_no) { _, _ -> }
            .show()
    }

    private fun editNote() {
        val intent = Intent(context, NoteActivity::class.java)
        intent.putExtra(NoteActivity.NOTE_ID, note?.id)
        intent.putExtra(NoteActivity.NOTE_OLD_TITLE, note?.title)
        intent.putExtra(NoteActivity.NOTE_OLD_TEXT, note?.text)
        startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance(note: NoteEntity) =
            NoteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_TITLE, note.title)
                    putLong(ARG_PARAM_DATE, note.created)
                    putInt(ARG_PARAM_ID, note.id)
                }
            }
    }
}