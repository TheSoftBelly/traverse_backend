package com.example.userauth.repository;

import com.example.userauth.model.NotificationHistory;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
@RequiredArgsConstructor
public class NotificationRepository {
    private final Firestore firestore;

    public List<NotificationHistory> findByFilters(String type, Timestamp start, Timestamp end, int offset, int limit) throws ExecutionException, InterruptedException {
        CollectionReference collection = firestore.collection("notifications");

        // 🔁 1. Firestore에서는 sent_at 정렬만 하고, 필터는 걸지 않음
        Query query = collection.orderBy("sent_at", Query.Direction.DESCENDING)
                .limit(500); // 필터 없이 가져오니까 최대 개수 제한 필요

        // 🔄 2. Firestore에서 일부만 가져오고, Java에서 필터링
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> docs = future.get().getDocuments();

        List<NotificationHistory> results = new ArrayList<>();
        for (QueryDocumentSnapshot doc : docs) {
            NotificationHistory history = doc.toObject(NotificationHistory.class);
            history.setId(doc.getId());

            //  Java 단에서 조건 검사
            boolean match = true;
            if (type != null && !type.isEmpty() && !type.equals(history.getType())) {
                match = false;
            }
            if (start != null && history.getSent_at().compareTo(start) < 0) {
                match = false;
            }
            if (end != null && history.getSent_at().compareTo(end) > 0) {
                match = false;
            }

            if (match) {
                results.add(history);
            }
        }

        // ⏳ 페이지네이션은 Java에서 수동으로
        int fromIndex = Math.min(offset, results.size());
        int toIndex = Math.min(fromIndex + limit, results.size());
        return results.subList(fromIndex, toIndex);
    }


    public long countByFilters(String type, Timestamp start, Timestamp end) throws ExecutionException, InterruptedException {
        CollectionReference collection = firestore.collection("notifications");

        // 인덱스 없이 조회
        Query query = collection.orderBy("sent_at", Query.Direction.DESCENDING)
                .limit(500); // 데이터 많은 경우 조정 필요

        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> docs = future.get().getDocuments();

        return docs.stream().filter(doc -> {
            NotificationHistory history = doc.toObject(NotificationHistory.class);

            boolean match = true;
            if (type != null && !type.isEmpty() && !type.equals(history.getType())) {
                match = false;
            }
            if (start != null && history.getSent_at().compareTo(start) < 0) {
                match = false;
            }
            if (end != null && history.getSent_at().compareTo(end) > 0) {
                match = false;
            }
            return match;
        }).count();
    }

}
