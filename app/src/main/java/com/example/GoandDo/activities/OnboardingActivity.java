package com.example.GoandDo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    TextView goAndDo_tv;
    CountryCodePicker ccp;
    TextView checkIcon_tv;


    private final String TAG = "OnboardingActivity";
    private String phoneNumber;
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        btn_Submit = findViewById(R.id.submitOnboard_button);
        goAndDo_tv = findViewById(R.id.go_and_do);
        checkIcon_tv = findViewById(R.id.check_icon);
        number_edtxt = findViewById(R.id.number);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        ccp.registerCarrierNumberEditText(number_edtxt);

        styleGoandDo_textView();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(credentials.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(APIInterface.class);


        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checks if number is valid
                if (ccp.isValidFullNumber()) {
                    phoneNumber = ccp.getFullNumberWithPlus();
                    getOtp(ApiJsonBody(phoneNumber));

                    startOtpVerificationActivity();
                }

            }
        });


        ccp.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                if(isValidNumber){
                    //displays the check icon whenever the phone number is valid
                    checkIcon_tv.setVisibility(View.VISIBLE);
                }
                else {
                    checkIcon_tv.setVisibility(View.INVISIBLE);
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

    //This method is for styling the GoandGo TextView
    private void styleGoandDo_textView(){
        String text = getResources().getString(R.string.go_and_do);

        SpannableString ss = new SpannableString(text);

        //specifies the colors to be used for the text color
        ForegroundColorSpan fcs_DarkPastelGreen = new ForegroundColorSpan(getResources().getColor(R.color.dark_pastel_green));
        ForegroundColorSpan fcs_FreeSpeechBlue =  new ForegroundColorSpan(getResources().getColor(R.color.free_speech_blue));

        //Changes the color of the text to which the span is attached.
        ss.setSpan(fcs_DarkPastelGreen, 0,2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(fcs_FreeSpeechBlue, 3,9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        goAndDo_tv.setText(ss);
    }

}