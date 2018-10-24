package pupai.com.pupai;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import pupai.com.pupai.Loaders.LoginLoader;

/**
 * Created by jinhoo on 18. 4. 6.
 */

public class LoginActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

  private EditText mEmailEditText, mPasswordEditText;
  private Button mSubmitButton;
  private TextView mRegisterTextView, mMainTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    mEmailEditText = findViewById(R.id.et_email);
    mPasswordEditText = findViewById(R.id.et_password);
    mSubmitButton = findViewById(R.id.bt_login);
    mRegisterTextView = findViewById(R.id.link_signup);
    mMainTextView = findViewById(R.id.link_main);

    mSubmitButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final Bundle b = new Bundle();
        String email = mEmailEditText.getText().toString();
        b.putString(LoginLoader.EMAIL_KEY, email);
        b.putString(LoginLoader.PASSWORD_KEY, mPasswordEditText.getText().toString());

        final SharedPreferences preference = getApplicationContext().getSharedPreferences("User Profile", MODE_PRIVATE);
        final Editor editor = preference.edit();
        editor.putString("email", email).apply();

        getLoaderManager().initLoader(R.id.login_loader_id, b, LoginActivity.this);
      }
    });
/*
    mRegisterTextView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Start the Signup activity
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
      }
    });

    mMainTextView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Start the Signup activity
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
      }
    });
*/
  }

  @Override
  public Loader<String> onCreateLoader(int i, Bundle bundle) {
    return new LoginLoader(getApplicationContext(), bundle);
  }

  @Override
  public void onLoadFinished(Loader<String> loader, String s) {
    if(s.contains("error")) {
      Toast.makeText(this, "Login Failed!", Toast.LENGTH_LONG).show();
    }
    else {
      final SharedPreferences preference = getApplicationContext().getSharedPreferences("User Profile", MODE_PRIVATE);
      final Editor editor = preference.edit();
      String token = s.substring(s.indexOf(':')+2, s.indexOf('}')-1);
      editor.putString("token", token).apply();
      Intent new_intent = new Intent(getApplicationContext(), ProfileActivity.class);
      startActivity(new_intent);
    }
  }

  @Override
  public void onLoaderReset(Loader<String> loader) {
  }
}
