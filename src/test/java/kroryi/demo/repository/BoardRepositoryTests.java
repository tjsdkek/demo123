package kroryi.demo.repository;

import kroryi.demo.domain.Board;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {
    @Autowired
    private BoardRepository boardRepository;
//
//    @Test
//    public void testInsert(){
//        IntStream.rangeClosed(1,100).forEach(i -> {
//            Board board = Board.builder()
//                    .title("하하하" + i)
//                    .content("내용" +i)
//                    .writer("사용자" + (i%10))
//                    .build();
//            Board result = boardRepository.save(board);
//            log.info("BNO: {}", result.getBno());
//
//
//        });
//    }
//
//    @Test
//    public void testSelect(){
//        Long bno = 100L;
//        Optional<Board> result = boardRepository.findById(bno);
//        Board board = result.orElseThrow();
//        log.info(board.toString());
//    }
//
//    @Test
//    public void testUpdate(){
//        Long bno = 100L;
//        Optional<Board> result = boardRepository.findById(bno);
//        Board board = result.orElseThrow();
//        log.info(board.toString());
//        board.change("수정한거야....","내용도 수정 했지롱.");
//        //save 안하면 저장 안됨.
//        boardRepository.save(board);
//    }
//
//    @Test
//    public void testDelete(){
//        Long bno = 1L;
//        boardRepository.deleteById(bno);
//    }
//
//    @Test
//    public void testPaging(){
//        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
////        Page<Board> result = boardRepository.findAll(pageable);
////        log.info("전체 게시물 수 {}", result.getTotalElements());
////        log.info("전체 페이지 수 {}", result.getTotalPages());
////        log.info("페이지 번호{}", result.getNumber());
////        log.info("한페이지당 수 {}", result.getSize());
////        log.info("한페이지당 수 {}", result.getContent());
////        List<Board> boards = result.getContent();
////        boards.forEach(board -> {
////            log.info(board);
////        });
//
//        Page<Board> resultKeyword = boardRepository.findKeyword("하8",pageable);
//        log.info("전체 게시물 수 {}", resultKeyword.getTotalElements());
//        log.info("전체 페이지 수 {}", resultKeyword.getTotalPages());
//        log.info("페이지 번호{}", resultKeyword.getNumber());
//        log.info("한페이지당 수 {}", resultKeyword.getSize());
//        log.info("한페이지당 수 {}", resultKeyword.getContent());
//        List<Board> boards = resultKeyword.getContent();
//        boards.forEach(board -> {
//            log.info(board);
//        });
//    }

    @Test
    public void testSearch(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
        boardRepository.search(pageable);

    }


}
