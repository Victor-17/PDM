package com.example.rgbmanager

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var rvColors: RecyclerView
    private lateinit var fabAddColor: FloatingActionButton
    private lateinit var editColorResult: ActivityResultLauncher<Intent>
    private var colors: MutableList<RGB> = mutableListOf()

    init {
        this.colors.add(RGB("Red", 255, 0, 0))

        this.colors.add(RGB("Green", 0, 255, 0))

        this.colors.add(RGB("Blue", 0, 0, 255))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.rvColors = findViewById(R.id.rvColors)
        this.fabAddColor = findViewById(R.id.fabAddColor)

        this.rvColors.adapter = CorAdapter(this.colors)
        (this.rvColors.adapter as CorAdapter).onItemClickRecyclerView = OnItemClick()

        ItemTouchHelper(OnSwipe()).attachToRecyclerView(this.rvColors)

        val addColorResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK) {
                val color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.data?.getSerializableExtra("COR", RGB::class.java)
                } else {
                    it.data?.getSerializableExtra("COR") as RGB
                } as RGB

                (this.rvColors.adapter as CorAdapter).add(color)
            }
        }

        this.editColorResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.data?.getSerializableExtra("COR", RGB::class.java)
                } else {
                    it.data?.getSerializableExtra("COR") as RGB
                } as RGB

                val position = it.data?.getIntExtra("POSITION", 0) ?: 0

                (this@MainActivity.rvColors.adapter as CorAdapter).edit(color, position)
            }
        }

        this.fabAddColor.setOnClickListener {
            val intent = Intent(this, CorFormActivity::class.java).apply {
                putExtra("COR", RGB("", 0, 0, 0))
            }
            addColorResult.launch(intent)
        }
    }



    inner class OnItemClick: OnItemClickRecyclerView() {
        override fun onItemClick(position: Int) {
            val color = this@MainActivity.colors.get(position)

            val intent = Intent(this@MainActivity, CorFormActivity::class.java).apply {
                putExtra("COR", color)
                putExtra("POSITION", position)
            }

            this@MainActivity.editColorResult.launch(intent)
        }
    }

    inner class OnSwipe: ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.DOWN or ItemTouchHelper.UP,
        ItemTouchHelper.START or ItemTouchHelper.END
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder,
        ): Boolean {
            (this@MainActivity.rvColors.adapter as CorAdapter).mov(
                viewHolder.adapterPosition,
                target.adapterPosition
            )
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if (direction == ItemTouchHelper.START) {
                val cor = colors[viewHolder.adapterPosition]
                val shareIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, cor.getHex())
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(shareIntent, "Compartilhar"))
            } else {
                val builder = AlertDialog.Builder(this@MainActivity).apply {
                    setTitle("Remover a cor")
                    setMessage("Deseja remover essa cor?")
                    setPositiveButton("Sim") { _, _ ->
                        (this@MainActivity.rvColors.adapter as CorAdapter).del(viewHolder.adapterPosition)
                    }
                    setNegativeButton("Não", null)
                    this@MainActivity.rvColors.adapter?.notifyDataSetChanged()
                }
                builder.create().show()
            }
        }
    }
}