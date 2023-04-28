package com.example.toptop.ui.me;

import static android.widget.Toast.LENGTH_SHORT;
import static androidx.databinding.DataBindingUtil.setContentView;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.toptop.Funk;
import com.example.toptop.MainActivity;
import com.example.toptop.R;
import com.example.toptop.SettingActivity;
import com.example.toptop.chat.ChatMessage;
import com.example.toptop.ui.home.ProfileDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class MeFragment extends Fragment {

    private ImageView settingImg;
    ImageView avatar, premium_icon;
    TextView name, username, videos, followers, following, likes;
    Button follow, btChat;
    String urlAvatar;
    int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        super.onViewCreated(view, savedInstanceState);
        settingImg = view.findViewById(R.id.settings);
        settingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });

        avatar = view.findViewById(R.id.profile_image);
        name = view.findViewById(R.id.profile_name_);
        premium_icon = view.findViewById(R.id.premium_icon);
        username = view.findViewById(R.id.profile_username);
        videos = view.findViewById(R.id.profile_videos);
        followers = view.findViewById(R.id.profile_followers);
        following = view.findViewById(R.id.profile_following);
        likes = view.findViewById(R.id.profile_likes);
        follow = view.findViewById(R.id.btn_follow);
        btChat = view.findViewById(R.id.btChat);
        userId = Funk.get_user(getContext()).getUid();

        getProfile();
        return view;
    }

    private void getProfile() {

        String url = "https://soc.q2k.dev/api/" + userId + "/info";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject user = response.getJSONObject("user");
                            urlAvatar = user.getString("avatar");
                            Glide.with(getActivity()).load(urlAvatar).into(avatar);
                            name.setText(user.getString("name"));
                            username.setText(user.getString("username"));
                            videos.setText(user.getString("videos"));
                            followers.setText(user.getString("followers"));
                            following.setText(user.getString("following"));
                            likes.setText(user.getString("likes"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + Funk.get_token(getContext()));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
}