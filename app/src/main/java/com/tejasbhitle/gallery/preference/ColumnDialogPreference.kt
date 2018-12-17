package com.tejasbhitle.gallery.preference

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.preference.DialogPreference
import com.tejasbhitle.gallery.R

class ColumnDialogPreference internal constructor(context: Context,
                                                  attrs: AttributeSet?,
                                                  defStyleAttr: Int,
                                                  defStyleRes: Int)
    : DialogPreference(context){

    val TAG = "DialogPreference"

    constructor (context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : this(context, attrs, defStyleAttr, 0){

        /*
            Explicitly setting attributes defined in xml
            as they aren't getting implicitly getting set
        */
        for(i in 0 until attrs!!.attributeCount){
            val attr = attrs.getAttributeName(i)
            val value = attrs.getAttributeValue(i)
            when(attr){
                "title" -> title = value
                "summary" -> summary = value
                "key" -> key = value
                "iconSpaceReserved" -> isIconSpaceReserved = value!!.toBoolean()
            }
            Log.d(TAG,"$attr -> $value")
        }
    }

    constructor (context: Context, attrs: AttributeSet?)
            : this(context,attrs,R.attr.dialogPreferenceStyle)

    constructor (context: Context)
            : this(context, null)


    private val default: Int = 2
    private var columns : Int = default


    init {
        isPersistent = true
        columns = getColumnValue()
        summary = getColumnValue().toString()
        dialogLayoutResource = R.layout.dialog_grid_column_prefs
        dialogTitle = "Select column count"
        dialogMessage = "Grid columns number"
        positiveButtonText = "Okay"
        negativeButtonText = "Cancel"
        Log.e(TAG,"init")
    }

    fun getColumnValue() = getPersistedInt(default)

    fun setColumnValue(columns: Int){
        this.columns = columns
        persistInt(columns)
        summary = columns.toString()
    }

    override fun onSetInitialValue(restorePersistedValue: Boolean, defaultValue: Any?) {
        super.onSetInitialValue(restorePersistedValue, defaultValue)
        setColumnValue(
                if(restorePersistedValue) getPersistedInt(default)
                else defaultValue as Int
        )
    }

    override fun getDialogLayoutResource(): Int {
        return R.layout.dialog_grid_column_prefs
    }


}