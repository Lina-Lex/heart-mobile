package com.example.GoandDo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chaos.view.PinView;
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

public class OtpVerificationActivity extends AppCompatActivity {

    Button btn_signIn;
    PinView otpPin_edtxt;

    private final String TAG = "OtpVerificationActivity";
    String phoneNumber;
    String otp;
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        btn_signIn = findViewById(R.id.signInOtp_button);
        otpPin_edtxt = findViewById(R.id.otp_pin);


        phoneNumber = getIntent().getStringExtra("PhoneNUmber");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(credentials.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(APIInterface.class);


        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otp = otpPin_edtxt.getText().toString();
                if (!otp.isEmpty()) {
                    validateOtp(ApiJsonBody(phoneNumber, otp));
                }


            }
        });

    }

    //validates the otp
    private void validateOtp(JsonObject ApiBody) {
        Call<JsonObject> call = apiInterface.validate_otp(ApiBody);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i(TAG, "Validate Otp" + response.code());
                //checks if otp is successfully validated
                if(response.code()==200){
                    Log.i(TAG, "Switching to signIn Activity");
                    startSignInActivity();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //returns the raw Json Api body for the get_otp post request
    private JsonObject ApiJsonBody(String phoneNumber, String otp) {

        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObj_ = new JSONObject();
            jsonObj_.put("phone", phoneNumber);
            jsonObj_.put("otp", otp);

            //parses the Json object to Json
            gsonObject = (JsonObject) JsonParser.parseString(jsonObj_.toString());

            Log.i(TAG, "Json Object : " + gsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gsonObject;
    }

    private void startSignInActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }


}