package kroryi.demo.repository;

import kroryi.demo.domain.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepositroy extends JpaRepository<Reply, Long> {

    @Query("select r from Reply r where r.board.bno = :bno order by r.rno desc")
    Page<Reply> listOfBoard(Long bno, Pageable pageable);
}
