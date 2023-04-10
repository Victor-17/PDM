package com.example.janela

import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var rvNomes: RecyclerView
    private lateinit var fabAdd: FloatingActionButton
    private var lista = mutableListOf<String>()
    private lateinit var etNome: EditText
    private lateinit var tts: TextToSpeech

    init {
        this.lista.add("Flávia")
        this.lista.add("José")
        this.lista.add("Maria")
        this.lista.add("Pedro")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.rvNomes = findViewById(R.id.rvNomes)
        this.fabAdd = findViewById(R.id.fabAdd)

        this.fabAdd.setOnClickListener{ add() }

        this.rvNomes.adapter = MyAdapter(this.lista)
        (this.rvNomes.adapter as MyAdapter).onItemClickRecyclerView = OnItemClick()

        this.tts = TextToSpeech(this, null)

        ItemTouchHelper(OnSwipe()).attachToRecyclerView(this.rvNomes)
    }

    fun add(){
        this.etNome = EditText(this)
        val builder = AlertDialog.Builder(this).apply {
            setTitle("Novo Nome!")
            setMessage("Digite o novo nome")
            setView(this@MainActivity.etNome)
            setPositiveButton("Salvar", OnClick())
            setNegativeButton("Cancelar", null)
        }
        builder.create().show()
    }

    inner class OnSwipe: ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.DOWN or ItemTouchHelper.UP,
        ItemTouchHelper.START or ItemTouchHelper.END){

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            TODO("Not yet implemented")
        }

    }

    inner class OnClick: OnClickListener{
        override fun onClick(dialog: DialogInterface?, which: Int) {
            val nome = this@MainActivity.etNome.text.toString()
            (this@MainActivity.rvNomes.adapter as MyAdapter).add(nome)
        }
    }

    inner class OnItemClick: OnItemClickRecyclerView{
        override fun onItemClick(position: Int) {
            val nome = this@MainActivity.lista.get(position)
//            Toast.makeText(this@MainActivity, nome, Toast.LENGTH_SHORT).show()
            this@MainActivity.tts.speak(nome, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }
}