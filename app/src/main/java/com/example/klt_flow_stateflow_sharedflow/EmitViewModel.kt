package com.example.klt_flow_stateflow_sharedflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EmitViewModel : ViewModel() {

    /** < LiveData > can be or cannot be initiate INITIAL VALUE
     *  If we declare initial value to liveData, the observer will know it when it observe our liveData.
     *  It is very cold observer means it can only be emit data when something start call it (means observe it)
     *  It is only life cycle aware data, observed for view states in Main thread.
     */
//    private val _liveData : MutableLiveData<String> = MutableLiveData("Initial Value LiveData")
    private val _liveData : MutableLiveData<String> = MutableLiveData()
    val liveData : LiveData<String> get() = _liveData

    /** < Flow > can be or cannot be initiate INITIAL VALUE
     *  It is just a coroutines. But coroutines can only do with once for each data.
     *  Flow can do as coroutines with multiple changes of data.
     */
//    private val _flowData: Flow<String> = flowOf("Initial Value")
    private val _flowData: Flow<String> = flowOf()
    val flowData get() = _flowData

    /** < StateFlow > can only be initiated with INITIAL VALUE
     *  Like LiveData everything is the same except this StateFlow is kind of Flow.
     *  LiveData can or cannot initiate INITIAL VALUE, but StateFlow can only initiate INITIAL VALUE
     *  LiveData is absolutely cold observer, it can only emit data when the caller call it,(in our case, click the button and call fetchLiveData())
     *  but after call, it can make the connection about data changes with the caller until the caller finish connection.
     *  StateFlow is  hot observer means it can emit data whether or not it is called. (in our case, don't even need fetchStateFlow(), the caller
     *  can get the stateFlow data just only use collect). So this is HOT observer.
     */
    private val _stateFlowData = MutableStateFlow("Initial Value StateFlow")
    val stateFlowData get() = _stateFlowData.asStateFlow()

    /** < SharedFlow > cannot be initiate INITIAL VALUE
     *  It is cold observer means it can emit data when it is called.
     *  It only emit data is changed however the caller calls it.
     *  Means that the last data it emitted is not changed when caller call it,
     *  it will not emit that the same data.
     */

    private val _sharedFlowData = MutableSharedFlow<String>()
    val sharedFlowData get() = _sharedFlowData.asSharedFlow()

    fun fetchLiveData(){
        viewModelScope.launch {
            _liveData.postValue("This is live data")
        }
    }

    fun fetchFlow() {
        viewModelScope.launch {

        }
    }

    fun fetchStateFlow() {
        viewModelScope.launch {
            _stateFlowData.value = "This is stateFlow"
        }
    }
    fun fetchSharedFlow() {
        viewModelScope.launch {

        }
    }
}