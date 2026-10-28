package com.portfolio.service;

import com.portfolio.dto.response.ContactMessageResponse;
import com.portfolio.dto.response.NotificationPayload;

public interface NotificationSocketService {

    void sendNotificationToAll(NotificationPayload notification);

    void sendNotificationToAdmin(NotificationPayload notification);

    void sendContactMessageNotification(ContactMessageResponse message, long unreadCount);

    void broadcastUnreadCount(long count);

    void sendCustomEvent(String eventName, Object data);

    void sendCustomEventToAdmin(String eventName, Object data);

    int getConnectedClientsCount();

    boolean isServerRunning();

    java.util.List<ContactMessageResponse> seedSampleNotifications();

    void clearAllMessages();
}
