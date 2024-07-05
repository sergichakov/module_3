package com.melchakov.chuck_berry_module_3.model;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class SessionObject
{
    private int currentLevel;
    private String name;
    private int numberOfGames;
    private String sessionId;
}
