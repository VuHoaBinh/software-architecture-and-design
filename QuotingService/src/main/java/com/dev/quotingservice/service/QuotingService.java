package com.dev.quotingservice.service;

import com.dev.quotingservice.dto.QuotingDTO;
import com.dev.quotingservice.entity.Category;
import com.dev.quotingservice.entity.Status;
import com.dev.quotingservice.repository.QuotingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
@AllArgsConstructor
@Slf4j
public class QuotingService {
    private QuotingRepository repository;

    private static final double BASE_VALUE = 0.8; // Giả sử giá cơ bản mặc định
    private static final double IPHONE_ADJUSTMENT = 0.2; // Điều chỉnh danh mục cho sản phẩm điện tử
    private static final double ANDROID_ADJUSTMENT = 0.15; // Điều chỉnh danh mục cho sản phẩm điện tử
    private static final double TIME_DECAY_RATE = 0.05; // Tỉ lệ giảm giá theo thời gian (5% mỗi năm)
    private static final double NEW_95_DISCOUNT = 0.05; // Giảm giá cho món hàng mới 95%
    private static final double NEW_90_DISCOUNT = 0.1; // Giảm giá cho món hàng mới 90%
    private static final double NEW_85_DISCOUNT = 0.15; // Giảm giá cho món hàng mới 85%
    private static final double NEW_80_DISCOUNT = 0.2; // Giảm giá cho món hàng mới 80%


    public Mono<Double> calculateRepurchasePrice(QuotingDTO info) {
        // Tính giá cơ bản
        double repurchasePrice = info.getPriceOrigin() * BASE_VALUE;

        // Điều chỉnh giá dựa trên danh mục
        if (info.getCategory() == Category.IPHONE)
            repurchasePrice += info.getPriceOrigin() * IPHONE_ADJUSTMENT;
        else
            repurchasePrice += info.getPriceOrigin() * ANDROID_ADJUSTMENT;

        // Điều chỉnh giá dựa trên thời gian
        Date currentDate = new Date();
        long timeDifferenceInMillis = currentDate.getTime() - info.getDateBy().getTime();
        long yearsDifference = timeDifferenceInMillis / (1000L * 60 * 60 * 24 * 365); // Chuyển đổi sang số năm
        double timeAdjustment = yearsDifference * TIME_DECAY_RATE * info.getPriceOrigin();
        repurchasePrice -= timeAdjustment;

        // Điều chỉnh giá dựa trên trạng thái
        switch (info.getStatus()) {
            case NEW_95:
                repurchasePrice -= info.getPriceOrigin() * NEW_95_DISCOUNT;
                break;
            case NEW_90:
                repurchasePrice -= info.getPriceOrigin() * NEW_90_DISCOUNT;
                break;
            case NEW_85:
                repurchasePrice -= info.getPriceOrigin() * NEW_85_DISCOUNT;
                break;
            case NEW_80:
                repurchasePrice -= info.getPriceOrigin() * NEW_80_DISCOUNT;
                break;
        }

        return Mono.just(repurchasePrice);
    }
}
