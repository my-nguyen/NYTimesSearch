package com.nguyen.nytimessearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by My on 2/10/2016.
 */
public class Settings implements Serializable {
    public Date beginDate;
    public String sortOrder;
    public boolean arts;
    public boolean fashionStyle;
    public boolean sports;

    @Inject
    public Settings() {
        beginDate = null;
        sortOrder = null;
        arts = false;
        fashionStyle = false;
        sports = false;
    }

    public String getBeginDate() {
        return beginDate == null ? null : beginDate.toString();
    }

    public String getSortOrder() {
        // convert the first letter from uppercase to lowercase, so that "Oldest" becomes "oldest",
        // "Newest" becomes "newest"
        return sortOrder == null ? null : (sortOrder.substring(0, 1).toLowerCase() + sortOrder.substring(1));
    }

    public String getFilterQuery() {
        if (!arts && !fashionStyle && !sports) {
            return null;
        } else {
            List<String> values = new ArrayList<>();
            if (arts) {
                values.add("\"Arts\"");
            }
            if (fashionStyle) {
                values.add("\"Fashion & Style\"");
            }
            if (sports) {
                values.add("\"Sports\"");
            }

            StringBuilder builder = new StringBuilder();
            builder.append("news_desk:(");
            builder.append(values.get(0));
            for (int i = 1; i < values.size(); i++)
                builder.append(" ").append(values.get(i));
            builder.append(")");
            return builder.toString();
        }
    }

    @Override
    public String toString() {
        return "Settings{" +
                "beginDate=" + beginDate +
                ", sortOrder='" + sortOrder + '\'' +
                ", arts=" + arts +
                ", fashionStyle=" + fashionStyle +
                ", sports=" + sports +
                '}';
    }
}
