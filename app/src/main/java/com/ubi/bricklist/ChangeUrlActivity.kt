package com.ubi.bricklist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ubi.bricklist.utilities.UserSettings
import kotlinx.android.synthetic.main.activity_change_url.*

class ChangeUrlActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Change XML Url"

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_url)

        urlInput.setText(UserSettings.xmlUrl)
    }

    fun accept(v: View) {
        UserSettings.xmlUrl = urlInput.text.toString()
        finish()
    }

    fun dismiss(v: View) {
        finish()
    }

    override fun finish() {
        val data = Intent()
        setResult(Activity.RESULT_OK, data)

        super.finish()
    }
}
