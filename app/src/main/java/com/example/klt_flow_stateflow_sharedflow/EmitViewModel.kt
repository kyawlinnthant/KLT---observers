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
     *  It is very cold observer means it can only be emitted data when something starts calling it (means observe it)
     *  It is only life cycle aware data, observed for view states in Main thread.
     */
    private val _liveData: MutableLiveData<String> = MutableLiveData("Initial Value LiveData")

    /**
     * If we start assign liveData with no default value, the caller from Activity can get no value and change nothing.
     * When button is clicked, it starts call the fetchLiveData() and change the liveData
     * Then textView in Activity just know the changes by observe.
     * Use LiveData when you want to operate something like: you want to show user default value and show the user when
     * user make something changes and show that changes ever!!!
     */
//    private val _liveData : MutableLiveData<String> = MutableLiveData()
    val liveData: LiveData<String> get() = _liveData


    /** < Flow > can be or cannot be initiate INITIAL VALUE
     *  It is just a coroutines. But coroutines can only do with once for each data.
     *  Flow can do as coroutines with multiple changes of data.
     *  But it is the continuous flow as its name. When it is once called, it starts its work FLOW.
     *  Normal FLOW does not contain STATE.
     */
    private var _flowData: Flow<String> = flowOf("Initial Value Flow")
//    private var _flowData: Flow<String> = flowOf()
    val flowData get() = _flowData


    /** < StateFlow > can only be initiated with INITIAL VALUE
     *  Like LiveData everything is the same except this StateFlow is kind of Flow.
     *  LiveData can or cannot initiate INITIAL VALUE, but StateFlow can only initiate INITIAL VALUE
     *  LiveData is absolutely cold observer, it can only emit data when the caller start listen(observe) it.
     *  but after call, it can make the connection about data changes with the caller until the caller finish connection.
     *  StateFlow is  HOT observer means it can emit data whether or not it is called.
     *  StateFlow also save the same data and not emit again the same data.
     */
    private val _stateFlowData = MutableStateFlow("Initial Value StateFlow")
    val stateFlowData get() = _stateFlowData.asStateFlow()


    /** < SharedFlow > cannot be initiate INITIAL VALUE
     *  It is HOT observer means it can emit data whether it is called or not.
     *  It only emit data is changed however the caller calls it.
     *  Means that the last data it emitted is not changed when caller call it,
     *  it will not emit that the same data.
     *  Use sharedFlow for one time event.
     */
    private val _sharedFlowData = MutableSharedFlow<String>()
    val sharedFlowData get() = _sharedFlowData.asSharedFlow()

    fun fetchLiveData() {
        viewModelScope.launch {
            _liveData.postValue("This is live data")
        }
    }

    fun fetchFlowData() {
        _flowData = flow {
            repeat(5) {
                emit("$it")
                kotlinx.coroutines.delay(1000L)
            }
        }
    }

    fun fetchStateFlow() {
        viewModelScope.launch {
            _stateFlowData.value = "This is stateFlow"
        }
    }

    fun fetchSharedFlow() {
        viewModelScope.launch {
            _sharedFlowData.emit("This is sharedFlow")
        }
    }
}
