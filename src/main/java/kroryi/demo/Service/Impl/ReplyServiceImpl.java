package kroryi.demo.Service.Impl;

import kroryi.demo.Service.ReplyService;
import kroryi.demo.domain.Board;
import kroryi.demo.domain.Reply;
import kroryi.demo.dto.PageRequestDTO;
import kroryi.demo.dto.PageResponseDTO;
import kroryi.demo.dto.ReplyDTO;
import kroryi.demo.repository.BoardRepository;
import kroryi.demo.repository.ReplyRepositroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepositroy replyRepositroy;
    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;

    @Value("${paging.range}")
    private int defaultPageRange;

    @Override
    public Long register(ReplyDTO replyDTO) {
        Board board = boardRepository.findById(replyDTO.getBno())
                .orElseThrow(() -> new IllegalArgumentException("게시물 없음"));
        Reply reply = modelMapper.map(replyDTO, Reply.class);
        reply.setBoard(board);

        Long rno = replyRepositroy.save(reply).getRno()
                .describeConstable().orElseThrow(() -> new DataIntegrityViolationException("제약조건 위반"));
        return rno;
    }

    @Override
    public ReplyDTO read(Long rno) {
        Optional<Reply> replyOptional = replyRepositroy.findById(rno);
        Reply reply = replyOptional.orElseThrow();

        return modelMapper.map(reply, ReplyDTO.class);
    }

    @Override
    public void modify(ReplyDTO replyDTO) {
        Optional<Reply> replyOptional = replyRepositroy.findById(replyDTO.getRno());
        Reply reply = replyOptional.orElseThrow();
        // 책에는 Reply.java change 메서드 추가 해야함.
        reply.change(replyDTO.getReplyText());
        replyRepositroy.save(reply);

    }

    @Override
    public void remove(Long rno) {
        replyRepositroy.deleteById(rno);
    }

    @Override
    public PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() < 0 ? 0 : pageRequestDTO.getPage(), pageRequestDTO.getSize(),
                Sort.by("rno").ascending());
        Page<Reply> result = replyRepositroy.listOfBoard(bno, pageable);
        List<ReplyDTO> dtoList =
                result.getContent().stream().map(reply -> modelMapper.map(reply, ReplyDTO.class)).collect(Collectors.toList());
        return PageResponseDTO.<ReplyDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .pageRange(defaultPageRange)
                .build();
    }
}
