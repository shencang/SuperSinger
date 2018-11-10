package cn.edu.nc.music.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import cn.edu.nc.music.R;

public class search extends AppCompatActivity {
    private FrameLayout singer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        singer = findViewById(R.id.singer);
        singer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(search.this,PlayPage.class);
                startActivity(intent);
            }
        });
        ImageView search_goback = findViewById(R.id.search_goback);
        search_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.this.finish();
            }
        });
    }
}
