package vn.ihqqq.MentorFlow.utils;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeAgoUtils {
    public static String calculateTimeAgo(LocalDateTime pastTime) {
        if (pastTime == null) return "Không xác định";

        LocalDateTime now = LocalDateTime.now();
        if (pastTime.isAfter(now)) {
            return "Trong tương lai";
        }

        Duration duration = Duration.between(pastTime, now);

        long seconds = duration.getSeconds();
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();

        if (seconds < 60) {
            return "Vừa xong";
        } else if (minutes < 60) {
            return minutes + " phút trước";
        } else if (hours < 24) {
            return hours + " giờ trước";
        } else if (days == 1) {
            return "Hôm qua";
        } else if (days < 7) {
            return days + " ngày trước";
        } else if (days < 30) {
            return (days / 7) + " tuần trước";
        } else if (days < 365) {
            return (days / 30) + " tháng trước";
        } else {
            return (days / 365) + " năm trước";
        }
    }
}
