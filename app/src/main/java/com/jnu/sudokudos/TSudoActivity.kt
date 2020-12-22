package com.jnu.sudokudos

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.jnu.sudokudos.dataprocess.MapTool
import com.jnu.sudokudos.dataprocess.Qbank
import com.jnu.sudokudos.jsudoku.Board

class TSudoActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mSudoKu: Board
    private var firstFlag:  Boolean = true

    private lateinit var mSudoConfig: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sudoku)

        mSudoKu = findViewById(R.id.board)
        mSudoConfig = Qbank.loadMap(this, "t")
        mSudoKu.setGameOverCallBack {
            AlertDialog.Builder(this).setTitle("")
                .setMessage("成功通过教程！")
                .setNegativeButton("再看看") { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("下一局") { dialog, which ->
                    dialog.dismiss()
                    mSudoConfig = Qbank.getTeachMap()
                    mSudoKu.loadMap(mSudoConfig)
                }
                .create()
                .show()
        }
        mSudoKu.loadMap(mSudoConfig)

        // 同一题重来
        var button: Button = findViewById(R.id.button_again)
        button.setOnClickListener(){
            mSudoKu.loadMap(mSudoConfig)
        }

        // 新开一局
        button = findViewById(R.id.button_next)
        button.setOnClickListener(){
            mSudoConfig = Qbank.getTeachMap()
            mSudoKu.loadMap(mSudoConfig)
        }

        // 保存
        button = findViewById(R.id.button_save)
        button.setOnClickListener(){
            Qbank.saveMap(this, "t", mSudoKu.currentMap)

            AlertDialog.Builder(this).setTitle("")
                .setMessage("假设你已经了解了数独的规则。那么尝试解决这个最简单的数独吧！\n记得尝试不同的按钮")
                .create()
                .show()
        }
    }

    override fun onClick(v: View?) {
        var number: TextView = v as TextView

        mSudoKu.inputText(number.text.toString())

    }
}