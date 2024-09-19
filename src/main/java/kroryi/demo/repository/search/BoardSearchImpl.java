package kroryi.demo.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kroryi.demo.domain.Board;
import kroryi.demo.domain.QBoard;
import kroryi.demo.domain.QReply;
import kroryi.demo.dto.BoardImageDTO;
import kroryi.demo.dto.BoardListAllDTO;
import kroryi.demo.dto.BoardListReplyCountDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.collect;

@Log4j2
public class BoardSearchImpl extends
        QuerydslRepositorySupport
        implements BoardSearch {

    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> search(Pageable pageable) {

        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);

        BooleanBuilder builder = new BooleanBuilder();
        // title = '1'
        builder.or(board.title.contains("1"));
        // or 연결
        // content = '11'
        builder.or(board.content.contains("11"));
        // where ( title ='1' or content='11')
        query.where(builder);

        // and 연결
        // where (      or     ) and bno > 10
        query.where(board.bno.gt(10L));

        // select * from board where ( title = '1' or content = '11 ) and bno > 10;
        // pageable 0, 10

        this.getQuerydsl().applyPagination(pageable, query);
        // select * from board where ( title = '1' or content = '11 ) and bno > 10 limit ? , ?;

        List<Board> list = query.fetch();
        System.out.println("--------->" + list);

        long count = query.fetchCount();

        return null;
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);

        if ((types != null) && (types.length > 0) && keyword != null) {
            BooleanBuilder builder = new BooleanBuilder();
            for (String type : types) {
                switch (type) {
                    case "t":
                        builder.or(board.title.like('%' + keyword + '%'));
                        break;
                    case "c":
                        builder.or(board.content.like('%' + keyword + '%'));
                        break;
                    case "w":
                        builder.or(board.writer.like('%' + keyword + '%'));
                        break;
                    case "tc":
                        builder.or(board.title.like('%' + keyword + '%'));
                        builder.or(board.content.like('%' + keyword + '%'));
                        break;
                    case "tcw":
                        builder.or(board.title.like('%' + keyword + '%'));
                        builder.or(board.content.like('%' + keyword + '%'));
                        builder.or(board.writer.like('%' + keyword + '%'));
                        break;

                }
            }
            query.where(builder);
        }

        query.where(board.bno.gt(0L));

        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();
        long count = query.fetchCount();

        Page<Board> boardPage = new PageImpl<>(list, pageable, count);

        return boardPage;
    }

    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {
        // Querydsl을 사용하려면 QEntity
        // mvn clean install -> target Q파일들이 생긴다. 생성경로는 따로 수정할 수 있다.
        // QEntity Entity 등록된 디렉토리로 복사해서 사용
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        // 복잡한 커리문 주로 조인문 작성에 사용
        // select * from board
        // left join repley where board.bno = reply.board_bno
        JPQLQuery<Board> query = from(board);
        query.leftJoin(reply).on(reply.board.eq(board));
        query.groupBy(board);

        if ((types != null) && (types.length > 0) && keyword != null) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.like(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.like(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.like(keyword));
                        break;
                    case "d":
                        booleanBuilder.or(reply.replyText.contains(keyword));
                }
            }
            query.where(booleanBuilder);
        }
        query.where(board.bno.gt(0L));

        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(
                Projections.bean(BoardListReplyCountDTO.class,
                        board.bno,
                        board.title,
                        board.writer,
                        board.regDate,
                        reply.count().as("replyCount")
                )
        );
        this.getQuerydsl().applyPagination(pageable, dtoQuery);
        List<BoardListReplyCountDTO> dtolist = dtoQuery.fetch();

        long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtolist, pageable, count);
    }

    @Override
    public Page<BoardListAllDTO> searchWithAllReplyCount(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

//        // select * from board left join reply on reply.board_id = board.id
//        JPQLQuery<Board> boardJPQLQuery = from(board);
//        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board));
//        boardJPQLQuery.groupBy(board);
//        getQuerydsl().applyPagination(pageable, boardJPQLQuery);
//
//        // countDistinct는 중복을 제거하고 난 후의 개수
//        JPQLQuery<Tuple> tupleJPQLQuery = boardJPQLQuery.select(board, reply.countDistinct());
//        List<Tuple> tupleList = tupleJPQLQuery.fetch();
        BooleanBuilder booleanbuilder = new BooleanBuilder();

        if ((types != null) && (types.length > 0) && keyword != null) {
            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanbuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanbuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanbuilder.or(board.writer.contains(keyword));
                        break;
                    case "d":
                        booleanbuilder.or(reply.replyText.contains(keyword));
                }
            }
        }

        List<Tuple> tupleList = getQuerydsl()
                        .applyPagination(pageable, from(board)
                        .leftJoin(reply).on(reply.board.eq(board))
                        .groupBy(board)
                        .where(booleanbuilder)
                        .select(board, reply.count()))
                .fetch();

        log.info("Tuple --> {}", tupleList);

        List<BoardListAllDTO> dtolist = tupleList.stream().map(tuple -> {
            Board board1 = (Board) tuple.get(board);
            long replyCount = tuple.get(1, Long.class);

            BoardListAllDTO dto = BoardListAllDTO.builder()
                    .bno(board1.getBno())
                    .title(board1.getTitle())
                    .writer(board1.getWriter())
                    .regDate(board1.getRegDate())
                    .replyCount(replyCount)
                    .build();

            List<BoardImageDTO> imageDTOS = board1.getImagesSet().stream().sorted()
                    .map(boardImage ->
                            BoardImageDTO.builder()
                                    .uuid(boardImage.getUuid())
                                    .fileName(boardImage.getFileName())
                                    .ord(boardImage.getOrd())
                                    .build()
                    ).collect(Collectors.toList());
            dto.setBoardImages(imageDTOS);
            return dto;
        }).collect(Collectors.toList());

        long totalCount = tupleList.size();

        return new PageImpl<>(dtolist, pageable, totalCount);
    }
}
