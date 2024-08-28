package kroryi.demo.repository.search;

import com.querydsl.jpa.JPQLQuery;
import kroryi.demo.domain.Board;
import kroryi.demo.domain.QBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class BoardSearchImpl extends
        QuerydslRepositorySupport
        implements BoardSearch{


    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> search(Pageable pageable) {

        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);
        query.where(board.title.contains("1"));

        this.getQuerydsl().applyPagination(pageable, query);


        List<Board> list = query.fetch();
        long count = query.fetchCount();

        return null;
    }
}
