package com.melchakov.chuck_berry_module_3.model;
import lombok.Data;
import java.util.List;
@Data
public class Question{
    private int id;
    private String question;
    private List<Integer> answers;
    private boolean failed;
    private boolean win;
}