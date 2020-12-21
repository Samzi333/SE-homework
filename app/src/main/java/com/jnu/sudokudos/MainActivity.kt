package com.jnu.sudokudos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val dataset: Array<DemosAdapter.Demo> = arrayOf(
        DemosAdapter.Demo("Sudoku", "简单模式", SSudoActivity::class.java),
        DemosAdapter.Demo("Sudoku", "中等模式", MSudoActivity::class.java),
        DemosAdapter.Demo("Sudoku", "困难模式", HSudoActivity::class.java),
        DemosAdapter.Demo("Sudoku", "教程", TSudoActivity::class.java)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewManager = LinearLayoutManager(this)
        viewAdapter = DemosAdapter(dataset)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    fun start(activity: Class<*>, layoutFileId: Int) {
        val intent = Intent(this, activity).apply {
            putExtra("layout_file_id", layoutFileId)
        }
        startActivity(intent)
    }
}