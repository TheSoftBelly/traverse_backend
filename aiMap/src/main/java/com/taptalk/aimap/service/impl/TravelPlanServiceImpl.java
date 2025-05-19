package com.taptalk.aimap.service.impl;

import com.taptalk.aimap.dto.travel.TravelPlanSubmitRequest;
import com.taptalk.aimap.entity.*;
import com.taptalk.aimap.exception.ErrorCode;
import com.taptalk.aimap.exception.TravelPlanException;
import com.taptalk.aimap.repository.*;
import com.taptalk.aimap.service.TravelPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TravelPlanServiceImpl implements TravelPlanService {

    private final TripRepository tripRepository;
    private final DestinationRepository destinationRepository;
    private final DailyScheduleRepository dailyScheduleRepository;
    private final TravelEssentialsRepository travelEssentialsRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Trip submitTravelPlan(Long userId, TravelPlanSubmitRequest request) {
        // 사용자 존재 여부 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TravelPlanException(ErrorCode.USER_NOT_FOUND));

        // 여행지 정보 저장
        Destination destination = Destination.builder()
                .name(request.getDestination().getName())
                .type(request.getDestination().getType())
                .countryCode(request.getDestination().getCountryCode())
                .cityId(request.getDestination().getCityId())
                .areaCode(request.getDestination().getAreaCode())
                .contentTypeId(request.getDestination().getContentTypeId())
                .build();
        destination = destinationRepository.save(destination);

        // 여행 정보 저장
        Trip trip = Trip.builder()
                .user(user)
                .destination(destination)
                .startDate(request.getTravelDates().getStartDate())
                .endDate(request.getTravelDates().getEndDate())
                .companionType(Trip.CompanionType.valueOf(request.getCompanionType()))
                .concept(Trip.Concept.valueOf(request.getConcepts().get(0))) // 첫 번째 컨셉 사용
                .pace(Trip.Pace.MODERATE) // 기본값 설정
                .arrivalTransport(request.getTransportation().getArrivalTransport())
                .localTransport(request.getTransportation().getLocalTransport())
                .budget(request.getBudget().getTotalBudget())
                .trafficPass(request.getTransportation().getHasPass())
                .rentCar(request.getTransportation().getRentCar())
                .usim(request.getCommunication().getHasUsim())
                .pocketWifi(request.getCommunication().getHasPocketWifi())
                .passport(request.getDocuments().getHasPassport())
                .visa(request.getDocuments().getHasVisa())
                .insurance(request.getInsurance().getHasTravelInsurance())
                .build();
        trip = tripRepository.save(trip);

        // 일정 정보 저장
        Trip finalTrip = trip;
        List<DailySchedule> dailySchedules = request.getSelectedPlaces().stream()
                .map(place -> DailySchedule.builder()
                        .trip(finalTrip)
                        .visitDate(request.getTravelDates().getStartDate().plusDays(place.getDay() - 1))
                        .startTime(place.getStartTime())
                        .endTime(place.getEndTime())
                        .visitOrder(place.getOrder())
                        .build())
                .collect(Collectors.toList());
        dailyScheduleRepository.saveAll(dailySchedules);

        // 여행 필수품 정보 저장
        TravelEssentials travelEssentials = TravelEssentials.builder()
                .trip(trip)
                .hasPassport(request.getDocuments().getHasPassport())
                .hasVisa(request.getDocuments().getHasVisa())
                .hasTravelInsurance(request.getInsurance().getHasTravelInsurance())
                .hasUsim(request.getCommunication().getHasUsim())
                .hasPocketWifi(request.getCommunication().getHasPocketWifi())
                .build();
        travelEssentialsRepository.save(travelEssentials);

        return trip;
    }
} 