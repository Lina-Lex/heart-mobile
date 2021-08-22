package com.example.GoandDo.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.GoandDo.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallFriendActivity extends AppCompatActivity {

    ListView listView;
    String sName[] = {"ANN YOUNG", "CHRISTOPHER SULLIVAN", "CARTER WORKMAN", "JUDY BISHOP", "LILY MILLER", "PATRICK HILL", "RUTH MYERS"};
    int images[] = {R.drawable.profile_image, R.drawable.profile_image, R.drawable.profile_image, R.drawable.profile_image, R.drawable.profile_image, R.drawable.profile_image, R.drawable.profile_image};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_friend);

        listView = findViewById(R.id.listView);

        MyAdapter adapter = new MyAdapter(this, sName, images);
        listView.setAdapter(adapter);
        listView.setDivider(null);
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String rName[];
        int rImage[];

        MyAdapter (Context c, String name[], int img[]){
            super(c, R.layout.contact_list_dummy_items, R.id.dummyProfileText, name);
            this.context = c;
            this.rName = name;
            this.rImage = img;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dummy_layout = layoutInflater.inflate(R.layout.contact_list_dummy_items, parent, false);
            CircleImageView profileImg = dummy_layout.findViewById(R.id.dummyProfileImg);
            TextView profileName = dummy_layout.findViewById(R.id.dummyProfileText);

            profileImg.setImageResource(rImage[position]);
            profileName.setText(rName[position]);

            return dummy_layout;
        }
    }
}