package kroryi.demo.controller;

import io.swagger.annotations.ApiOperation;
import kroryi.demo.dto.UploadFileDTO;
import kroryi.demo.dto.UploadResultDTO;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@Log4j2
public class UpDownController {

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @ApiOperation(value = "Upload POST", notes = "POST 방식으로 파일등록")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> upload(UploadFileDTO uploadFileDTO){
        log.info(uploadFileDTO);
        log.info("/upload --> {}", uploadPath);

        if(uploadFileDTO.getFiles() != null && !uploadFileDTO.getFiles().isEmpty()) {

            final List<UploadResultDTO> list = new ArrayList<>();
            uploadFileDTO.getFiles().forEach(file -> {
                String originalFilename = file.getOriginalFilename();
                String uuid = UUID.randomUUID().toString();

                // originalFilename = "아이유.jpeg"
                // savePath = "/home/work/IdeProject/demo7/upload/ea12343-df234-564_아이유.jpeg"
                Path savePath = Paths.get(uploadPath, uuid + "_" + originalFilename);
                boolean image = false;

                try {
                    // 실제 서버 하드디스크에서 파일을 저장하는곳
                    file.transferTo(savePath);
                    if(Files.probeContentType(savePath).startsWith("image")) {
                        image = true;
                        File thumbFile = new File(uploadPath, "S_" + uuid + "_" + originalFilename);
                        Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
                list.add(UploadResultDTO.builder()
                        .uuid(uuid)
                        .fileName(originalFilename)
                        .img(image)
                        .build());
            });
            return list;
        }
        return null;
    }

    @ApiOperation(value = "view 파일", notes = "GET 방식으로 첨부파일 조회")
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName){
        // File.separator은 / 디렉토리 구분자
        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
        // /home/work/IdeaProject/demo7/demo/upload/아이유.jpg
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try{
            // resource.getFile().toPath() 호출하면
            // " /home/work/IdeaProject/demo7/demo/upload/ea12343-df234-564_아이유.jpeg"
            // Files.probeContentType("/home/work/IdeaProject/demo7/upload/ea12343-df234-564_아이유.jpeg"
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @ApiOperation(value = "remove 파일", notes = "DELECT 방식으로 파일 삭제")
    @DeleteMapping("/remove/{fileName}")
    public Map<String, Boolean> removeFile(@PathVariable String fileName){
        // File.separator은 / 디렉토리 구분자
        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
        String resourceName = resource.getFilename();

        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;

        try{
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();

            if(contentType.startsWith("image")) {
                File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);

                thumbnailFile.delete();
            }

        }catch (Exception e){
            log.error(e.getMessage());
        }
        resultMap.put("result", removed);
        // resultMap에 추가로 응답 메세지를 만들어 넣을 수 있다.
//        resultMap.put("code", "200");

        return resultMap;
    }
}
