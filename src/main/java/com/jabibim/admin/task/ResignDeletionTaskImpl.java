package com.jabibim.admin.task;

import com.jabibim.admin.domain.Student;
import com.jabibim.admin.service.ResignedStudentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResignDeletionTaskImpl implements com.jabibim.admin.service.ResignDeletionTask {

  private static final Logger logger = LoggerFactory.getLogger(ResignDeletionTaskImpl.class);

  private final ResignedStudentService resignedStudentService;

  //@Scheduled(fixedDelay = 1000)
  public void test() throws Exception{
    logger.info("test");
  }

  // 매일 00 : 00 에 삭제 프로세스 시작.
  @Scheduled(cron="* * 0 * * *")
  public void deleteResignStudentData() throws Exception {
    logger.info("정보 보관 기한 넘긴 데이터 삭제 프로세스 시작");

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 삭제 예정일이 현재 시간보다 나중인 학생 리스트를 가져온다.
    List<Student> deletionList = resignedStudentService.getResignedStudentData(LocalDateTime.now()
                                                                                  .format(formatter));

    if (deletionList.isEmpty()) {
      logger.info("삭제할 학생 정보 없음");
      return;
    }

    // 가져온 학생들 하나씩 삭제 실행
    for (Student student : deletionList) {
      int result = resignedStudentService.deleteResignedStudentData(student);

      if (result > 0) {
        logger.info("학생 정보 삭제" + student.getStudentId());
      }
    }


  }



}
