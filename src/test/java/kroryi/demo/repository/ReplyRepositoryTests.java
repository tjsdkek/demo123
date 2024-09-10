package kroryi.demo.repository;

import kroryi.demo.Service.ReplyService;
import kroryi.demo.domain.Board;
import kroryi.demo.domain.Reply;
import kroryi.demo.dto.BoardListReplyCountDTO;
import kroryi.demo.dto.ReplyDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepositroy replyRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ReplyService replyService;

    @Test
    public void testInsert(){
        Long bno = 105L;
        Board board = Board.builder().bno(bno).build();

        Reply reply = Reply.builder()
                .board(board)
                .replyText("댓글")
                .replyer("replyer1")
                .build();

        replyRepository.save(reply);
    }

    @Test
    @Transactional
    public void testBoardReplies(){
        Long bno = 105L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending());
        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);
        // result 내부에는 여러가지 정보 page, size, end, start ....

        result.getContent().forEach(reply ->{
            // FetchType.LAZY로 설정되어있어 reply.getBoard()을 호출하면 게시판 정보가 쿼리된다.
            log.info("{}", reply);
            log.info("{}", reply.getBoard());
        });
    }

    @Test
    public void testSearchReplyCount(){
        String[] types = {"t", "c", "w", "d"};
        String keyword = "댓글";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info("Prev {} : next {}",result.hasPrevious(), result.hasNext());
        result.getContent().forEach(board -> log.info(board.toString()));
    }



    @Test
    public void testRegister(){
        ReplyDTO replyDTO = ReplyDTO.builder()
                .replyText("다해줬잖아")
                .replyer("신창섭")
                .bno(206L)
                .build();

        replyService.register(replyDTO);

        log.info("replyService.register ---> {}", replyDTO.toString());
    }
}
