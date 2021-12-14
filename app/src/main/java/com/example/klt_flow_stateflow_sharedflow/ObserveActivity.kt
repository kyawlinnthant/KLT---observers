package com.example.klt_flow_stateflow_sharedflow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import org.w3c.dom.Text

class ObserveActivity : AppCompatActivity(R.layout.activity_main) {

    private val tvLiveData : TextView by lazy {
        findViewById(R.id.tv_live_data)
    }
    private val tvFlow : TextView by lazy {
        findViewById(R.id.tv_flow)
    }
    private val tvStateFlow : TextView by lazy {
        findViewById(R.id.tv_state_flow)
    }
    private val tvSharedFlow : TextView by lazy {
        findViewById(R.id.tv_shared_flow)
    }
    private val btnLiveData : Button by lazy {
        findViewById(R.id.btn_live_data)
    }
    private val btnFlow : Button by lazy {
        findViewById(R.id.btn_flow)
    }
    private val btnStateFlow : Button by lazy {
        findViewById(R.id.btn_state_flow)
    }
    private val btnSharedFlow : Button by lazy {
        findViewById(R.id.btn_shared_flow)
    }
    private val viewModel : EmitViewModel by viewModels()

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
         * Before click this button, just see our tvStateFlow.text
         * It is the initial value from ViewModel.
         * Now you can know what is the HOT OBSERVER?
         */
        btnStateFlow.setOnClickListener {
            viewModel.fetchStateFlow()
        }
    }

    private fun observe() {

        viewModel.liveData.observe(this){
            tvLiveData.text = it
            showToast(it)
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
    }

    private fun showToast(text: String) {
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
    }
}