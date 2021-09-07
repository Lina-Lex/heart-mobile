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
import com.hbb20.CountryCodePicker;

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
    CountryCodePicker ccp;


    private final String TAG = "OnboardingActivity";
    private String phoneNumber;
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        btn_Submit = findViewById(R.id.submitOnboard_button);
        number_edtxt = findViewById(R.id.number);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        ccp.registerCarrierNumberEditText(number_edtxt);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(credentials.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(APIInterface.class);


        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = ccp.getFullNumberWithPlus();
                //checks if number is valid
                if (ccp.isValidFullNumber()) {
                    getOtp(ApiJsonBody(phoneNumber));

                    startOtpVerificationActivity();
                }

            }
        });

    }

    //navigates to OtpVerificationActivity
    private void startOtpVerificationActivity() {
        Intent intent = new Intent(this, OtpVerificationActivity.class);
        intent.putExtra("PhoneNUmber", phoneNumber);
        startActivity(intent);
    }

    //sends the Otp code to the given phoneNumber
    //params: The Json Api body of the Post request
    private void getOtp(JsonObject ApiBody) {
        Call<JsonObject> call = apiInterface.get_otp(ApiBody);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i(TAG, "get Otp" + response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
              t.printStackTrace();
            }
        });
    }

    //returns the raw Json Api body for the get_otp post request
    private JsonObject ApiJsonBody(String phoneNumber) {

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