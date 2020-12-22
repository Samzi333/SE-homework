package com.jnu.sudokudos

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.jnu.sudokudos.dataprocess.MapTool
import com.jnu.sudokudos.dataprocess.Qbank
import com.jnu.sudokudos.jsudoku.Board

class TSudoActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mSudoKu: Board

    private lateinit var mSudoConfig: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sudoku)

        mSudoKu = findViewById(R.id.board)
        mSudoConfig = Qbank.getTeachMap();
        mSudoKu.setGameOverCallBack {
            AlertDialog.Builder(this).setTitle("awesome!")
                .setMessage("Congratulations，you solve the Sudoku!")
                .setNegativeButton("exit") { dialog, which ->
                    dialog.dismiss()
                    finish()
                }
                .setPositiveButton("Next") { dialog, which ->
                    dialog.dismiss()

                }
                .create()
                .show()

        }
        mSudoKu.loadMap(mSudoConfig)
    }

    override fun onClick(v: View?) {
        var number: TextView = v as TextView

        mSudoKu.inputText(number.text.toString())

    }
}