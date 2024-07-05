package com.melchakov.chuck_berry_module_3.servlet;

import java.io.*;
import java.util.List;

import com.melchakov.chuck_berry_module_3.model.Answer;
import com.melchakov.chuck_berry_module_3.model.Data;
import com.melchakov.chuck_berry_module_3.model.Question;
import com.melchakov.chuck_berry_module_3.model.SessionObject;
import com.melchakov.chuck_berry_module_3.service.QuestionService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "questionServlet", value = "/question")
public class QuestionServlet extends HttpServlet {
    //private String message;
    //private QuestionService questionService=null;

    public void init() {
        Logger logger = LoggerFactory.getLogger(QuestionServlet.class);
        ServletContext servletContext = getServletContext();
        servletContext.setAttribute("logger",logger);
        try{
            QuestionService questionService=new QuestionService();
            //ServletContext servletContext = getServletContext();
            servletContext.setAttribute("service",questionService);
        }catch(IOException e) {
            logger.error("cant instantiate QuestionService "+ e.getMessage());
        //Надо перейти на страницу с ошибкой
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException , ServletException{

        /*try{
            questionService=QuestionService.getInstance();

        }catch(IOException e) {
            //Надо перейти на страницу с ошибкой
        }*/

        //Data data=null;
        //data = questionService.readFromFile();
        ServletContext servletContext = getServletContext();
        Logger logger=(Logger) servletContext.getAttribute("logger");
        QuestionService questionService=(QuestionService) servletContext.getAttribute("service");
        if (questionService==null)
            response.sendError(505,"cant evaluate question service");
        Integer pageNumber=null;
        try {
            pageNumber=Integer.parseInt(request.getParameter("page"));
        }catch(NumberFormatException e){
            response.sendError(404,"page_parameter is not Integer or No such at all");
            return;
        }
        HttpSession currentSession = request.getSession();
        SessionObject sessionObject=(SessionObject)currentSession.getAttribute("session");
        if(sessionObject==null){
            response.sendRedirect("/");
            return;
        }else{

        }


        String ipAddress = request.getHeader("X-FORWARDED-FOR");//getting ipAddress
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        request.setAttribute("ipAddress",""+ipAddress);
        request.setAttribute("numberOfGames",""+sessionObject.getNumberOfGames());
        String questionStr="";
        Question quest =null;

        try {
            quest = questionService.getQuestionById(pageNumber);
            questionStr=quest.getQuestion();
            //for (Integer integer:quest.getAnswers()){
                List<Answer> answerList= questionService.getAnswersByQuestionId(pageNumber);
                if (answerList.isEmpty()){
                    sessionObject.setNumberOfGames(sessionObject.getNumberOfGames()+1);

                    response.sendRedirect("/result?page="+pageNumber);
                    return;
                    //getServletContext().getRequestDispatcher("/result.jsp").forward(request, response);
                }
                Answer answer1=answerList.get(0);
                Answer answer2=answerList.get(1);
                request.setAttribute("answer_1",answer1.getAnswer());
                request.setAttribute("answer_2",answer2.getAnswer());
                request.setAttribute("answer_1_link","question?page="+questionService.getQuestionById(answer1.getNextFrame()).getId());
                request.setAttribute("answer_2_link","question?page="+questionService.getQuestionById(answer2.getNextFrame()).getId());

            //}
        }catch(IllegalArgumentException e){
            response.sendError(404, "no such number page=" + pageNumber);
        }


        /*ServletContext servletContext = getServletContext();
        servletContext.setAttribute("name", "Tom");

        try {

            if (request.getAttribute("question")==null) {
                request.setAttribute("question", data.getQuestions().get(0).getQuestion());
                request.setAttribute("answer_1", data.getQuestions().get(0).getAnswers().get(0));
                request.setAttribute("answer_2", data.getQuestions().get(0).getAnswers().get(1));
                request.getRequestDispatcher("/question").forward(request, response);
            }

        }catch(ServletException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }*/
        if (questionStr.isEmpty()){
            try {
                response.sendError(404, "question String with id="+pageNumber+" is empty ");
            }catch(IOException e){
                e.printStackTrace();
            }
            return;
        }
        int sessionLevel=sessionObject.getCurrentLevel();
        String playerName=sessionObject.getName();
        servletContext.setAttribute("playerName",playerName);
        if(pageNumber-sessionLevel>2) {
            response.sendError(404, "QuestionServlet you cheating! You set pageNumber="
                    + pageNumber + ", but your level=" + sessionLevel);
            logger.warn("Player "+ playerName+" is cheater. He set pageNumber="+pageNumber+" with Level="+sessionLevel);
        }

        /*for (Cookie cookie:request.getCookies()){
            int level=-1;
            System.out.println("level="+cookie.getName());
            if(cookie.getName().equals("level"))level=Integer.parseInt(cookie.getValue());
            System.out.println("levelnew="+level);
            if(pageNumber-level>2)
                response.sendError(404, "QuestionServlet you cheating! You set pageNumber="
                        + pageNumber+", but your level="+level);

            //need to test cheaters
            System.out.println("number="+pageNumber+" level="+level);
        }*/

        sessionObject.setCurrentLevel(pageNumber);
        //response.addCookie(new Cookie("level", ""+pageNumber));
        servletContext.setAttribute("question",questionStr);
        request.getRequestDispatcher("/question.jsp").include(request, response);
    }

    public void destroy() {
    }
}