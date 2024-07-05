import com.fasterxml.jackson.databind.ObjectMapper;
import com.melchakov.chuck_berry_module_3.model.Answer;
import com.melchakov.chuck_berry_module_3.model.Data;
import com.melchakov.chuck_berry_module_3.service.QuestionService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;


public class QuestionServiceTest {
    Data data=null;

    @BeforeEach
    @SneakyThrows
    void loadJSONBeforeEach(){
        Path filePath = null;
        String jsonFileName="questions.json";
        filePath= Paths.get(getClass().getClassLoader()
                .getResource(jsonFileName).toURI());
        String jsonContent = Files.readString(filePath);
        ObjectMapper om = new ObjectMapper();
        data = om.readValue(jsonContent, Data.class);

    }
    @Test
    @SneakyThrows
    void getQuestionById_shouldReturnEqualQuestionString(){
        QuestionService qService=new QuestionService();
        String quest=qService.getQuestionById(1).getQuestion();
        String expected=data.getQuestions().get(0).getQuestion();
        assertEquals(expected,quest);
    }

    @Test
    @SneakyThrows
    void getQuestionById_shouldReturnEqualAnswerIdIntegers(){
        QuestionService qService=new QuestionService();
        Integer answ1=qService.getQuestionById(1).getAnswers().get(0);
        Integer answ2=qService.getQuestionById(1).getAnswers().get(1);
        Integer expected1=data.getQuestions().get(0).getAnswers().get(0);
        Integer expected2=data.getQuestions().get(0).getAnswers().get(1);
        assertEquals(expected1,answ1);
        assertEquals(expected2,answ2);
    }
    @Test
    @SneakyThrows
    void getAnswerById_shouldReturnEqualAnswerString(){
        QuestionService qService=new QuestionService();
        String answer=qService.getAnswerById(1).getAnswer();
        String expected=data.getAnswers().get(0).getAnswer();
        assertEquals(expected,answer);
    }
    @Test
    @SneakyThrows
    void getAnswerById_shouldReturnEqualAnswerNextFrameInteger(){
        QuestionService qService=new QuestionService();
        Integer frame=qService.getAnswerById(1).getNextFrame();
        Integer expected=data.getAnswers().get(0).getNextFrame();
        assertEquals(expected,frame);
    }
    @Test
    @SneakyThrows
    void getAnswersByQuestionId_shouldReturnEqualListOfAnswers(){
        QuestionService qService=new QuestionService();
        Answer answer1=qService.getAnswersByQuestionId(1).get(0);
        Answer answer2=qService.getAnswersByQuestionId(1).get(1);
        Answer expected1=data.getAnswers().get(0);
        Answer expected2=data.getAnswers().get(1);
        assertEquals(expected1,answer1);
        assertEquals(expected2,answer2);
    }
}
