package pupai.com.pupai.Loaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.Volley;

/**
 * Created by jinhoo on 18. 4. 5.
 */

public class AppSingleton {
  private static AppSingleton mInstance;
  private RequestQueue mRequestQueue;
  private ImageLoader mImageLoader;
  private static Context mContext;

  private AppSingleton(Context context) {
    mContext = context;
    mRequestQueue = getRequestQueue();
    mImageLoader = new ImageLoader(mRequestQueue,
        new ImageCache() {
          private final LruCache<String, Bitmap> cache = new LruCache<>(20);

          @Override
          public Bitmap getBitmap(String url) {
            return cache.get(url);
          }

          @Override
          public void putBitmap(String url, Bitmap bitmap) {
            cache.put(url, bitmap);
          }
        });
  }

  public static synchronized AppSingleton getInstance(Context context){
    if(mInstance == null) mInstance = new AppSingleton(context);
    return mInstance;
  }

  public RequestQueue getRequestQueue() {
    if(mRequestQueue == null) {
      mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
    }
    return mRequestQueue;
  }

  public <T> void addToRequestQueue(Request<T> req) {
    getRequestQueue().add(req);
  }

  public ImageLoader getmImageLoader(){
    return mImageLoader;
  }

  protected void cancelPendingRequests(Object tag) {
    if(mRequestQueue != null) mRequestQueue.cancelAll(tag);
  }
}
