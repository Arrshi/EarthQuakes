package com.hfad.ideasworld.ui.earthquake.sorting

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.hfad.ideasworld.R
import kotlinx.android.synthetic.main.dialog_fragment.view.*

class SortingDialogFragment :DialogFragment(){
    private var resultType:Int=0
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val alertDialog = AlertDialog.Builder(it)
            val inflater = this.layoutInflater
            val view = inflater.inflate(R.layout.dialog_fragment, null)
            val textView:TextView=view.findViewById(R.id.sort_value)
            alertDialog.setView(view)
                .setPositiveButton("Ok") { dialogInterface: DialogInterface, i: Int ->
                    val intent = Intent()
                    intent.putExtra(MESSAGE_CONFRIM, resultType)
                    targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
                }
            view.seek_sort.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                @SuppressLint("SetTextI18n")
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    val tempText=textView.text.dropLastWhile { it_ch -> it_ch.isDigit()}
                    textView.text="$tempText$p1"
                    resultType=p1
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }

            })
            alertDialog.create()
            alertDialog.show()
        }?:throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        val MESSAGE_CONFRIM="MESSAGE"
    }
}