package com.kentoes.appwrite2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.kentoes.appwrite2.models.ProductAdapter
import com.kentoes.appwrite2.models.RealtimeViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<RealtimeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.btnSubscribe)
        button.setOnClickListener {
            viewModel.subscribeToProducts(this)
        }

        val btnGetData=findViewById<Button>(R.id.btnGetData)
        btnGetData.setOnClickListener {
            viewModel.getProducts(this)
        }

        val adapter = ProductAdapter()
        val recycler = findViewById<RecyclerView>(R.id.recyclerProducts)
        recycler.adapter = adapter

        viewModel.productStream.observe(this) {
            adapter.submitNext(it)
        }
        viewModel.productDeleted.observe(this) {
            adapter.submitDeleted(it)
        }

        lifecycle.addObserver(viewModel)
    }

    override fun onStop() {
        super.onStop()
        viewModel.unsubscribe()
        lifecycle.removeObserver(viewModel)
    }
}