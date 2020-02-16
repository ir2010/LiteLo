package com.ir_sj.litelo;

import java.util.Date;

public class ChatMessage
{
    private String messageText;
    private String messageUser;
    private String messageTime, messageDate;

    public ChatMessage(String mt, String mu, String time, String date)
    {
        messageText = mt;
        messageUser = mu;
        messageTime = time;
        messageDate = date;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
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

    public String getMessageTime()
    {
        return messageTime;
    }

    public void setMessageTime(String mt)
    {
        messageTime = mt;
    }
}