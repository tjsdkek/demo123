package kroryi.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;

@Getter
@ToString
public class PageResponseDTO<E> {

    private int page;
    private int size;
    private int total;
    private int start;
    private int end;
    private boolean prev;
    private boolean next;

    private List<E> dtoList;

    private final int pageRange;

    @Value("${paging.range}")
    private int defaultPageRange;


//    Board board = Board.builder()
//            .title("하하하" + i)
//            .content("내용" +i)
//            .writer("사용자" + (i%10))
//            .build();
//     @Builder(builderMethodName = "withAll")
//    Board board = Board.withAll()
//            .title("하하하" + i)
//            .content("내용" +i)
//            .writer("사용자" + (i%10))
//            .build();


    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> dtoList, int total, int pageRange) {
        if(total <= 0) throw new IllegalArgumentException("total <= 0");

        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        this.pageRange = pageRange == -1 ? defaultPageRange : pageRange;

        if(this.pageRange <= 0 ){
            throw new IllegalArgumentException("pageRange <= 0");
        }
        if (this.size <= 0) {
            throw new IllegalArgumentException("size must be greater than 0");
        }

        this.end = (int)(Math.ceil(this.page / this.pageRange)) * this.pageRange;
        this.start = this.end - (this.pageRange - 1);

        int last = (int)(Math.ceil((total/(double)size)));
        this.end = end > last ? last : end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }

}
