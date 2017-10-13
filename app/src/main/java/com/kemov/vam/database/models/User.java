package com.kemov.vam.database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by RuanJian-GuoYong on 2017/9/30.
 */

@DatabaseTable(tableName = "T_JY_RESULT_DOC")
public class User {
    @DatabaseField(generatedId = true)
    private int userId;
    @DatabaseField(columnName = "userNum")
    private int userNum;
    @DatabaseField(columnName = "userName")
    private String userName;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserNum() {
        return userNum;
    }

    public void setUserNum(int userNum) {
        this.userNum = userNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
