package com.norez.myclass.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.norez.myclass.R;
import com.norez.myclass.activities.LessonActivity;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;

import java.net.MalformedURLException;
import java.net.URL;

public class ConferenceFragment extends Fragment {
    private Button connectButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_conference, container, false);
        connectButton = v.findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL server;
                try{
                    server =new URL("https://meet.jit.si");
                    JitsiMeetConferenceOptions defaultOptions=
                            new JitsiMeetConferenceOptions.Builder()
                                    .setServerURL(server)
                                    .build();
                    JitsiMeet.setDefaultConferenceOptions(defaultOptions);

                }catch (MalformedURLException e){
                    e.printStackTrace();
                }
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("lessons").whereEqualTo("name", LessonActivity.title).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document: task.getResult()) {
                            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                                    .setRoom(document.getId())
                                    .build();
                            JitsiMeetActivity.launch(getActivity(), options);
                        }
                    }
                });
            }
        });
        return v;
    }
}