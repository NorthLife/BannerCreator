package rouchuan.viewpagerlayoutmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.leochuan.AutoPlayRecyclerView;
import com.leochuan.BannerCreator;
import com.leochuan.CenterSnapHelper;
import com.leochuan.ScaleLayoutManager;

public class TestActivity extends AppCompatActivity {
    private BannerCreator bannerCreator;
//    private AutoPlayRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    bannerCreator = findViewById(R.id.bc);
        bannerCreator.with().setLayoutManager(new ScaleLayoutManager(this, Util.Dp2px(this, 10)))
                .setAdapter(new DataAdapter()).config().setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_focus})
                .setPageIndicatorAlign(BannerCreator.PageIndicatorAlign.CENTER_HORIZONTAL);

    }
}
