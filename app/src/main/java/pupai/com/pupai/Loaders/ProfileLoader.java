package pupai.com.pupai.Loaders;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
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

public class ProfileLoader extends Loader<String> {
  private String cachedData;
  private RequestQueue queue;

  public static final String PROFILE_TAG = "profileloader";

  public ProfileLoader(Context context) {
    super(context);
    queue = AppSingleton.getInstance(this.getContext()).getRequestQueue();
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
    final String token = preference.getString("token", "");

    String url = "http://52.165.223.103:8000/user/profile";

    StringRequest jsonObjectRequest = new StringRequest(Method.GET, url,
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
      public Map<String, String> getHeaders()throws AuthFailureError {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Token "+token);
        return header;
      }
    };
    jsonObjectRequest.setTag(PROFILE_TAG);
    AppSingleton.getInstance(this.getContext()).addToRequestQueue(jsonObjectRequest);
  }

  @Override
  protected void onStopLoading() {
    super.onStopLoading();
  }

  @Override
  protected void onReset() {
    queue.cancelAll(PROFILE_TAG);
  }

  @Override
  public void deliverResult(String data) {
    cachedData = data;
    super.deliverResult(data);
  }
}

