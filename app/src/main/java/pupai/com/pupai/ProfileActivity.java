package pupai.com.pupai;

import android.app.LoaderManager;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import pupai.com.pupai.Loaders.ProfileLoader;

/**
 * Created by jinhoo on 18. 4. 6.
 */

public class ProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

  private ImageView mProfileImageView;
  private TextView mProfileNameTextView, mProfileEmailTextView, mProfileLocationTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);

    mProfileNameTextView = findViewById(R.id.tv_profile_name);
    mProfileEmailTextView = findViewById(R.id.tv_profile_email);
    mProfileLocationTextView = findViewById(R.id.tv_profile_location);

    getLoaderManager().initLoader(R.id.profile_loader_id, null, ProfileActivity.this);
  }

  @Override
  public Loader<String> onCreateLoader(int i, Bundle bundle) {
    return new ProfileLoader(getApplicationContext());
  }

  @Override
  public void onLoadFinished(Loader<String> loader, String s) {
    if(s.contains("error")) {
      Toast.makeText(this, "Connection Error: " + s, Toast.LENGTH_LONG).show();
    }
    else {
      final SharedPreferences preference = getApplicationContext().getSharedPreferences("User Profile", MODE_PRIVATE);

      String[] data = (s.substring(1, s.length()-1)).split(",");
      String name = data[0].substring(data[0].indexOf(":")+1, data[0].length()).replaceAll("\"", "");
      String location = data[1].substring(data[1].indexOf(":")+1, data[1].length()).replaceAll("\"", "");
      String email = preference.getString("email", "");

      mProfileNameTextView.setText(name);
      mProfileEmailTextView.setText(email);
      mProfileLocationTextView.setText(location);
    }
  }

  @Override
  public void onLoaderReset(Loader<String> loader) {
  }
}
