package rouchuan.viewpagerlayoutmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import rouchuan.viewpagerlayoutmanager.carousel.CarouselLayoutActivity;
import rouchuan.viewpagerlayoutmanager.circle.CircleLayoutActivity;
import rouchuan.viewpagerlayoutmanager.circlescale.CircleScaleLayoutActivity;
import rouchuan.viewpagerlayoutmanager.gallery.GalleryLayoutActivity;
import rouchuan.viewpagerlayoutmanager.rotate.RotateLayoutActivity;
import rouchuan.viewpagerlayoutmanager.scale.ScaleLayoutActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public final static String INTENT_TITLE = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt_circle).setOnClickListener(this);
        findViewById(R.id.bt_circle_scale).setOnClickListener(this);
        findViewById(R.id.bt_elevate_scale).setOnClickListener(this);
        findViewById(R.id.bt_gallery).setOnClickListener(this);
        findViewById(R.id.bt_rotate).setOnClickListener(this);
        findViewById(R.id.bt_scale).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_circle:
                startActivity(CircleLayoutActivity.class, v);
                break;
            case R.id.bt_circle_scale:
                startActivity(CircleScaleLayoutActivity.class, v);
                break;
            case R.id.bt_elevate_scale:
                startActivity(CarouselLayoutActivity.class, v);
                break;
            case R.id.bt_gallery:
                startActivity(GalleryLayoutActivity.class, v);
                break;
            case R.id.bt_rotate:
                startActivity(RotateLayoutActivity.class, v);
                break;
            case R.id.bt_scale:
                startActivity(ScaleLayoutActivity.class, v);
                break;
        }
    }

    private void startActivity(Class clz, View view) {
        Intent intent = new Intent(this, clz);
        if (view instanceof AppCompatButton) {
            intent.putExtra(INTENT_TITLE, ((AppCompatButton) view).getText());
        }
        startActivity(intent);
    }
}
