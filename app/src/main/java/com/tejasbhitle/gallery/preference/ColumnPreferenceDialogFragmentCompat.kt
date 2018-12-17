package com.tejasbhitle.gallery.preference

import androidx.preference.PreferenceDialogFragmentCompat
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.preference.Preference
import com.shawnlin.numberpicker.NumberPicker
import com.tejasbhitle.gallery.R


class ColumnPreferenceDialogFragmentCompat
    : PreferenceDialogFragmentCompat() {

    val TAG = "GCPDialogFragmentCompat"
    private var numberPicker: NumberPicker? = null
    private var selectedValue: Int = 2

    companion object {
        fun newInstance(preference: Preference): ColumnPreferenceDialogFragmentCompat {
            val fragment = ColumnPreferenceDialogFragmentCompat()
            val b = Bundle(1)
            b.putString(PreferenceDialogFragmentCompat.ARG_KEY, preference.key)
            fragment.arguments = b
            return fragment
        }
    }


    override fun onBindDialogView(view: View?) {
        super.onBindDialogView(view)
        numberPicker = view!!.findViewById(R.id.number_picker)
        if(preference is ColumnDialogPreference ){
            numberPicker!!.value = (preference as ColumnDialogPreference).getColumnValue()
            numberPicker!!.setOnValueChangedListener{ picker, oldVal, newVal ->
                selectedValue = newVal
            }
        }

    }

    override fun onDialogClosed(positiveResult: Boolean) {
        Log.e(TAG,positiveResult.toString())
        if(positiveResult && preference is ColumnDialogPreference)
            (preference as ColumnDialogPreference).setColumnValue(selectedValue)
    }
}
