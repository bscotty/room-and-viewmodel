package com.pajato.argusremastered

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import com.pajato.argusremastered.model.Content
import com.pajato.argusremastered.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainActivityViewModel
    val objLoaded = " Content objects loaded."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        viewModel.count.observe(this, Observer<Int> { count ->
            if (count != null) {
                counter.text = count.toString()
            }
        })

        viewModel.content.observe(this, Observer<MutableList<Content>> { contentList ->
            if (contentList != null) {
                Snackbar.make(root, "" + contentList.size + objLoaded, Snackbar.LENGTH_LONG)
                        .show()
                counter.text = contentList.size.toString()
            }
        })

    }

    fun fabClick(view: View) {
        viewModel.increment()
    }
}
