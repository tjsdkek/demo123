package kroryi.demo.repository;

import kroryi.demo.Service.BoardService;
import kroryi.demo.dto.BoardDTO;
import kroryi.demo.dto.PageRequestDTO;
import kroryi.demo.dto.PageResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService service;

    @Test
    public void testRegister(){

        log.info("register-> {}", service.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("게시판 첫 글")
                .content("내용도 첫 내용")
                .writer("김유신")
                .build();

        Long bno = service.register(boardDTO);
        log.info("board 게시물 등록-> {}", bno);

    }

    @Test
    public void testModify(){
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(301L)
                .title("게시판 첫 글-------------")
                .content("내용도 첫 내용------------")
                .writer("김유신")
                .build();
        service.modify(boardDTO);
    }

    @Test
    public void testRemove(){
        service.remove(300L);
    }

    @Test
    public void pageTest(){

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("내용")
                .page(1)
                .size(10)
                .build();


        PageResponseDTO<BoardDTO> responseDTO = service.list(pageRequestDTO);

        log.info(responseDTO);
    }

}
