package com.excellence.iptv.bean;

/**
 * @author maiwenwu
 * @date 2018/4/9
 */

public class Program {

    private int mProgramNum;
    private String mProgramName;
    private String mProgramTime;
    private String mEpg;

    private String mFavoritePic;
    private boolean mIsFavorite = false;

    public Program() {

    }

    public Program(int programNum, String programName, String programTime, String epg, String favoritePic, boolean isFavorite) {
        mProgramNum = programNum;
        mProgramName = programName;
        mProgramTime = programTime;
        mEpg = epg;
        mFavoritePic = favoritePic;
        mIsFavorite = isFavorite;
    }

    public int getProgramNum() {
        return mProgramNum;
    }

    public void setProgramNum(int programNum) {
        mProgramNum = programNum;
    }

    public String getProgramName() {
        return mProgramName;
    }

    public void setProgramName(String programName) {
        mProgramName = programName;
    }

    public String getProgramTime() {
        return mProgramTime;
    }

    public void setProgramTime(String programTime) {
        mProgramTime = programTime;
    }

    public String getEpg() {
        return mEpg;
    }

    public void setEpg(String epg) {
        mEpg = epg;
    }

    public String getFavoritePic() {
        return mFavoritePic;
    }

    public void setFavoritePic(String favoritePic) {
        mFavoritePic = favoritePic;
    }

    public boolean isFavorite() {
        return mIsFavorite;
    }

    public void setFavorite(boolean favorite) {
        mIsFavorite = favorite;
    }

    @Override
    public String toString() {
        return "Program{" +
                "mProgramNum=" + mProgramNum +
                ", mProgramName='" + mProgramName + '\'' +
                ", mProgramTime='" + mProgramTime + '\'' +
                ", mEpg='" + mEpg + '\'' +
                ", mFavoritePic='" + mFavoritePic + '\'' +
                ", mIsFavorite=" + mIsFavorite +
                '}';
    }
}
