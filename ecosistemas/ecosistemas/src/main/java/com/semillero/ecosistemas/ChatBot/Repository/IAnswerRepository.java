package com.semillero.ecosistemas.ChatBot.Repository;

import com.semillero.ecosistemas.ChatBot.Model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAnswerRepository extends JpaRepository<Answer, Long> {
    Answer findByQuestionId(Long questionId);
}