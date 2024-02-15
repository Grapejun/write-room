package com.main.writeRoom.scheduler;

import com.main.writeRoom.domain.mapping.ChallengeGoalsParticipation;
import com.main.writeRoom.domain.mapping.ChallengeRoutineParticipation;
import com.main.writeRoom.domain.mapping.ChallengeStatus;
import com.main.writeRoom.service.ChallengeService.ChallengeGoalsQueryService;
import com.main.writeRoom.service.ChallengeService.ChallengeRoutineQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
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
    private final ChallengeGoalsQueryService goalsQueryService;

    @Scheduled(cron = "0 15 11 * * *")  //매일 자정에 검사
    //@Scheduled(fixedRate = 60000) //1분마다 검사(테스트용)
    public void checkChallengeRoutine() {
        log.info("챌린지 달성 체크 작업 시작");

        List<ChallengeRoutineParticipation> routineParticipationList = routineQueryService.findByChallengeStatus(ChallengeStatus.PROGRESS);
        List<ChallengeGoalsParticipation> goalsParticipationList = goalsQueryService.findByChallengeStatus(ChallengeStatus.PROGRESS);

        routineParticipationList.forEach(routineParticipation -> {
            //루틴 체크 작업
            System.out.println("========================================");
            System.out.println("챌린지 루틴 참여" + routineParticipation.getId() + " achieveCount: " + routineParticipation.getAchieveCount());
            System.out.println("챌린지 루틴 참여" + routineParticipation.getId() + " status: " + routineParticipation.getChallengeStatus());
            checkRoutineAchieve(routineParticipation);
            System.out.println("체크 후");
            System.out.println("챌린지 루틴 참여" + routineParticipation.getId() + " achieveCount: " + routineParticipation.getAchieveCount());
            System.out.println("챌린지 루틴 참여" + routineParticipation.getId() + " status: " + routineParticipation.getChallengeStatus());
        });

        goalsParticipationList.forEach(goalsParticipation ->{
            //목표량 체크 작업 내용
            System.out.println();
            System.out.println("챌린지 목표량 참여"+goalsParticipation.getId()+" status: "+goalsParticipation.getChallengeStatus());
            checkGoalsAchieve(goalsParticipation);
            System.out.println("체크 후");
            System.out.println("챌린지 목표량 참여"+goalsParticipation.getId()+" status: "+goalsParticipation.getChallengeStatus());
            System.out.println("========================================");
        });
    }

    public void checkRoutineAchieve(ChallengeRoutineParticipation crp) {
        if (!(LocalDate.now().minusDays(1).isBefore(crp.getChallengeRoutine().getStartDate())) && ChronoUnit.DAYS.between(crp.getChallengeRoutine().getDeadline(), LocalDate.now().minusDays(1)) % 7 == 0) { //어제가(자정에 검사하니까 어제 날짜로 해야함)챌린지 기간의 주 마지막 날인 경우,

            if (crp.getAchieveCount() < crp.getChallengeRoutine().getTargetCount()) { //주의 마지막 날까지 목표일수를 못 채웠으므로 실패
                crp.setChallengeStatus(ChallengeStatus.FAILURE);
                crp.setStatusUpdatedAt(LocalDate.now().minusDays(1)); //실패 날짜 기록(어제로 기록)
            }
            crp.setAchieveCount(0);  // 주의 마지막 날이니까 달성 카운트를 0으로 초기화
        }
         /*if (LocalDate.now().isEqual(crp.getChallengeRoutine().getStartDate())) { //테스트용
            if (crp.getAchieveCount() < crp.getChallengeRoutine().getTargetCount()) { //주의 마지막 날까지 목표일수를 못 채웠으므로 실패
                crp.setChallengeStatus(ChallengeStatus.FAILURE);
                crp.setStatusUpdatedAt(LocalDate.now()); //실패 날짜 기록
            }
            crp.setAchieveCount(0);  // 주의 마지막 날이니까 달성 카운트를 0으로 초기화
        }*/
        crp.setIsNoteToday(false); //하루 지났으니까 다시 false로 초기화
    }

    public void checkGoalsAchieve(ChallengeGoalsParticipation cgp) {
        if (cgp.getChallengeGoals().getStartDate() != null && LocalDate.now().minusDays(1).isEqual(cgp.getChallengeGoals().getDeadline())) { //어제가 마감일이라면, 목표량을 채우지 못한 경우만 남음. 채웠으면 노트 저장할 때 이미 SUCCESS로 바뀌니까.
            cgp.setChallengeStatus(ChallengeStatus.FAILURE);
            cgp.setStatusUpdatedAt(LocalDate.now().minusDays(1));
        }
        /*if (LocalDate.now().isEqual(cgp.getChallengeGoals().getStartDate())) { //테스트용
            cgp.setChallengeStatus(ChallengeStatus.FAILURE);
            cgp.setStatusUpdatedAt(LocalDate.now());
        }*/
    }
}
