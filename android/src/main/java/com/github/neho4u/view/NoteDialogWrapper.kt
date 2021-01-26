package com.github.neho4u.view

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.github.neho4u.R
import com.github.neho4u.databinding.DialogNoteLayoutBinding
import com.github.neho4u.model.Note

class NoteDialogWrapper(
    private val context: Context,
    private val note: Note?,
    private val onOkCallback: (text: String, dialog: Dialog) -> Unit
) {
    private val layoutBinding = DialogNoteLayoutBinding.inflate(LayoutInflater.from(context))
    private val dialog = Dialog(context).apply {
        setContentView(layoutBinding.root)
        setTitle(context.resources.getString(R.string.search_filer))
    }

    init {
        with(layoutBinding) {
            bNoteCancel.setOnClickListener {
                dialog.dismiss()
            }
            bNoteOk.setOnClickListener {
                onOkCallback(layoutBinding.etNoteText.text.toString(), dialog)
            }
        }
    }

    fun show() {
        dialog.show()
        if (note != null) {
            layoutBinding.textView.text = context.getString(R.string.edit_note)
        }
        layoutBinding.bNoteOk.isEnabled = false
        initWithNote(note)
        layoutBinding.bNoteOk.isEnabled = true
        layoutBinding.etNoteText.requestFocus()
    }

    private fun initWithNote(note: Note?) {
        layoutBinding.etNoteText.setText(note?.mobileNoteText ?: "")
    }
}
