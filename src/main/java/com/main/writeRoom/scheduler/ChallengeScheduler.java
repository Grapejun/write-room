package com.main.writeRoom.scheduler;

import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import com.main.writeRoom.domain.mapping.ChallengeStatus;
import com.main.writeRoom.service.ChallengeService.ChallengeRoutineQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChallengeScheduler {
    private final ChallengeRoutineQueryService routineQueryService;

    @Scheduled(cron = "0 0 0 * * *")  //매일 자정에 검사
    //@Scheduled(fixedRate = 60000) //10초마다 검사
    public void checkChallengeRoutine() {
        log.info("루틴 체크 작업 시작");

        List<ChallengeRoutineParticipation> routineParticipationList = routineQueryService.findByChallengeStatus(ChallengeStatus.PROGRESS);

        for (int i = 0; i < routineParticipationList.size(); i++) {
            //체크 작업 내용
            //System.out.println(routineParticipationList.get(i).getAchieveCount());
            //System.out.println(routineParticipationList.get(i).getChallengeStatus());
            check(routineParticipationList.get(i));
            //System.out.println(routineParticipationList.get(i).getAchieveCount());
            //System.out.println(routineParticipationList.get(i).getChallengeStatus());
        }
    }

    public void check(ChallengeRoutineParticipation crp) {
        if (ChronoUnit.DAYS.between(crp.getChallengeRoutine().getDeadline(), LocalDate.now()) % 7 == 0) { //오늘이 챌린지 기간의 주 마지막 날인 경우,

            if (crp.getAchieveCount() < crp.getChallengeRoutine().getTargetCount()) { //주의 마지막 날까지 목표일수를 못 채웠으므로 실패
                crp.setChallengeStatus(ChallengeStatus.FAILURE);
                crp.setStatusUpdatedAt(LocalDate.now()); //실패 날짜 기록
            }
            crp.setAchieveCount(0);  // 주의 마지막 날이니까 달성 카운트를 0으로 초기화
        }

       /* if (LocalDate.now().isEqual(crp.getChallengeRoutine().getStartDate())) {
            if (crp.getAchieveCount() < crp.getChallengeRoutine().getTargetCount()) { //주의 마지막 날까지 목표일수를 못 채웠으므로 실패
                crp.setChallengeStatus(ChallengeStatus.FAILURE);
                crp.setStatusUpdatedAt(LocalDate.now()); //실패 날짜 기록
            }
            crp.setAchieveCount(0);  // 주의 마지막 날이니까 달성 카운트를 0으로 초기화
        }*/
    }
}
