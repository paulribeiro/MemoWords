package com.paulribe.memowords.common.firebase;

import com.paulribe.memowords.common.model.UserConfig;

public interface UserConfigLoadedStatus {
    void userConfigIsLoaded(UserConfig userConfig);

}
