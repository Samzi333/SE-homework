package com.jnu.sudokudos.jsudoku;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jnu.sudokudos.R;
import com.jnu.sudokudos.util.DensityUtils;

import java.util.ArrayList;
import java.util.List;

public class Board extends RelativeLayout implements View.OnClickListener {
    private final String TAG = Board.class.getSimpleName();

    private List<List<Grid>> mGridArray = new ArrayList<>();
    private List<List<TextView>> mCellArray;
    private TextView mCurrentCell;

    private String mErrorTextColor = "#ff0000";
    private String mLightTextColor = "#ffffff";
    private String mDefaultTextColor = "#000000";
    private String mLightBgColor = "#571C8C";
    private String mDefaultBgColor = "#ffffff";

    private String mDisableTextColor = "#7E7E7E";

    private GameOverCallBack mGameOverCallBack;

    public Board(Context context) {
        this(context, null);
    }

    public Board(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Board(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        int padding = DensityUtils.dp2px(context, 1);
        setPadding(padding, padding, padding, padding);
        setBackgroundColor(Color.BLACK);
        // 3*3 个九宫格Grid
        // 处理方式和Grid处理tv基本一致
        for (int i = 0; i < 3; i++) {
            List<Grid> gridList = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                // 第 i,j 个Grid
                Grid grid = new Grid(context, attrs, defStyleAttr);
                grid.setId(View.generateViewId());
                addView(grid);
                LayoutParams params = (LayoutParams) grid.getLayoutParams();
                if (j == 0) {
                    if (i == 0) {
                        params.addRule(RelativeLayout.ALIGN_PARENT_START);
                        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    } else if (i == 1) {
                        params.addRule(RelativeLayout.BELOW, mGridArray.get(0).get(0).getId());
                        params.topMargin = DensityUtils.dp2px(context, 1);
                    } else {
                        params.addRule(RelativeLayout.BELOW, mGridArray.get(1).get(0).getId());
                        params.topMargin = DensityUtils.dp2px(context, 1);
                    }
                } else if (j == 1) {
                    params.addRule(RelativeLayout.RIGHT_OF, gridList.get(j - 1).getId());
                    params.addRule(ALIGN_TOP, gridList.get(j - 1).getId());
                    params.leftMargin = DensityUtils.dp2px(context, 1);
                } else {
                    params.addRule(RelativeLayout.RIGHT_OF, gridList.get(j - 1).getId());
                    params.addRule(ALIGN_TOP, gridList.get(j - 1).getId());
                    params.leftMargin = DensityUtils.dp2px(context, 1);
                }
                gridList.add(grid);
            }
            mGridArray.add(gridList);
        }

        // CellArray存9*9个textView
        mCellArray = new ArrayList<>();
        for (int i = 0; i < 9; i++) {//初始化Cell Array
            List<TextView> cellArray = new ArrayList<>();
            for (int j = 0; j < 9; j++) {
                // i, j计算所属的Grid(x, y)
                int x = i < 3 ? 0 : i < 6 ? 1 : 2; //3x3 的格子
                int y = j < 3 ? 0 : j < 6 ? 1 : 2;
                // 获得所在的Grid
                Grid grid = mGridArray.get(x).get(y);
                // 获得所在的Grid的tvArray
                List<List<TextView>> gridTextArrays = grid.getTextArrays();
                // 获得对应的tv
                TextView cell = gridTextArrays.get(i - x * 3).get(j - y * 3);
                // 设置相关属性
                cell.setTag(R.id.row, i);
                cell.setTag(R.id.column, j);
                cell.setTag(R.id.isLoad, false);
                cell.setTextColor(Color.parseColor(mDefaultTextColor));
                cell.setBackgroundColor(Color.parseColor(mDefaultBgColor));
                cell.setOnClickListener(this);
                cellArray.add(j, cell);
            }
            mCellArray.add(i, cellArray);
        }

    }

    public void setGameOverCallBack(GameOverCallBack mGameOverCallBack) {
        this.mGameOverCallBack = mGameOverCallBack;
    }

    @Override
    public void onClick(View v) {
        // 更新点中的Cell(TextView)
        mCurrentCell = (TextView) v;
        // 高亮
        lightUpCellByRowAndColumn((int) v.getTag(R.id.row), (int) v.getTag(R.id.column));
    }

    /**
     * Enter a number from 1-9
     *
     * @param number number
     */
    public void inputText(String number) {
        // 输入number到选中的Cell（tv）
        if (mCurrentCell == null) return;
        // 如果是可填的（没有被Load题目）
        if (!(Boolean) mCurrentCell.getTag(R.id.isLoad)) {
            if("0".equals(number)) mCurrentCell.setText("");
            else mCurrentCell.setText(number);

            boolean gameOver = checkFinish();
            if (gameOver) {
                if (mGameOverCallBack != null) mGameOverCallBack.gameOver();
            }
        }
    }

    /**
     * load sudoku map
     *
     * @param map map
     */
    public void loadMap(String map) {
        if (TextUtils.isEmpty(map)) return;
        // 遍历9*9个Cell
        for (int i = 0; i < mCellArray.size(); i++) {
            List<TextView> array = mCellArray.get(i);
            for (int j = 0; j < array.size(); j++) {
                TextView cell = array.get(j);
                String s = map.substring(9 * i + j, 9 * i + j + 1);
                if (!"0".equals(s)) {
                    cell.setText(s);
                    cell.setTag(R.id.isLoad, true);
                    cell.setTextColor(Color.parseColor(mDisableTextColor));
                }else {
                    cell.setTag(R.id.isLoad, false);
                    cell.setText("");
                }
            }
        }
    }

    /**
     * check Game is finish
     *
     * @return boolean
     */
    private boolean checkFinish() {
        boolean finish = false;
        int row = (int) mCurrentCell.getTag(R.id.row);
        int column = (int) mCurrentCell.getTag(R.id.column);
        // 检查是否有冲突
        boolean error = checkGameError(row, column);
        if (error) {
            // 有冲突就高亮相同数字的
            lightSameNumber(row, column, true);
        } else {
            finish = true;
            lightSameNumber(row, column, false);
            for (int i = 0; i < mCellArray.size(); i++) {
                List<TextView> array = mCellArray.get(i);
                for (int j = 0; j < array.size(); j++) {
                    TextView textView = array.get(j);
                    if (TextUtils.isEmpty(textView.getText().toString())) {
                        finish = false;
                        break;
                    }
                }
            }
        }
        return finish;
    }

    /**
     * check game error
     *
     * @param row    row
     * @param col column
     * @return boolean
     */
    private boolean checkGameError(int row, int col) {
        boolean result = false;
        result = checkSection(row, col);
        if (result) return result;
        //check row
        for (int i = 0; i < 9; i++) {
            String value = mCellArray.get(i).get(col).getText().toString();
            if (TextUtils.isEmpty(value)) continue;
            for (int j = i; j < 9; j++) {
                if (i == j) continue;
                if (value.equals(mCellArray.get(j).get(col).getText().toString())) {
                    Log.d(TAG, String.format("row error,value:%1$s in row:%2$d and col:%3$d", value, row, col));
                    result = true;
                    break;
                }
            }
        }

        if (result) return result;

        //check column
        for (int i = 0; i < 9; i++) {
            String value = mCellArray.get(row).get(i).getText().toString();
            if (TextUtils.isEmpty(value)) continue;
            for (int j = i; j < 9; j++) {
                if (i == j) continue;
                if (value.equals(mCellArray.get(row).get(j).getText().toString())) {
                    Log.d(TAG, String.format("column error,value:%1$s in row:%2$d and column:%3$d", value, row, col));
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * check duplicate numbers in the 3x3 grid
     *
     * @param row    row
     * @param column column
     * @return true or false
     */
    private boolean checkSection(int row, int column) {
        boolean result = false;
        String value = mCellArray.get(row).get(column).getText().toString();
        if (TextUtils.isEmpty(value)) {
            return result;
        }
        int start_i = row < 3 ? 0 : (row < 6 ? 3 : 6);//3x3 格子的边界
        int start_j = column < 3 ? 0 : (column < 6 ? 3 : 6);
        int end_i = start_i + 3;
        int end_j = start_j + 3;

        for (int i = start_i; i < end_i; i++) {
            for (int j = start_j; j < end_j; j++) {
                if (i == row && j == column) continue;
                if (value.equals(mCellArray.get(i).get(j).getText().toString())) {//如果3x3格子的内容有重复的数字则返回错误
                    Log.d(TAG, String.format("section error,value:%1$s in row:%2$d and column:%3$d", value, row, column));
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Highlight the  grid by row and column
     *
     * @param row    row
     * @param column column
     */
    private void lightUpCellByRowAndColumn(int row, int column) {
        boolean lightText = !TextUtils.isEmpty(mCellArray.get(row).get(column).getText().toString());
        if (lightText) {
            lightSameNumber(row, column, false);
        } else {
            lightRowAndColumn(row, column);
        }
    }

    /**
     * Highlight the same number
     *
     * @param row     row
     * @param column  column
     * @param isError error
     */
    private void lightSameNumber(int row, int column, boolean isError) {
        String value = mCellArray.get(row).get(column).getText().toString();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (value.equals(mCellArray.get(i).get(j).getText().toString()) && !"".equals(value)) {
                    //change number color
                    if (i == row && column == j) {
                        //If it is wrong, change the text color without changing the background.
                        if (isError || mCellArray.get(i).get(j).getCurrentTextColor() == Color.parseColor(mErrorTextColor)) {
                            mCellArray.get(i).get(j).setBackgroundColor(Color.parseColor(mErrorTextColor));
                            mCellArray.get(i).get(j).setTextColor(Color.parseColor(mLightTextColor));
                        } else {
                            mCellArray.get(i).get(j).setBackgroundColor(Color.parseColor(mLightBgColor));
                            mCellArray.get(i).get(j).setTextColor(Color.parseColor(mLightTextColor));
                        }
                    } else {
                        if (mCellArray.get(i).get(j).getCurrentTextColor() == Color.parseColor(mErrorTextColor)) {
                            mCellArray.get(i).get(j).setTextColor(Color.parseColor(mErrorTextColor));
                        } else {
                            mCellArray.get(i).get(j).setTextColor(Color.parseColor(mLightTextColor));
                        }
                        mCellArray.get(i).get(j).setBackgroundColor(Color.parseColor(mLightBgColor));
                    }
                } else {
                    if ((Boolean) mCellArray.get(i).get(j).getTag(R.id.isLoad)) {
                        mCellArray.get(i).get(j).setTextColor(Color.parseColor(mDisableTextColor));
                    } else {
                        mCellArray.get(i).get(j).setTextColor(Color.parseColor(mDefaultTextColor));
                    }
                    mCellArray.get(i).get(j).setBackgroundColor(Color.parseColor(mDefaultBgColor));
                }
            }
        }
    }


    /**
     * Highlight row and column
     *
     * @param row    row
     * @param column column
     */
    private void lightRowAndColumn(int row, int column) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (i == row || j == column) {
                    if (mCellArray.get(i).get(j).getCurrentTextColor() == Color.parseColor(mErrorTextColor)) {
                        mCellArray.get(i).get(j).setTextColor(Color.parseColor(mErrorTextColor));
                    }
                    mCellArray.get(i).get(j).setBackgroundColor(Color.parseColor(mLightBgColor));
                } else {
                    if ((Boolean) mCellArray.get(i).get(j).getTag(R.id.isLoad)) {
                        mCellArray.get(i).get(j).setTextColor(Color.parseColor(mDisableTextColor));
                    } else {
                        if (mCellArray.get(i).get(j).getCurrentTextColor() == Color.parseColor(mErrorTextColor)) {
                            mCellArray.get(i).get(j).setTextColor(Color.parseColor(mErrorTextColor));
                        } else {
                            mCellArray.get(i).get(j).setTextColor(Color.parseColor(mDefaultTextColor));
                        }
                    }
                    mCellArray.get(i).get(j).setBackgroundColor(Color.parseColor(mLightTextColor));
                }
            }
        }
        mCellArray.get(row).get(column).setBackgroundColor(Color.parseColor(mLightBgColor));
    }

    public interface GameOverCallBack {
        void gameOver();
    }

    public String getCurrentMap(){
        StringBuilder map= new StringBuilder();
        String s;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                s = mCellArray.get(i).get(j).getText().toString();
                if("".equals(s))
                    s = "0";
                map.append(s);
            }
        }

        return map.toString();
    }
}
