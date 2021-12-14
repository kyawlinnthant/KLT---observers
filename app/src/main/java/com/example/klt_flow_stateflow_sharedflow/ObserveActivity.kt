package com.example.klt_flow_stateflow_sharedflow

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ObserveActivity : AppCompatActivity(R.layout.activity_main) {

    private val tvLiveData: TextView by lazy {
        findViewById(R.id.tv_live_data)
    }
    private val tvFlow: TextView by lazy {
        findViewById(R.id.tv_flow)
    }
    private val tvStateFlow: TextView by lazy {
        findViewById(R.id.tv_state_flow)
    }
    private val tvSharedFlow: TextView by lazy {
        findViewById(R.id.tv_shared_flow)
    }
    private val btnLiveData: Button by lazy {
        findViewById(R.id.btn_live_data)
    }
    private val btnFlow: Button by lazy {
        findViewById(R.id.btn_flow)
    }
    private val btnStateFlow: Button by lazy {
        findViewById(R.id.btn_state_flow)
    }
    private val btnSharedFlow: Button by lazy {
        findViewById(R.id.btn_shared_flow)
    }
    private val viewModel: EmitViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        call()
        observe()
    }

    private fun call() {
        btnLiveData.setOnClickListener {
            viewModel.fetchLiveData()
        }

        /**
         * Just watch how the flow is done.
         * When you listen flowData with lifecycleScope.launch, it can get as fast as soon
         * But it can emit multiple values sequentially, but ONLY A SINGLE VALUE.
         * Therefore in our case we can't get the new data of FLOW when we click the button
         * We can only get by rotate ( change lifecycle ) to get the new FLOW data.
         */
        btnFlow.setOnClickListener {
            viewModel.fetchFlowData()
        }

        /**
         * Before click this button, just see our tvStateFlow.text
         * It is the initial value from ViewModel.
         * And JUST WATCH THE TOAST MESSAGE, hot observer can listen fast
         * So STATEFLOW value can reach first.
         * Now you can know what is the HOT OBSERVER?
         */
        btnStateFlow.setOnClickListener {
            viewModel.fetchStateFlow()
        }
        btnSharedFlow.setOnClickListener {
            viewModel.fetchSharedFlow()
        }
    }

    private fun observe() {

        viewModel.liveData.observe(this) {
            tvLiveData.text = it
            showToast(it)
        }

        lifecycleScope.launch {
            viewModel.flowData.collect {
                tvFlow.text = it
                showToast(it)
            }
        }

        /** Just remember, to use StateFlow we have to observe it in our lifecycleScope.
         *  and just use launchWhenCreated (***)
         *  and collectLatest (***)
         *  our StateFlow is now LIVEDATA, but can change CoroutineContext according to
         *  lifecycleScope. How useful Flow is!!!
         */
        lifecycleScope.launchWhenCreated {
            viewModel.stateFlowData.collectLatest {
                tvStateFlow.text = it
                showToast(it)
            }
        }

        lifecycleScope.launch {
            viewModel.sharedFlowData.collect {
                tvSharedFlow.text = it
                showToast(it)
            }

        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}