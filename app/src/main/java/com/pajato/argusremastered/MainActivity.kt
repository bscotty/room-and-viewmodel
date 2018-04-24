package com.pajato.argusremastered

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
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

        val adapter = ContentAdapter(emptyList())
        contentList.adapter = adapter
        contentList.layoutManager = LinearLayoutManager(this)

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        viewModel.content.observe(this, Observer<MutableList<Content>> { contentList ->
            if (contentList != null) {
                adapter.items = contentList
                adapter.notifyDataSetChanged()
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                ENTER_REQUEST -> {
                    val title = data.getStringExtra(EnterActivity.TITLE_KEY)
                    val content = Content(title)
                    viewModel.addContent(content)
                }
            }

        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun fabClick(view: View) {
        val intent = Intent(this, EnterActivity::class.java)
        startActivityForResult(intent, ENTER_REQUEST)
    }

    companion object {
        const val ENTER_REQUEST = 0
    }
}
