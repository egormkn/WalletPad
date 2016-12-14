package su.gear.walletpad.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import su.gear.walletpad.R;

public class IconView extends View {

    private static final int CIRCLE = 0;
    private static final int SQUARE = 1;

    int shape = CIRCLE;
    int shapeColor = Color.GRAY;
    int icon = 0;
    int iconColor = Color.WHITE;
    float iconWidth = 0.0f;
    float iconHeight = 0.0f;



    private Paint paintShape;

    public IconView(Context context) {
        super(context);
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
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.IconView, defStyleAttr, defStyleRes);
        try {
            shape = a.getInt(R.styleable.IconView_shape, CIRCLE);
            shapeColor = a.getColor(R.styleable.IconView_shape_color, Color.GRAY);
            icon = a.getResourceId(R.styleable.IconView_icon, 0);
            iconColor = a.getColor(R.styleable.IconView_icon_color, Color.WHITE);
            iconWidth = a.getDimension(R.styleable.IconView_icon_width, 0);
            iconHeight = a.getDimension(R.styleable.IconView_icon_height, 0);
        } finally {
            a.recycle();
        }

        setupPaint();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int shapeWidth = getMeasuredWidth();
        int shapeHeight = getMeasuredHeight();
        switch (shape) {
            case CIRCLE:
                canvas.drawCircle(shapeWidth / 2, shapeHeight / 2, shapeWidth / 2, paintShape);
                break;
            case SQUARE:
                canvas.drawRect(0, 0, shapeWidth, shapeHeight, paintShape);
                break;
            default:
                throw new IllegalArgumentException("Unknown shape");
        }
        Drawable myImage = getContext().getResources().getDrawable(icon, getContext().getTheme());
        myImage.draw(canvas);
    }

    private void setupPaint() {
        paintShape = new Paint();
        paintShape.setAntiAlias(true);
        paintShape.setStyle(Paint.Style.FILL);
        paintShape.setColor(shapeColor);
        paintShape.setTextSize(30);
    }
}
