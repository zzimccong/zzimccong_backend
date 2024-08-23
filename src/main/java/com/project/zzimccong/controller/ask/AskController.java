package com.project.zzimccong.controller.ask;

import com.project.zzimccong.model.dto.ask.AskDTO;
import com.project.zzimccong.model.entity.ask.Ask;
import com.project.zzimccong.service.ask.AskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ask")
public class AskController {

    private final AskService askService;

    public AskController(AskService askService) {
        this.askService = askService;
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
}
