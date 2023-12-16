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