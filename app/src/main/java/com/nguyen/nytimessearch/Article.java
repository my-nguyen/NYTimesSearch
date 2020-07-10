package com.nguyen.nytimessearch;

import java.io.Serializable;

/**
 * Created by My on 2/9/2016.
 * Updated by My on 7/7/2020:
 * 1. migrated to AndroidX
 * 2. replaced ButterKnife with data binding
 * 3. replaced Volley with Retrofit
 * 4. replaced LinearLayout and RelativeLayout with ConstraintLayout
 * 5. implemented MVVM
 * 6. partially implemented Dagger DI
 */
public class Article implements Serializable {
    public String webUrl;
    public String headline;
    public String thumbNail;

    @Override
    public String toString() {
        return "Article{" +
                "webUrl='" + webUrl + '\'' +
                ", headline='" + headline + '\'' +
                ", thumbNail='" + thumbNail + '\'' +
                '}';
    }
}
