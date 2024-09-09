package kroryi.demo.controller;

import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import kroryi.demo.Service.ReplyService;
import kroryi.demo.dto.PageRequestDTO;
import kroryi.demo.dto.PageResponseDTO;
import kroryi.demo.dto.ReplyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @ApiOperation(value="Replies POST", notes ="POST 방식으로 댓글 등록")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> register(@Valid @RequestBody ReplyDTO replyDTO,
                                                        BindingResult bindingResult) throws BindException {

        log.info("replyDTO --->{}", replyDTO);

        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        // 백엔드에서 등록이 완료된 후에 프론트앤드로 데이터 응답을 할때
        // 응답코드, 응답메세지, 등록된 결과물 등을 전송해서
        // 프론트에서 활용할 수 있다.
        Map<String, Long> resultmap = new HashMap<>();
        Long rno = replyService.register(replyDTO);
        resultmap.put("rno", 105L);

        return resultmap;
    }

    @ApiOperation(value="Replies of Board", notes ="GET 방식으로 특정 게시물의 댓글 등록")
    @GetMapping(value = "/list/{bno}")
    public PageResponseDTO<ReplyDTO> getList(@PathVariable("bno") Long bno, PageRequestDTO pageRequestDTO) {

        PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfBoard(bno, pageRequestDTO);

        return responseDTO;
    }

    @ApiOperation(value="Replies of Reply", notes ="GET 방식으로 특정 게시물의 댓글 조회")
    @GetMapping(value = "/{rno}")
    public ReplyDTO getReplyDTO(@PathVariable("rno") Long rno) {

        return replyService.read(rno);
    }

    @ApiOperation(value = "Delete Reply", notes = "DELETE 방식으로 특정 댓글 삭제")
    @DeleteMapping("/{rno}")
    public Map<String, String> remove(@PathVariable("rno") Long rno) {

        replyService.remove(rno);
        Map<String, String> resultmap = new HashMap<>();
        resultmap.put("success_code", "200");
        resultmap.put("msg","빈 댓글을 삭제했습니다.");
        resultmap.put("rno", String.valueOf(rno));

        return resultmap;
    }

    @ApiOperation(value = "Modify Reply", notes = "PUT 방식으로 특정 댓글 수정")
    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> remove(@PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO) {
        replyDTO.setRno(rno);
        // 수정은 Reply 엔티티에 change 메서드 부분에 replyText만 수정하게 되어 있다.
        // replyer까지 수정하려면 change 메서드를 수정하면 된다.
        replyService.modify(replyDTO);
        Map<String, String> resultmap = new HashMap<>();
        resultmap.put("success_code", "200");
        resultmap.put("rno", String.valueOf(rno));
        return resultmap;
    }
}
