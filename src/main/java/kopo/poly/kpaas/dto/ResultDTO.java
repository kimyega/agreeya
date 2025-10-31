package kopo.poly.kpaas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 공통 응답 DTO
     * - result: 처리 결과 (1=성공, 0=실패, -1=에러)
     * - msg   : 응답 메시지
     * - data  : 추가 데이터 (문자열 형태, 필요 없으면 null)
     */

    private int result;    // 처리 결과 코드
    private String msg;    // 메시지
    private String data;   // 추가 데이터 (없으면 null)

}
