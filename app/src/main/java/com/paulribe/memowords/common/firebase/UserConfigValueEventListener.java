package com.paulribe.memowords.common.firebase;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.paulribe.memowords.common.model.UserConfig;

public class UserConfigValueEventListener implements ValueEventListener {

    private final UserConfigLoadedStatus userConfigLoadedStatus;

    public UserConfigValueEventListener(UserConfigLoadedStatus userConfigLoadedStatus) {
        this.userConfigLoadedStatus = userConfigLoadedStatus;
    }
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        UserConfig userConfig = dataSnapshot.getValue(UserConfig.class);
        if(userConfig != null) {
            userConfigLoadedStatus.userConfigIsLoaded(userConfig);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        // No action to be performed.
    }
}
