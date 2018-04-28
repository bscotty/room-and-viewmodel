package com.pajato.argusremastered

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.pajato.argusremastered.model.Content
import com.pajato.argusremastered.view.ContentAdapter
import com.pajato.argusremastered.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManager = LocationManager(this)
        this.lifecycle.addObserver(locationManager)

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
                    val network = data.getStringExtra(EnterActivity.NETWORK_KEY)
                    val content = Content(title, network)
                    viewModel.addContent(content)
                }
            }
        }
    }

    /** In the event of a permission granted, we continue with whatever task prompted the request. */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        // If the request is from the location manager, we want to re-request location.
        if (requestCode == LocationManager.LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationAccess()
            }
        }
        // Otherwise, let the system handle it.
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
