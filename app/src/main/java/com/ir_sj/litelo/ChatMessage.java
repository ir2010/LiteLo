package com.ir_sj.litelo;

import java.util.Date;

public class ChatMessage
{
    private String messageText;
    private String messageUser;
    private long messageTime;

    public ChatMessage(String mt, String mu)
    {
        messageText = mt;
        messageUser = mu;
        messageTime = new Date().getTime();
    }

    public ChatMessage()
    {

    }

    public String getMessageText()
    {
        return messageText;
    }

    public void setMessageText(String mt)
    {
        messageText = mt;
    }

    public String getMessageUser()
    {
        return messageUser;
    }

    public void setMessageUser(String mu)
    {
        messageUser = mu;
    }

    public long getMessageTime()
    {
        return messageTime;
    }

    public void setMessageTime(long mt)
    {
        messageTime = mt;
    }
}