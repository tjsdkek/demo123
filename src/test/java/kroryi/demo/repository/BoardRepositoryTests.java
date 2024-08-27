package kroryi.demo.repository;

import kroryi.demo.domain.Board;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {
    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void testInsert(){
        IntStream.rangeClosed(1,100).forEach(i -> {
            Board board = Board.builder()
                    .title("하하하" + i)
                    .content("내용" +i)
                    .writer("사용자" + (i%10))
                    .build();
            Board result = boardRepository.save(board);
            log.info("BNO: {}", result.getBno());


        });
    }

    @Test
    public void testSelect(){
        Long bno = 100L;
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();
        log.info(board.toString());
    }

    @Test
    public void testUpdate(){
        Long bno = 100L;
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();
        log.info(board.toString());
        board.change("수정한거야....","내용도 수정 했지롱.");
        //save 안하면 저장 안됨.
        boardRepository.save(board);
    }



}
