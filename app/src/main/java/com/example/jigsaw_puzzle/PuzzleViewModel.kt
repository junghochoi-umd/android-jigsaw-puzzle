package com.example.jigsaw_puzzle

import android.util.Log
import androidx.lifecycle.*


class PuzzleViewModel: ViewModel()  {


    private val _puzzleArr = MutableLiveData<Any>()

    val puzzleArr:  LiveData<Any>
        get() = _puzzleArr



}