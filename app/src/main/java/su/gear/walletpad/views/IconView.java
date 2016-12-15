package su.gear.walletpad.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.view.View;

import su.gear.walletpad.R;

public class IconView extends View {

    private static final String TAG = IconView.class.getSimpleName();

    private static final int CIRCLE = 0;
    private static final int SQUARE = 1;

    private int shape = CIRCLE;
    private int iconColor = Color.WHITE;
    private int shapeColor = Color.GRAY;
    private int shapeWidth = 0;
    private int shapeHeight = 0;
    private Paint paintShape = null;
    private Drawable icon = null;
    private int iconWidth = 0;
    private int iconHeight = 0;

    public IconView(Context context) {
        super(context);
        setupAttributes(null, 0, 0);
    }

    public IconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupAttributes(attrs, 0, 0);
    }

    public IconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupAttributes(attrs, defStyleAttr, 0);
    }

    private void setupAttributes(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        paintShape = new Paint();
        paintShape.setAntiAlias(true);
        paintShape.setStyle(Paint.Style.FILL);
        if (attrs == null) {
            return;
        }
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.IconView, defStyleAttr, defStyleRes);
        try {
            shape = a.getInt(R.styleable.IconView_shape, CIRCLE);
            shapeColor = a.getColor(R.styleable.IconView_shape_color, Color.GRAY);
            icon = AppCompatResources.getDrawable(getContext(), a.getResourceId(R.styleable.IconView_icon, R.drawable.ic_default));
            if (icon != null) {
                icon = icon.mutate();
            }
            iconColor = a.getColor(R.styleable.IconView_icon_color, Color.WHITE);
            iconWidth = (int) a.getDimension(R.styleable.IconView_icon_width, 16);
            iconHeight = (int) a.getDimension(R.styleable.IconView_icon_height, 16);
        } finally {
            a.recycle();
        }

        paintShape.setColor(shapeColor);
        if (icon != null) {
            icon.setColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getWidth() - getPaddingRight();
        int bottom = getHeight() - getPaddingBottom();
        shapeWidth = getWidth();
        shapeHeight = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (shape) {
            case CIRCLE:
                canvas.drawCircle(shapeWidth / 2, shapeHeight / 2, Math.min(shapeWidth / 2, shapeHeight / 2), paintShape);
                break;
            case SQUARE:
                canvas.drawRect(0, 0, shapeWidth, shapeHeight, paintShape);
                break;
        }
        if (icon != null) {
            icon.setBounds((shapeWidth - iconWidth) / 2, (shapeHeight - iconHeight) / 2, (shapeWidth - iconWidth) / 2 + iconWidth, (shapeHeight - iconHeight) / 2 + iconHeight);
            icon.draw(canvas);
        }
    }

    public int getShapeColor() {
        return shapeColor;
    }

    public void setShapeColor(int color) {
        shapeColor = color;
        paintShape.setColor(shapeColor);
        invalidate();
        requestLayout();
    }

    public int getIconColor() {
        return iconColor;
    }

    public void setIconColor(int color) {
        iconColor = color;
        if (icon != null) {
            icon.setColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP);
        }
        invalidate();
        requestLayout();
    }
}
