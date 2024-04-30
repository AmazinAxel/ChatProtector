package com.amazinaxel.chatprotector.core;

import com.amazinaxel.chatprotector.core.cache.RepeatedMessages;

public class SharedCore {
    public RepeatedMessages repeatedMessages = new RepeatedMessages();
    public RepeatedMessages getRepeatedMessages() {
        return this.repeatedMessages;
    }

}

