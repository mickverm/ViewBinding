package be.mickverm.viewbinding.sample

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import be.mickverm.viewbinding.bindView

class MainActivity : AppCompatActivity() {

    private val tvText by bindView<TextView>(R.id.tv_text)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvText.text = "Hello ViewBinding!"
    }
}
