package pupai.com.pupai.Loaders;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jinhoo on 18. 4. 6.
 */

public class LoginLoader extends Loader<String>{
  private String cachedData;
  private RequestQueue queue;
  private Bundle args;

  public static final String LOGIN_TAG = "loginloader";
  public static final String EMAIL_KEY = "email";
  public static final String PASSWORD_KEY = "password";

  public LoginLoader(Context context, Bundle args) {
    super(context);
    queue = AppSingleton.getInstance(this.getContext()).getRequestQueue();
    this.args = args;
  }

  @Override
  protected void onStartLoading() {
    if(cachedData == null) {
      forceLoad();
    }
    else {
      super.deliverResult(cachedData);
    }
  }

  @Override
  protected void onForceLoad() {
    final SharedPreferences preference = getContext().getSharedPreferences("User Profile", MODE_PRIVATE);
    final Editor editor = preference.edit();

    final String email = args.getString(EMAIL_KEY);
    final String password = args.getString(PASSWORD_KEY);

    String url = "http://52.165.223.103:8000/user/login";

    StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            deliverResult(response);
          }
        }, new ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
          deliverResult("error "+error.getMessage());
      }
    }){
      @Override
      protected Map<String, String> getParams(){
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        return params;
      }
    };
    jsonObjectRequest.setTag(LOGIN_TAG);
    AppSingleton.getInstance(this.getContext()).addToRequestQueue(jsonObjectRequest);
  }

  @Override
  protected void onStopLoading() {
    super.onStopLoading();
  }

  @Override
  protected void onReset() {
    queue.cancelAll(LOGIN_TAG);
  }

  @Override
  public void deliverResult(String data) {
    cachedData = data;
    super.deliverResult(data);
  }
}
