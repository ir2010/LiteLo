package com.ir_sj.litelo;

public class Chat
{
    private String chatTitle;
    private String chatUser;
    private String chatDate;
    private String chatTime;

    public Chat(String t, String u, String d, String time)
    {
        chatTitle = t;
        chatUser = u;
        chatDate = d;
        chatTime = time;
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
}