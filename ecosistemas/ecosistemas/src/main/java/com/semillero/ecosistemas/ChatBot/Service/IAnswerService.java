package com.semillero.ecosistemas.ChatBot.Service;

import com.semillero.ecosistemas.ChatBot.Model.Answer;

import java.util.List;

public interface IAnswerService {

    //Create
    public Answer saveAnswer(Answer answer);

    //Read
    public Answer findAnswerById(Long id);

    //Get All
    public List<Answer> getAllAnswer();

    //Update
    public Answer uptadeAnswer(Long id, Answer answer);

    //Delete
    public void deleteAnswerById(Long id);
}
