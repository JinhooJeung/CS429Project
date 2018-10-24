package pupai.com.pupai;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import pupai.com.pupai.Loaders.ImageLoader;
import pupai.com.pupai.Loaders.LoginLoader;

/**
 * Created by jinhoo on 18. 4. 6.
 */

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{
  private static final int REQUEST_IMAGE_CAPTURE = 1;
  private static final int REQUEST_IMAGE_RETRIEVAL = 2;

  private ImageView mImagePreview;
  private Button mSubmitButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mImagePreview = findViewById(R.id.iv_main);
    mSubmitButton = findViewById(R.id.bt_submit_main);

    if (savedInstanceState != null) {
      byte[] imageData = savedInstanceState.getByteArray(ImageLoader.IMAGE_KEY);

      if (imageData != null) {
        //Log.d("Location", "Restoring savedInstanceState");
        Bitmap bmp = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        mImagePreview.setImageBitmap(bmp);
      } else {
        Log.d("LoadingError", "Error while loading imageData");
      }
    }

    mSubmitButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        BitmapDrawable drawable = (BitmapDrawable) mImagePreview.getDrawable();
        Bitmap compressedImage = drawable.getBitmap();

        ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
        compressedImage.compress(Bitmap.CompressFormat.PNG, 100, imageStream);
        String imageString = Base64.encodeToString(imageStream.toByteArray(), Base64.DEFAULT);
        byte[] imageData = imageStream.toByteArray();
        final Bundle b = new Bundle();
        b.putByteArray(ImageLoader.IMAGE_KEY, imageData);
        getLoaderManager().initLoader(R.id.image_loader_id, b, MainActivity.this);
      }
    });
  }

  public void takePicture(View view) {
    Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    if(captureIntent.resolveActivity(getPackageManager()) != null)
      startActivityForResult(captureIntent, REQUEST_IMAGE_CAPTURE);
  }

  public void browseGallery(View view) {
    Intent browseIntent = new Intent(Intent.ACTION_GET_CONTENT);
    browseIntent.setType("image/*");

    if(browseIntent.resolveActivity(getPackageManager()) != null)
      startActivityForResult(browseIntent, REQUEST_IMAGE_RETRIEVAL);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    BitmapDrawable drawable = (BitmapDrawable) mImagePreview.getDrawable();
    Bitmap compressedImage = drawable.getBitmap();
    ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
    compressedImage.compress(Bitmap.CompressFormat.PNG, 100, imageStream);
    byte[] imageData = imageStream.toByteArray();
    outState.putByteArray(ImageLoader.IMAGE_KEY, imageData);
  }

  @Override
  public void onActivityResult(int request, int result, Intent data) {
    if(request == REQUEST_IMAGE_CAPTURE && result == RESULT_OK && data != null) {
      handleImageCapture(data.getExtras());
    } else if (request == REQUEST_IMAGE_RETRIEVAL && result == RESULT_OK && data != null) {
      handleImageRetrieval(data.getData());
    }
  }

  private void handleImageCapture(Bundle data) {
    if(data != null) {
      Bitmap image = (Bitmap) data.get("data");

      if(image != null) {
        mImagePreview.setImageBitmap(image);
        mSubmitButton.setClickable(true);
        mSubmitButton.setVisibility(View.VISIBLE);
      }
    }
  }

  private void handleImageRetrieval(Uri uri) {
    InputStream imageStream = null;

    try {
      imageStream = getContentResolver().openInputStream(uri);
      Bitmap image = BitmapFactory.decodeStream(imageStream);

      if(image != null) {
        mImagePreview.setImageBitmap(image);
        mSubmitButton.setClickable(true);
        mSubmitButton.setVisibility(View.VISIBLE);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } finally {
      try {
        if (imageStream != null)
          imageStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public Loader<String> onCreateLoader(int i, Bundle bundle) {
    return new ImageLoader(getApplicationContext(), bundle);
  }

  @Override
  public void onLoadFinished(Loader<String> loader, String s) {
    if(s.contains("error")) {
      Toast.makeText(this, "Network Error "+s, Toast.LENGTH_LONG).show();
    }
    else {
      Toast.makeText(this, "Success: "+s, Toast.LENGTH_LONG).show();
      //Intent new_intent = new Intent(getApplicationContext(), ProfileActivity.class);
      //startActivity(new_intent);
    }
  }

  @Override
  public void onLoaderReset(Loader<String> loader) {
  }
}
