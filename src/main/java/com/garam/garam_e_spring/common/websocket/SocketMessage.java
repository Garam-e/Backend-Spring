package com.garam.garam_e_spring.common.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocketMessage {

    private String sender;
    private String channelId;
    private Object content;

    public void setSender(String sender) {
        this.sender = sender;
    }
}
