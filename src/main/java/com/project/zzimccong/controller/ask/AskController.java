package com.project.zzimccong.controller.ask;

import com.project.zzimccong.model.dto.ask.AnswerDTO;
import com.project.zzimccong.model.dto.ask.AskDTO;
import com.project.zzimccong.model.entity.ask.Ask;
import com.project.zzimccong.service.ask.AnswerService;
import com.project.zzimccong.service.ask.AskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ask")
@Slf4j
public class AskController {

    private final AskService askService;
    private final AnswerService answerService;

    public AskController(AskService askService, AnswerService answerService) {
        this.askService = askService;
        this.answerService = answerService;
    }

    @PostMapping
    public Ask createAsk(@RequestBody AskDTO askDTO) {
        return askService.createAsk(askDTO);
    }

    @GetMapping("/user")
    public List<Ask> getAsksByUser(@RequestParam Integer id, @RequestParam String role) {
        return askService.getAsksByUser(id, role);
    }

    @GetMapping("/{id}")
    public Ask getAskById(@PathVariable Integer id) {
        return askService.getAskById(id);
    }

    @GetMapping("/all")
    public List<Ask> getAllAsks() {
        return askService.getAllAsks();
    }

    @PostMapping("/Answer")
    public void insertAnswer(@RequestBody AnswerDTO answerDTO) {
        answerService.insertAnswer(answerDTO);
    }

    @GetMapping("/Answer/{id}")
    private ResponseEntity<AnswerDTO> getAnswerById(@PathVariable Integer id) {
        AnswerDTO answerDTO = answerService.getAnswerByAskId(id);
        return ResponseEntity.ok(answerDTO);
    }
}
