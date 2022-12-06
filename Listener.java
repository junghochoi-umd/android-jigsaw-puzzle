package dragosholban.com.androidpuzzlegame;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.StrictMath.abs;

public class TouchListener implements View.OnTouchListener {
    private float xCoordinateApplied;
    private float yCoordinateApplied;
    private PuzzleActivity action;

    public TouchListener(PuzzleActivity action) {
        this.action = action;
    }

    @Override
    public boolean onTouch(View viewSeen, MotionEvent motion) {
        float x = motion.getRawX();
        float y = motion.getRawY();
        final double capacity = sqrt(pow(viewSeen.getWidth(), 2) + pow(viewSeen.getHeight(), 2)) / 10;

        PuzzlePiece block = (PuzzlePiece) viewSeen;
        if (!block.canMove) {
            return true;
        }

        RelativeLayout.LayoutParams leftFrame = (RelativeLayout.LayoutParams) viewSeen.getLayoutParams();
        switch (motion.getAction() & Motion.ACTION_MASK) {
            case Motion.ACTION_MOVE:
                leftFrame.leftSideDiff = (int) (x - xCoordinateApplied);
                leftFrame.topSideDiff = (int) (y - yCoordinateApplied);
                viewSeen.setLayoutParams(leftFrame);
                break;
            case Motion.ACTION_DOWN:
                xCoordinateApplied = x - leftFrame.leftSideDiff;
             yCoordinateApplied = y - leftFrame.topSideDiff;
                block.bringToFront();
                break;
            case Motion.ACTION_UP:
                int verticalDiff = abs(block.xCoord - leftFrame.leftSideDiff);
                int horizontalDiff = abs(block.yCoord - leftFrame.topSideDiff);
                if (verticalDiff <= capacity && horizontalDiff <= capacity) {
                    leftFrame.leftSideDiff = block.xCoord;
                    leftFrame.topSideDiff = block.yCoord;
                    block.setLayoutParams(leftFrame);
                    block.canMove = false;
                    sendViewToBack(block);
                    action.checkGameOver();
                }
                break;
        }

        return true;
    }

    public void sendViewToBack(final View pid) {
        final ViewGroup parentPid = (ViewGroup)pid.getParent();
        if (null != parent) {
            parentPid.removeView(pid);
            parentPid.addView(pid, 0);
        }
    }
}
