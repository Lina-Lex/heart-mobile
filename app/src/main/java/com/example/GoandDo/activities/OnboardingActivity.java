package com.example.GoandDo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.GoandDo.R;
import com.example.GoandDo.request.APIInterface;
import com.example.GoandDo.utils.credentials;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OnboardingActivity extends AppCompatActivity {

    Button btn_Submit;
    EditText number_edtxt;

    private String TAG = "OnboardingActivity";
    private String phoneNumber;
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        btn_Submit = findViewById(R.id.submitOnboard_button);
        number_edtxt = findViewById(R.id.number);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(credentials.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(APIInterface.class);


        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = number_edtxt.getText().toString();
                if (!phoneNumber.isEmpty()) {
                    getOtp(ApiJsonMap(phoneNumber));

                    startOtpVerificationActivity();
                }

            }
        });

    }

    //navigates to OtpVerificationActivity
    private void startOtpVerificationActivity() {
        Intent intent = new Intent(this, OtpVerificationActivity.class);
        startActivity(intent);
    }

    //sends the Otp code to the given phoneNumber
    //params: The Json Api body of the Post request
    private void getOtp(JsonObject ApiBody) {
        Call<JsonObject> call = apiInterface.get_otp(ApiBody);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i("on", "sent number" + response);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    //returns the Json Api body for the get_otp post request
    private JsonObject ApiJsonMap(String phoneNumber) {

        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObj_ = new JSONObject();
            jsonObj_.put("phone", phoneNumber);

            //parses the Json object to Json
            gsonObject = (JsonObject) JsonParser.parseString(jsonObj_.toString());

            Log.i(TAG, "Json Object : " + gsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gsonObject;
    }

}