package pupai.com.pupai;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import pupai.com.pupai.Loaders.FavAdapter;

/**
 * Created by jinhoo on 18. 4. 16.
 */

public class FavActivity extends AppCompatActivity{
  private RecyclerView recyclerView;
  private RecyclerView.Adapter mAdapter;
  private RecyclerView.LayoutManager mLayoutManager;

  @Override
  public void onCreate( Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fav);

    recyclerView = findViewById(R.id.rv_fav);

    mLayoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(mLayoutManager);

    List<String> input = new ArrayList<>();
    for(int i = 0; i < 10; i++) {
      input.add("Test"+i);
    }

    mAdapter = new FavAdapter(input);
    recyclerView.setAdapter(mAdapter);
  }
}
