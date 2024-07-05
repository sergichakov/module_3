package com.melchakov.chuck_berry_module_3.servlet;

import com.melchakov.chuck_berry_module_3.model.*;
import java.io.*;
import java.util.stream.Collectors;
//home/user/IdeaProjects/jsp_example/src/main/java/com/kupreychik/chuck_berry_jakarta/listener/AppContextListener.java
import com.melchakov.chuck_berry_module_3.service.QuestionService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "resultServlet", value = "/result")
public class ResultServlet extends HttpServlet{
    //private QuestionService questionService=null;
    public void init() {
        Logger logger = LoggerFactory.getLogger(ResultServlet.class);
        ServletContext servletContext = getServletContext();
        servletContext.setAttribute("logger",logger);
        try{
            QuestionService questionService=new QuestionService();
            servletContext.setAttribute("service",questionService);
        }catch(IOException e) {
            logger.error("cant instantiate QuestionService "+ e.getMessage());
            //Надо перейти на страницу с ошибкой
        }

    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ServletContext servletContext = getServletContext();
        QuestionService questionService=(QuestionService) servletContext.getAttribute("service");

        /*try{
            questionService=QuestionService.getInstance();
        }catch(IOException e) {

            //Надо перейти на страницу с ошибкой
        }*/

        Logger logger=(Logger) servletContext.getAttribute("logger");
        HttpSession currentSession = request.getSession();
        SessionObject sessionObject=(SessionObject)currentSession.getAttribute("session");
        if(sessionObject==null){
            response.sendRedirect("/");
            return;
        }else{

        }
        if (questionService==null)
            response.sendError(505,"cant instantiate question service");
        //System.out.println("now");
        //ServletContext servletContext = getServletContext();
        Integer pageNumber=null;
        try {
            pageNumber=Integer.parseInt(request.getParameter("page"));
        }catch(NumberFormatException e){
            response.sendError(404,"page_parameter is not Integer or No such at all");
            return;
        }

        //Data data=questionService.readFromFile();
        String questionStr="";
        Question quest=null;
        try {
            quest = questionService.getQuestionById(pageNumber);
            questionStr=quest.getQuestion();
        }catch(IllegalArgumentException e){
            response.sendError(404, "no such number page=" + pageNumber);
        }
        /*String str=data.getQuestions().stream()
                .map((quest)->{
                    System.out.println("getId:"+quest.getId());
            if(quest.getId()==(pageNumber))return quest.getQuestion();
            else{
                try {
                    response.sendError(404, "no such page number " + pageNumber);
                }catch(IOException e){
                    e.printStackTrace();
                }
                return "";
            }
                 }).findFirst().orElse("");//.collect(Collectors.joining());
        */
        if (questionStr.isEmpty()){
            try {
                response.sendError(404, "question String with id="+pageNumber+" is empty ");
            }catch(IOException e){
                e.printStackTrace();
            }
            return;
        }
        int sessionLevel=sessionObject.getCurrentLevel();
        if(pageNumber-sessionLevel>2||quest.isFailed()==false&&quest.isWin()==false)
            response.sendError(404, "ResultServlet you cheating! You set pageNumber="
                    + pageNumber+", but your level="+sessionLevel);
        /*for (Cookie cookie:request.getCookies()){
            int level=-1;
            if(cookie.getName().equals("level"))level=Integer.parseInt(cookie.getValue());
            if(pageNumber-level>2)
                response.sendError(404, "ResultServlet you cheating! You set pageNumber="
                        + pageNumber+", but your level="+level);


            //need to test cheaters
        }*/
        //response.addCookie(new Cookie("level", ""+pageNumber));
        String ipAddress = request.getHeader("X-FORWARDED-FOR");//getting ipAddress
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        String playerName=sessionObject.getName();
        if(quest.isWin())logger.info("Player "+playerName+" win! ");
        else if(quest.isFailed())logger.info("Player "+playerName+" loosed! ");
        servletContext.setAttribute("playerName",playerName);
        request.setAttribute("ipAddress",""+ipAddress);
        request.setAttribute("numberOfGames",""+sessionObject.getNumberOfGames());
        servletContext.setAttribute("result",questionStr);
        request.getRequestDispatcher("/result.jsp").include(request, response);
    }
}
