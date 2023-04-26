package com.example.rgbmanager

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class CorFormActivity : AppCompatActivity() {
    private var position: Int = 0
    private lateinit var color: RGB
    private lateinit var etColor: EditText
    private lateinit var etRed: EditText
    private lateinit var etGreen: EditText
    private lateinit var etBlue: EditText
    private lateinit var btnCancel: Button
    private lateinit var btnSaveColor: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_form)

        this.color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("COR", RGB::class.java)
        } else {
            intent.getSerializableExtra("COR")
        } as RGB

        this.position = intent.getIntExtra("POSITION", 0)

        this.etColor = findViewById(R.id.etColorName)
        this.etRed = findViewById(R.id.etRed)
        this.etGreen = findViewById(R.id.etGreen)
        this.etBlue = findViewById(R.id.etBlue)
        this.btnSaveColor = findViewById(R.id.btSaveColor)
        this.btnCancel = findViewById(R.id.btCancelColor)

        if (this.color.name != "") {
            formulario()
        }

        this.btnSaveColor.setOnClickListener { salvarCor() }
        this.btnCancel.setOnClickListener { finish() }
    }

    private fun formulario() {
        this.etColor.setText(color.name)
        this.etRed.setText(color.red.toString())
        this.etGreen.setText(color.green.toString())
        this.etBlue.setText(color.blue.toString())
    }

    private fun salvarCor() {
        val colorName = this.etColor.text.toString()
        val red = this.etRed.text.toString().toInt()
        val green = this.etGreen.text.toString().toInt()
        val blue = this.etBlue.text.toString().toInt()
        val color = RGB(colorName, red, green, blue)
        val intent = Intent().apply {
            putExtra("COR", color)
            putExtra("POSITION", this@CorFormActivity.position)
        }

        setResult(RESULT_OK, intent)
        finish()
    }
}