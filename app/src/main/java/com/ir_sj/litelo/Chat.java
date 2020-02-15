package com.ir_sj.litelo;

public class Chat
{
    private String chatTitle;
    private String chatCreator;
    private String chatDate;
    private String chatTime;
    private int chatStatus;
    private String userName;

    public Chat(String t, String u, String d, String time, int s, String un)
    {
        chatTitle = t;
        chatCreator = u;
        chatDate = d;
        chatTime = time;
        chatStatus = s;
        userName = un;
    }

    public Chat()
    {

    }

    public String getChatTitle()
    {
        return chatTitle;
    }

    public void setChatTitle(String mt)
    {
        chatTitle = mt;
    }

    public String getChatCreator()
    {
        return chatCreator;
    }

    public void setChatCreator(String mt)
    {
        chatCreator = mt;
    }

    public String getChatDate()
    {
        return chatDate;
    }

    public void setChatDate(String mu)
    {
        chatDate = mu;
    }

    public String getChatTime()
    {
        return chatTime;
    }

    public void setChatTime(String mt)
    {
        chatTime = mt;
    }

    public int getChatStatus()
    {
        return chatStatus;
    }

    public void setChatStatus(int mt)
    {
        chatStatus = mt;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String mt)
    {
        userName = mt;
    }


}