package pupai.com.pupai.Loaders;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by jinhoo on 18. 4. 6.
 */

public class ImageLoader extends Loader<String>{
  private String cachedData;
  private RequestQueue queue;
  private Bundle args;

  public static final String IMAGE_TAG = "imageloader";
  public static final String IMAGE_KEY = "image";

  public ImageLoader(Context context, Bundle args) {
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
    final byte[] imageData = args.getByteArray(IMAGE_KEY);

    final String imageString = imageData.toString();


    String url = "http://52.165.223.103:8000/classify";

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
      protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("image", imageString);
        return params;
      }
      @Override
      public Map<String, String> getHeaders() {
        Map<String, String> header = new HashMap<>();
        header.put("Content-type", "image/png");
        header.put("Content-Disposition", "attachment; filename=upload.jpg");
        return header;
      }
    };
    jsonObjectRequest.setTag(IMAGE_TAG);
    AppSingleton.getInstance(this.getContext()).addToRequestQueue(jsonObjectRequest);

  }

  @Override
  protected void onStopLoading() {
    super.onStopLoading();
  }

  @Override
  protected void onReset() {
    queue.cancelAll(IMAGE_TAG);
  }

  @Override
  public void deliverResult(String data) {
    cachedData = data;
    super.deliverResult(data);
  }
}
