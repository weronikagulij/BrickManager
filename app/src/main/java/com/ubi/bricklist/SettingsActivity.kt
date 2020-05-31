package com.ubi.bricklist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_settings.*
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.ubi.bricklist.utilities.UserSettings


class SettingsActivity : AppCompatActivity() {

    private val tag = "mymsg"
    private val REQUEST_CODE = 10000

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Settings"

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        prefixText.text = UserSettings.xmlUrl

        if(UserSettings.showingActive) {
            archivedSwitch.isChecked = true
            showArchivedText.text = "Hide archived"
        }

        archivedSwitch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                UserSettings.showingActive = true
                showArchivedText.text = "Hide archived"
            } else {
                UserSettings.showingActive = false
                showArchivedText.text = "Show archived"
            }
        }
    }

    fun changePrefix(v: View) {
        val intent = Intent(this, ChangeUrlActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if((requestCode==REQUEST_CODE)
            && (resultCode == Activity.RESULT_OK)) {
            if(data != null) {
                prefixText.text = UserSettings.xmlUrl
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun finish() {
        val data = Intent()
        setResult(Activity.RESULT_OK, data)

        super.finish()
    }
}
