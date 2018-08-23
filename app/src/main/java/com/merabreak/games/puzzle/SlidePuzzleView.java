package com.merabreak.games.puzzle;

import android.content.Context;
import android.graphics.*;
import android.graphics.Paint.Style;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.merabreak.R;

import java.util.HashSet;
import java.util.Set;

public class SlidePuzzleView extends View {

    public interface SlidePuzzleListener {
        public void isSolved();
    }

    public static enum ShowNumbers {NONE, SOME, ALL}

    private static final int FRAME_SHRINK = 1;

    private static final long VIBRATE_DRAG = 5;
    private static final long VIBRATE_MATCH = 50;
    private static final long VIBRATE_SOLVED = 250;

    private static final int COLOR_SOLVED = 0x00000000;
    private static final int COLOR_ACTIVE = 0x00000000;

    private Bitmap bitmap;
    private Rect sourceRect;
    private RectF targetRect;
    private SlidePuzzle slidePuzzle;
    private int targetWidth;
    private int targetHeight;
    private int targetOffsetX;
    private int targetOffsetY;
    private int puzzleWidth;
    private int puzzleHeight;
    private int targetColumnWidth;
    private int targetRowHeight;
    private int sourceColumnWidth;
    private int sourceRowHeight;
    private int sourceWidth;
    private int sourceHeight;
    private Set<Integer> dragging = null;
    private int dragStartX;
    private int dragStartY;
    private int dragOffsetX;
    private int dragOffsetY;
    private int dragDirection;
    private ShowNumbers showNumbers = ShowNumbers.SOME;
    private Paint textPaint;
    private Paint circlePaint;
    private int canvasWidth;
    private int canvasHeight;
    private Paint framePaint;
    private boolean dragInTarget = false;
    private int[] tiles;
    private Paint tilePaint;

    SlidePuzzleListener slidePuzzleListener;

    public SlidePuzzleView(Context context, SlidePuzzle slidePuzzle, SlidePuzzleListener slidePuzzleListener) {
        super(context);

        this.slidePuzzleListener = slidePuzzleListener;

        sourceRect = new Rect();
        targetRect = new RectF();

        this.slidePuzzle = slidePuzzle;

        tilePaint = new Paint();
        tilePaint.setAntiAlias(true);
        //  tilePaint.setARGB(0xbd, 0xff, 0xff, 0xff);
        tilePaint.setDither(true);
        tilePaint.setFilterBitmap(true);

        textPaint = new Paint();
        textPaint.setARGB(0xff, 0xff, 0xff, 0xff);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(getResources().getColor(R.color.white));
        textPaint.setTextSize(30);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        // textPaint.setShadowLayer(1, 2, 2, 0xff000000);

        framePaint = new Paint();
        framePaint.setARGB(0xff, 0xff, 0x00, 0x00);
        framePaint.setStyle(Style.STROKE);

        circlePaint = new Paint();
        circlePaint.setColor(getResources().getColor(R.color.black_transparent2));
        circlePaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        puzzleWidth = puzzleHeight = 0;
    }

    private void refreshDimensions() {
        targetWidth = canvasWidth;
        targetHeight = canvasHeight;

        sourceWidth = bitmap.getWidth();
        sourceHeight = bitmap.getHeight();

        double targetRatio = (double) targetWidth / (double) targetHeight;
        double sourceRatio = (double) sourceWidth / (double) sourceHeight;

        targetOffsetX = 0;
        targetOffsetY = 0;

        if (sourceRatio > targetRatio) {
            int newTargetHeight = (int) (targetWidth / sourceRatio);
            int delta = targetHeight - newTargetHeight;
            targetOffsetY = delta / 2;
            targetHeight = newTargetHeight;
        } else if (sourceRatio < targetRatio) {
            int newTargetWidth = (int) (targetHeight * sourceRatio);
            int delta = targetWidth - newTargetWidth;
            targetOffsetX = delta / 2;
            targetWidth = newTargetWidth;
        }

        puzzleWidth = slidePuzzle.getWidth();
        puzzleHeight = slidePuzzle.getHeight();

        targetColumnWidth = targetWidth / puzzleWidth;
        targetRowHeight = targetHeight / puzzleHeight;
        sourceColumnWidth = sourceWidth / puzzleWidth;
        sourceRowHeight = sourceHeight / puzzleHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (slidePuzzle == null || bitmap == null) {
            return;
        }

        if (puzzleWidth != slidePuzzle.getWidth() || puzzleHeight != slidePuzzle.getHeight()) {
            refreshDimensions();
        }

        boolean solved = slidePuzzle.isSolved();
        canvas.drawColor(solved ? COLOR_SOLVED : COLOR_ACTIVE);

        int[] originalTiles = slidePuzzle.getTiles();

        if (tiles == null || tiles.length != originalTiles.length) {
            tiles = new int[originalTiles.length];
        }

        for (int i = 0; i < tiles.length; i++) {
            if (originalTiles[i] == originalTiles.length - 1) {
                continue;
            }

            if (dragInTarget && dragging.contains(i)) {
                tiles[i - SlidePuzzle.DIRECTION_X[dragDirection] - puzzleWidth * SlidePuzzle.DIRECTION_Y[dragDirection]] = originalTiles[i];
            } else {
                tiles[i] = originalTiles[i];
            }
        }

        int delta = !dragInTarget ? 0 : (SlidePuzzle.DIRECTION_X[dragDirection] + puzzleWidth * SlidePuzzle.DIRECTION_Y[dragDirection]) * dragging.size();
        int shownHandleLocation = slidePuzzle.getHandleLocation() + delta;
        tiles[shownHandleLocation] = tiles.length - 1;

        int emptyTile = tiles.length - 1;

        for (int i = 0; i < tiles.length; i++) {
            if (!solved && originalTiles[i] == emptyTile) {
                continue;
            }

            int targetColumn = slidePuzzle.getColumnAt(i);
            int targetRow = slidePuzzle.getRowAt(i);

            int sourceColumn = slidePuzzle.getColumnAt(originalTiles[i]);
            int sourceRow = slidePuzzle.getRowAt(originalTiles[i]);

            targetRect.left = targetOffsetX + targetColumnWidth * targetColumn;
            targetRect.top = targetOffsetY + targetRowHeight * targetRow;
            targetRect.right = targetColumn < puzzleWidth - 1 ? targetRect.left + targetColumnWidth : targetOffsetX + targetWidth;
            targetRect.bottom = targetRow < puzzleHeight - 1 ? targetRect.top + targetRowHeight : targetOffsetY + targetHeight;

            sourceRect.left = sourceColumnWidth * sourceColumn;
            sourceRect.top = sourceRowHeight * sourceRow;
            sourceRect.right = sourceColumn < puzzleWidth - 1 ? sourceRect.left + sourceColumnWidth : sourceWidth;
            sourceRect.bottom = sourceRow < puzzleHeight - 1 ? sourceRect.top + sourceRowHeight : sourceHeight;

            boolean isDragTile = dragging != null && dragging.contains(i);

            boolean matchLeft;
            boolean matchRight;
            boolean matchTop;
            boolean matchBottom;

            int di = i;

            if (dragInTarget && dragging.contains(i)) {
                di = di - SlidePuzzle.DIRECTION_X[dragDirection] - puzzleWidth * SlidePuzzle.DIRECTION_Y[dragDirection];
            }

            if (di == tiles[di]) {
                matchLeft = matchRight = matchTop = matchBottom = true;
            } else {
                matchLeft = (di - 1) >= 0 && di % puzzleWidth > 0 && tiles[di] % puzzleWidth > 0 && tiles[di - 1] == tiles[di] - 1;
                matchRight = tiles[di] + 1 < tiles.length - 1 && (di + 1) % puzzleWidth > 0 && (tiles[di] + 1) % puzzleWidth > 0 && (di + 1) < tiles.length && (di + 1) % puzzleWidth > 0 && tiles[di + 1] == tiles[di] + 1;
                matchTop = (di - puzzleWidth) >= 0 && tiles[di - puzzleWidth] == tiles[di] - puzzleWidth;
                matchBottom = tiles[di] + puzzleWidth < tiles.length - 1 && (di + puzzleWidth) < tiles.length && tiles[di + puzzleWidth] == tiles[di] + puzzleWidth;
            }

            if (!matchLeft) {
                sourceRect.left += FRAME_SHRINK;
                targetRect.left += FRAME_SHRINK;
            }

            if (!matchRight) {
                sourceRect.right -= FRAME_SHRINK;
                targetRect.right -= FRAME_SHRINK;
            }

            if (!matchTop) {
                sourceRect.top += FRAME_SHRINK;
                targetRect.top += FRAME_SHRINK;
            }

            if (!matchBottom) {
                sourceRect.bottom -= FRAME_SHRINK;
                targetRect.bottom -= FRAME_SHRINK;
            }

            if (isDragTile) {
                targetRect.left += dragOffsetX;
                targetRect.right += dragOffsetX;
                targetRect.top += dragOffsetY;
                targetRect.bottom += dragOffsetY;
            }

            canvas.drawBitmap(bitmap, sourceRect, targetRect, tilePaint);

            if (!matchLeft) {
                canvas.drawLine(targetRect.left, targetRect.top, targetRect.left, targetRect.bottom, framePaint);
            }

            if (!matchRight) {
                canvas.drawLine(targetRect.right - 1, targetRect.top, targetRect.right - 1, targetRect.bottom, framePaint);
            }

            if (!matchTop) {
                canvas.drawLine(targetRect.left, targetRect.top, targetRect.right, targetRect.top, framePaint);
            }

            if (!matchBottom) {
                canvas.drawLine(targetRect.left, targetRect.bottom - 1, targetRect.right, targetRect.bottom - 1, framePaint);
            }

            if (!solved && (showNumbers == ShowNumbers.ALL || (showNumbers == ShowNumbers.SOME && di != tiles[di]))) {
                canvas.drawText(String.valueOf(originalTiles[i] + 1), (targetRect.left + targetRect.right) / 2, (targetRect.top + targetRect.bottom) / 2 - (textPaint.descent() + textPaint.ascent()) / 2, textPaint);
                canvas.drawCircle((targetRect.left + targetRect.right) / 2, (targetRect.top + targetRect.bottom) / 2 - (textPaint.descent() + textPaint.ascent()) / 2 - 10, 25, circlePaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (slidePuzzle == null || bitmap == null) {
            return false;
        }

        if (slidePuzzle.isSolved()) {
            return false;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return startDrag(event);
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            return updateDrag(event);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            return finishDrag(event);
        } else {
            return false;
        }
    }

    private boolean finishDrag(MotionEvent event) {
        if (dragging == null) {
            return false;
        }

        updateDrag(event);

        if (dragInTarget) {
            doMove(dragDirection, dragging.size());
        } else {
            vibrate(VIBRATE_DRAG);
        }

        dragInTarget = false;
        dragging = null;
        invalidate();
        return true;
    }

    private void doMove(int dragDirection, int count) {
        playSlide();
        if (slidePuzzle.moveTile(dragDirection, count)) {
            vibrate(slidePuzzle.isSolved() ? VIBRATE_SOLVED : VIBRATE_MATCH);
        } else {
            vibrate(VIBRATE_DRAG);
        }

        invalidate();

        if (slidePuzzle.isSolved()) {
            onFinish();
        }
    }

    private void vibrate(long d) {
        Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

        if (v != null) {
//            v.vibrate(d);
        }
    }

    private void onFinish() {
        slidePuzzleListener.isSolved();
//		SlidePuzzleMain activity = (SlidePuzzleMain) getContext();
//		activity.onFinish();
    }

    private void playSlide() {
//        SlidePuzzleMain activity = (SlidePuzzleMain) getContext();
//        activity.playSound();
    }


    private boolean startDrag(MotionEvent event) {
        if (dragging != null) {
            return false;
        }

        int x = ((int) event.getX() - targetOffsetX) / targetColumnWidth;
        int y = ((int) event.getY() - targetOffsetY) / targetRowHeight;

        if (x < 0 || x >= puzzleWidth || y < 0 || y >= puzzleHeight) {
            return false;
        }

        int direction = slidePuzzle.getDirection(x + puzzleWidth * y);

        if (direction >= 0) {
            dragging = new HashSet<Integer>();

            while (x + puzzleWidth * y != slidePuzzle.getHandleLocation()) {
                dragging.add(x + puzzleWidth * y);
                dragStartX = (int) event.getX();
                dragStartY = (int) event.getY();
                dragOffsetX = 0;
                dragOffsetY = 0;
                dragDirection = direction;

                x -= SlidePuzzle.DIRECTION_X[direction];
                y -= SlidePuzzle.DIRECTION_Y[direction];
            }
        }

        dragInTarget = false;
        vibrate(VIBRATE_DRAG);

        return true;
    }

    private boolean updateDrag(MotionEvent event) {
        if (dragging == null) {
            return false;
        }

        int directionX = SlidePuzzle.DIRECTION_X[dragDirection] * -1;
        int directionY = SlidePuzzle.DIRECTION_Y[dragDirection] * -1;

        if (directionX != 0) {
            dragOffsetX = (int) event.getX() - dragStartX;

            if (Math.signum(dragOffsetX) != directionX) {
                dragOffsetX = 0;
            } else if (Math.abs(dragOffsetX) > targetColumnWidth) {
                dragOffsetX = directionX * targetColumnWidth;
            }
        }

        if (directionY != 0) {
            dragOffsetY = (int) event.getY() - dragStartY;

            if (Math.signum(dragOffsetY) != directionY) {
                dragOffsetY = 0;
            } else if (Math.abs(dragOffsetY) > targetRowHeight) {
                dragOffsetY = directionY * targetRowHeight;
            }
        }

        dragInTarget = Math.abs(dragOffsetX) > targetColumnWidth / 2 ||
                Math.abs(dragOffsetY) > targetRowHeight / 2;

        invalidate();

        return true;
    }


    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        puzzleWidth = 0;
        puzzleHeight = 0;
    }

    public ShowNumbers getShowNumbers() {
        return showNumbers;
    }

    public int getTargetWidth() {
        return targetWidth;
    }

    public int getTargetHeight() {
        return targetHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int canvasWidth = MeasureSpec.getSize(widthMeasureSpec);
        int canvasHeight = MeasureSpec.getSize(heightMeasureSpec);

        if (this.canvasWidth != canvasWidth || this.canvasHeight != canvasHeight) {
            this.canvasWidth = canvasWidth;
            this.canvasHeight = canvasHeight;
            puzzleWidth = 0;
            puzzleHeight = 0;
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
