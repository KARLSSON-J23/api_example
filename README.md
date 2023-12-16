# Android Rapid API 教學

**以翻譯API為例**

API網址:https://rapidapi.com/dickyagustin/api/text-translator2/

首先打開Rapid Api
<img src="https://i.imgur.com/AgZIJKm.png">

點擊訂閱按鈕

<img src="https://i.imgur.com/WuhNGVd.png">

選擇免費方案

<img src="https://i.imgur.com/9Qv8R1G.png">

點擊訂閱

<img src="https://i.imgur.com/ou63dpj.png">

出現此畫面代表訂閱成功

<img src="https://i.imgur.com/HMAcLZV.png">

測試API是否正常(註:此API目前中文僅有簡體中文)

<img src="https://i.imgur.com/IJ12hxI.png">

### 前端

<img src="https://i.imgur.com/dZH3QG3.png">

XML

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <TextView
        android:id="@+id/textShow_id"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:text="Result"
        android:textSize="24dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.498"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.246" />

    <EditText
        android:id="@+id/edt_input_id"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:ems="10"
        android:inputType="text"
        android:text="Hello World!"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textShow_id" />

    <Button
        android:id="@+id/btn_translator_id"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:text="translator"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/edt_input_id" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

### 開始撰寫程式碼

選擇程式語言

<img src="https://i.imgur.com/qOqlkGr.png">

選擇JAVA 的 OKhttp

<img src="https://i.imgur.com/i0zrnEa.png">

#### 加入implementation
<img src="https://i.imgur.com/rsl2R5F.png">

OKhttp需要加入implementation 因此可到OKhttp官網取得最新版implementation或是複製我提供的版本

```java
implementation("com.squareup.okhttp3:okhttp:4.11.0")
```
OKhttp官網: https://square.github.io/okhttp/
<img src="https://i.imgur.com/PQfUjr8.png">

接著在AndroidManifest.xml中加入網路權限
```xml
<uses-permission android:name="android.permission.INTERNET"></uses-permission>
```

然後將Rapid API提供的程式碼複製到Android Studio中

<img src="https://i.imgur.com/NJZEr4x.png">

這時Request會出現紅字，只需要依照提示將class import進去就好

<img src="https://i.imgur.com/sg2Stdn.png">

<img src="https://i.imgur.com/fpXHrWI.png">

### 接下來需要使用Thread語法，因為在請求API時會有有網路延遲，如果沒有使用執行緒的話會造成系統閃退

Thread語法:

```java
new Thread(new Runnable() {
    @Override
    public void run() {
        //執行內容
    }
}).start();
```

再將Rapid API提供的程式碼結合在Thread中，即可執行

```java
void getTranslatorResult(String input) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "source_language=en&target_language=zh&text=What%20is%20your%20name%3F");

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //請求設定
                    Request request = new Request.Builder()
                        .url("https://text-translator2.p.rapidapi.com/translate")
                        .post(body)
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("X-RapidAPI-Key", "f165b7871amsha687f4a243bd2f6p1146d9jsnaa4cb4499a75")
                        .addHeader("X-RapidAPI-Host", "text-translator2.p.rapidapi.com")
                        .build();
                    Response response = null;
                    //以上為RapidApi 直接複製的程式碼

                    response = client.newCall(request).execute(); //發送請求

                    final String result = response.body().string().toString(); //注意必續將變數設為final

                    //必須使用 runOnUiThread 更新UI否則會造成系統崩潰
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Text_show.setText(result); //將翻譯內容顯示在ShowText中
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
```

### 完整JAVA程式碼

```java
package com.example.api_example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private TextView Text_show;
    private EditText Edt_inputText;
    private Button Btn_Translator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Text_show = findViewById(R.id.textShow_id);
        Edt_inputText= findViewById(R.id.edt_input_id);
        Btn_Translator = findViewById(R.id.btn_translator_id);

        Btn_Translator.setOnClickListener(onClickListener);



    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String  input = Edt_inputText.getText().toString();//取得輸入框文字
            //按鈕被點擊時進行翻譯
            try {
                getTranslatorResult(input);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
    };

    void getTranslatorResult(String input) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "source_language=en&target_language=zh&text=What%20is%20your%20name%3F");

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //請求設定
                    Request request = new Request.Builder()
                        .url("https://text-translator2.p.rapidapi.com/translate")
                        .post(body)
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("X-RapidAPI-Key", "f165b7871amsha687f4a243bd2f6p1146d9jsnaa4cb4499a75")
                        .addHeader("X-RapidAPI-Host", "text-translator2.p.rapidapi.com")
                        .build();
                    Response response = null;
                    //以上為RapidApi 直接複製的程式碼

                    response = client.newCall(request).execute(); //發送請求

                    final String result = response.body().string().toString(); //注意必續將變數設為final

                    //必須使用 runOnUiThread 更新UI否則會造成系統崩潰
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Text_show.setText(result); //將翻譯內容顯示在ShowText中
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();


    }
}
```

### 執行系統，點擊翻譯按鈕

成功出現回傳結果

<img src="https://i.imgur.com/2pkz8FP.png">

因為回傳結果為unicede，所以需要再進行編碼轉換，為了不讓系統太複雜就不做編碼轉換了，以下是轉碼的結果

<img src="https://i.imgur.com/UNKCQmg.png">




