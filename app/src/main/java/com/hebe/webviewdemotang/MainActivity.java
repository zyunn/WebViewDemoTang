package com.hebe.webviewdemotang;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView=(WebView)findViewById(R.id.web_view);
        webView.setWebChromeClient(new WebChromeClient(){

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                // 设置调用系统相册的意图(隐式意图)
                Intent intent = new Intent();

                // 设置值活动//android.intent.action.PICK
                intent.setAction(Intent.ACTION_PICK);

                // 设置类型和数据
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");

                // 开启系统的相册
                startActivityForResult(intent,
                        1);




                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            }
        });

        webView.loadUrl("file:///android_asset/tangtang.html");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 这里拿到图片地址路径，上传就可以了
        super.onActivityResult(requestCode, resultCode, data);
        String path = getAbsoluteImagePath(data.getData());
        Bitmap bitmap = BitmapFactory.decodeFile(path);// getimage(path);
        //转成url
        Uri imageUri = Uri.parse(MediaStore.Images.Media.insertImage(
                getContentResolver(), bitmap, null, null));

        Uri result = data == null || resultCode != RESULT_OK ? null
                : data.getData();
        //上传图片


    }

    protected String getAbsoluteImagePath(Uri uri) {
        // can post image
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)

        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }
}
