package com.pajato.argusremastered

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_enter.*

class EnterActivity : AppCompatActivity() {

    companion object {
        val TITLE_KEY = "title"
        val NETWORK_KEY = "network"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter)
        setSupportActionBar(searchToolbar)
        titleInput.afterTextChanged { processSaveButtonState() }
        networkInput.afterTextChanged { processSaveButtonState() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu, color the check icon to be the correct color and disable it.
        menuInflater.inflate(R.menu.save_menu, menu)
        menu.getItem(0).isEnabled = false
        val color = ContextCompat.getColor(applicationContext, android.R.color.white)
        menu.getItem(0).icon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle a click on the save button by wrapping up the activity.
        if (item.itemId == R.id.save) {
            save()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        // Establish an edit text extension function to deal with the save button.
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }

    private fun processSaveButtonState() {
        val menu = searchToolbar.menu
        menu.getItem(0).isEnabled = canSave()
    }

    private fun canSave(): Boolean {
        return !(titleInput.text.isEmpty() || networkInput.text.isEmpty())
    }

    private fun save() {
        // Return the collected data to the main activity.
        val result = Intent()
        result.putExtra(TITLE_KEY, titleInput.editableText.toString())
        result.putExtra(NETWORK_KEY, networkInput.editableText.toString())
        setResult(Activity.RESULT_OK, result)
        finish()
    }
}