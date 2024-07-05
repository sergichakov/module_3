package com.melchakov.chuck_berry_module_3.model;


import lombok.Getter;

import java.util.List;
@lombok.Data
@Getter
public class Data{
    private List<Question> questions;
    private List<Answer> answers;

}