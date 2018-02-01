package saman.com.butterknifelibrary;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Lib1Activity extends Activity {

    @BindView(R2.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib1);
        ButterKnife.bind(this);
    }

    @OnClick(R2.id.tv_title)
    public void onViewClicked() {
    }
}
