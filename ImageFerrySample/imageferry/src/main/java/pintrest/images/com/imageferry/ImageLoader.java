package pintrest.images.com.imageferry;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Ishtiaq on Dec , 01, 2018.
 */
public class ImageLoader extends Downloader {

    private static final ImageLoader mInstance = new ImageLoader();
    private HashMap<String, HashSet<RequestCreator>> trackMap = new HashMap<>();


    public static ImageLoader getInstance() {
        return mInstance;
    }


    public void load(@NonNull final ImageView imageView, @NonNull final String url) {
        load(imageView, url, null);
    }

    public void load(@NonNull final ImageView imageView, @NonNull final String url, @Nullable final BitmapCallback callback) {

        RequestCreator request = new RequestCreator(imageView, callback);
        HashSet<RequestCreator> requestSet = trackMap.get(url);
        HashSet<RequestCreator> value = requestSet == null ? new HashSet<RequestCreator>() : requestSet;
        value.add(request);
        trackMap.put(url, value);

        DownloadCallback downloadCallback = new DownloadCallback() {
            @Override
            public void onFailure(String tag, Exception e) {
                processFailure(tag, e);
            }

            @Override
            public void onResponse(final String tag, final byte[] data) {

                HashSet<RequestCreator> requestList = trackMap.get(url);
                if (requestList != null) {
                    for (RequestCreator request : requestList) {
                        ImageView requestView = request.getImageView();
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        postResponse(tag, request, requestView, bitmap);
                    }
                    trackMap.remove(url);
                }

            }
        };
        download(url, downloadCallback);
    }

    private void processFailure(@NonNull String url, Exception e) {
        HashSet<RequestCreator> requestList = trackMap.get(url);
        if (requestList != null) {
            for (RequestCreator request : requestList) {
                if (request.getCallback() != null) {
                    request.getCallback().onFailure(e);
                }
            }
            trackMap.remove(url);
        }
    }

    private void postResponse(final String tag, final RequestCreator request, final ImageView requestView, final Bitmap bitmap) {
        if (requestView.getParent() != null) {
            new Thread(new Runnable() {
                public void run() {
                    requestView.post(new Runnable() {
                        public void run() {
                            if (request.getCallback() != null) {
                                request.getCallback().onBitmapLoaded(tag, bitmap);
                            } else {
                                requestView.setImageBitmap(bitmap);
                            }
                        }
                    });
                }
            }).start();
        }
    }

    public void cancel(ImageView imageView, String url) {
        HashSet<RequestCreator> requestList = trackMap.get(url);
        if (requestList != null) {
            for (RequestCreator request : requestList) {
                if (request.getImageView().equals(imageView)) {
                    requestList.remove(request);
                }
            }

            if (requestList.size() == 0) {
                super.cancel(url);
            }
        }
    }


}
