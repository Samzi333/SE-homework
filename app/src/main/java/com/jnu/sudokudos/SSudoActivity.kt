package com.jnu.sudokudos

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.jnu.sudokudos.dataprocess.MapTool
import com.jnu.sudokudos.dataprocess.Qbank
import com.jnu.sudokudos.jsudoku.Board

class SSudoActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mSudoKu: Board

    private lateinit var mSudoConfig: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sudoku)

        mSudoKu = findViewById(R.id.board)
        mSudoConfig = Qbank.loadMap(this, "s")
        mSudoKu.setGameOverCallBack {
            AlertDialog.Builder(this).setTitle("")
                .setMessage("挑战成功！")
                .setNegativeButton("再看看") { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("下一局") { dialog, which ->
                    dialog.dismiss()
                    mSudoConfig = Qbank.getSimpleMap()
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
            mSudoConfig = Qbank.getSimpleMap()
            mSudoKu.loadMap(mSudoConfig)
        }

        // 保存
        button = findViewById(R.id.button_save)
        button.setOnClickListener(){
            Qbank.saveMap(this, "s", mSudoKu.currentMap)
        }
    }

    override fun onClick(v: View?) {
        val number: TextView = v as TextView
        mSudoKu.inputText(number.text.toString())
    }
}