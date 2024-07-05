package com.melchakov.chuck_berry_module_3.service;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.melchakov.chuck_berry_module_3.model.*;
import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1

public class QuestionService {
    private volatile Data data=null;
    public QuestionService()throws IOException{
        Path filePath = null;
        String jsonFileName="questions.json";
        try {
            filePath=Paths.get(getClass().getClassLoader()
                    .getResource(jsonFileName).toURI());
        }catch(URISyntaxException e){
            e.printStackTrace();
            throw new IOException("URI exception in question.json",e);
        }
        String jsonContent="";
        try {
            jsonContent = Files.readString(filePath);
        } catch (IOException e) {
            throw new IOException("No such file",e);
        }
        ObjectMapper om = new ObjectMapper();

        try {
            data = om.readValue(jsonContent, Data.class);
        }catch(JsonMappingException e){
            throw new IOException("Cant map JSon file",e);
        }catch(JsonProcessingException e){
            throw new IOException("Cant process JSon file",e);
        }

    }
    public Question getQuestionById(Integer questionId)throws IllegalArgumentException{

        Question quest=null;
        for(Question question:data.getQuestions()){
            Integer integer=question.getId();
            if (integer.equals(questionId)){
                quest= question;
                break;
            }
        }
        if (quest==null)throw new IllegalArgumentException("question id:"+questionId +" was not found");

        return quest;
    }
    public List<Answer> getAnswersByQuestionId(Integer questionId)throws IllegalArgumentException{
        Question quest=getQuestionById(questionId);
        List<Answer> listAnswers=new ArrayList<>();
        if(quest.getAnswers()!=null) {
            for (Integer answerId : quest.getAnswers()) {
                listAnswers.add(getAnswerById(answerId));
            }
        }
        return listAnswers;
    }

    public Answer getAnswerById(Integer answerId)throws IllegalArgumentException{

        Answer returnAnswer=null;
        for(Answer answer:data.getAnswers()){
            Integer integer=answer.getId();
            if (integer.equals(answerId)){
                returnAnswer=answer;
                break;
            }
        }
        if (returnAnswer==null)throw new IllegalArgumentException("question id was not found");
        return returnAnswer;
    }
    public Data readFromFile()  {
        return data;
    }
}
