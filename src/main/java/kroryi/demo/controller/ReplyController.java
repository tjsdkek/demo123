package kroryi.demo.controller;

import jakarta.validation.Valid;
import kroryi.demo.dto.ReplyDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/replies")
@Log4j2
public class ReplyController {
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> register(@Valid @RequestBody ReplyDTO replyDTO,
                                                        BindingResult bindingResult) throws BindException {
        log.info("replyDTO --->{}", replyDTO);

        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        Map<String, Long> resultmap = new HashMap<>();
        resultmap.put("rno", 105L);

        return resultmap;
    }
}
