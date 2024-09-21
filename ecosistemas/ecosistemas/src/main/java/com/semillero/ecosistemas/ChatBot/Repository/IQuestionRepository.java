package com.semillero.ecosistemas.ChatBot.Repository;

import com.semillero.ecosistemas.ChatBot.Model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IQuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCategoryId(Long categoryId);
}
