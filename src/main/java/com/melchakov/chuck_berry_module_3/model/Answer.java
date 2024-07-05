package com.melchakov.chuck_berry_module_3.model;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class Answer{
    private int id;
    private String answer;
    private int nextFrame;
}
