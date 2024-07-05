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
    private volatile Data data=null;         //Нужна ленивая инициализация
    private static QuestionService questionService=null;
    public static synchronized QuestionService getInstance()throws IOException{
        if(questionService==null) {
            questionService = new QuestionService();

        }
        return questionService;
    }
    public QuestionService()throws IOException{
        Path filePath = null;
        String jsonFileName="questions.json"; //home/user/IdeaProjects/chuck_berry_module_3/src/main/resources/questions.json";
        //if (questionService!=null)return questionService;
        try {
            filePath=Paths.get(getClass().getClassLoader()
                    .getResource(jsonFileName).toURI());

        }catch(URISyntaxException e){
            e.printStackTrace();
            throw new IOException("URI exception in question.json",e);
        }
        //Path filePath = Path.of(jsonFileName);

        String jsonContent="";
        try {
            jsonContent = Files.readString(filePath);
        } catch (IOException e) {
            throw new IOException("No such file",e);
        }


        //StringBuilder jsonStringBuilder=new StringBuilder();

        /*try (BufferedReader br
                 = new BufferedReader(new InputStreamReader(
                   new FileInputStream(jsonFileName)      ))) {
            String line;
            while ((line = br.readLine()) != null) {
                jsonStringBuilder.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //надо переейти на страницу с ошибкой
        } catch (IOException e) {
            e.printStackTrace();
            //надо переейти на страницу с ошибкой
        }*/
        ObjectMapper om = new ObjectMapper();
        //Data data = null;
        try {
            data = om.readValue(jsonContent, Data.class);
        }catch(JsonMappingException e){
            //e.printStackTrace();
            throw new IOException("Cant map JSon file",e);
            //НАдо перейки на старницу с ошибкой
        }catch(JsonProcessingException e){
            //e.printStackTrace();
            throw new IOException("Cant process JSon file",e);
            //НАдо перейки на старницу с ошибкой
        }
        //this.data=data;
    }
    public Question getQuestionById(Integer questionId)throws IllegalArgumentException{
        //List<Integer> intList=null;
        Question quest=null;
        for(Question question:data.getQuestions()){
            Integer integer=question.getId();
            if (integer.equals(questionId)){
                /*intList=question.getAnswers();
                for (Answer answer:data.getAnswers()){

                }*/

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
        //заглушка надо поставить что надо

        //List<Answer> answerList=new ArrayList<>();
        Answer returnAnswer=null;
        for(Answer answer:data.getAnswers()){
            Integer integer=answer.getId();
            if (integer.equals(answerId)){
                returnAnswer=answer;
                break;
                //intList=answer.getAnswers();
                //answerList.add(answer);
                /*for (Answer answer:data.getAnswers()){

                }*/
            }
        }
        if (returnAnswer==null)throw new IllegalArgumentException("question id was not found");
        return returnAnswer;
    }
    public Data readFromFile()  {
        return data;
    }
}
