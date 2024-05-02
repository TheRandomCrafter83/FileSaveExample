package com.coderzf1.filesaveexample

import android.app.Application
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class MainActivityViewModel(private val app:Application): AndroidViewModel(app) {

    var data = MutableLiveData("")
        private set

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveFile(uri:Uri, data:String){
        viewModelScope.launch(Dispatchers.IO) {
            val fileStream = app.contentResolver.openOutputStream(uri, "w") ?: return@launch
            Log.d("VIEWMODEL", "saveFile: FileSaving")
            val writer = OutputStreamWriter(fileStream)
            writer.write(data)
            writer.flush()
            fileStream.close()
        }

    }

    fun loadFile(uri:Uri){
        viewModelScope.launch(Dispatchers.IO) {
            val fileStream = app.contentResolver.openInputStream(uri) ?: return@launch
            val reader = InputStreamReader(fileStream)
            data.postValue(reader.readText())
            fileStream.close()
        }
    }
}