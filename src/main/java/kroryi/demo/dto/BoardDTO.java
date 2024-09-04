package kroryi.demo.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link kroryi.demo.domain.Board}
 */
// @Value 어노테이션을 사용하면 필드이 private가 않되는 현상 때문에 규칙 위반
// Config설정에서   .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class BoardDTO implements Serializable {
    private LocalDateTime registerDate;
    private LocalDateTime modifyDate;
    private Long bno;
    private String title;
    private String content;
    private String writer;
    private String address;
}