package com.portfolio.constant;

public final class AppConstants {

    private AppConstants() {
        // Prevent instantiation
    }

    // Pagination & Sorting Defaults
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY_DISPLAY_ORDER = "displayOrder";
    public static final String DEFAULT_SORT_BY_CREATED_AT = "createdAt";
    public static final String DEFAULT_SORT_DIRECTION_ASC = "asc";
    public static final String DEFAULT_SORT_DIRECTION_DESC = "desc";

    // Security & JWT Constants
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "Bearer";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_ADMIN_NAME = "ADMIN";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String CLAIM_TOKEN_TYPE = "token_type";
    public static final String TOKEN_TYPE_ACCESS = "ACCESS";
    public static final String TOKEN_TYPE_REFRESH = "REFRESH";

    // Application & Controller Response Messages
    public static final String MSG_LOGIN_SUCCESS = "Login successful";
    public static final String MSG_TOKEN_REFRESH_SUCCESS = "Token refreshed successfully";
    public static final String MSG_PASSWORD_CHANGED_SUCCESS = "Password changed successfully";
    public static final String MSG_PROFILE_CREATED_SUCCESS = "Profile created successfully";
    public static final String MSG_PROFILE_UPDATED_SUCCESS = "Profile updated successfully";
    public static final String MSG_PROJECT_CREATED_SUCCESS = "Project created successfully";
    public static final String MSG_PROJECT_UPDATED_SUCCESS = "Project updated successfully";
    public static final String MSG_PROJECT_PUBLISH_STATUS_UPDATED = "Project publication status updated";
    public static final String MSG_SKILL_CREATED_SUCCESS = "Skill created successfully";
    public static final String MSG_SKILL_UPDATED_SUCCESS = "Skill updated successfully";
    public static final String MSG_EXPERIENCE_CREATED_SUCCESS = "Experience record created successfully";
    public static final String MSG_EXPERIENCE_UPDATED_SUCCESS = "Experience record updated successfully";
    public static final String MSG_EDUCATION_CREATED_SUCCESS = "Education record created successfully";
    public static final String MSG_EDUCATION_UPDATED_SUCCESS = "Education record updated successfully";
    public static final String MSG_CERTIFICATION_CREATED_SUCCESS = "Certification created successfully";
    public static final String MSG_CERTIFICATION_UPDATED_SUCCESS = "Certification updated successfully";
    public static final String MSG_SOCIAL_LINK_CREATED_SUCCESS = "Social link created successfully";
    public static final String MSG_SOCIAL_LINK_UPDATED_SUCCESS = "Social link updated successfully";
    public static final String MSG_SITE_SETTING_SAVED_SUCCESS = "Site setting saved successfully";
    public static final String MSG_CONTACT_MESSAGE_SUBMIT_SUCCESS = "Thank you for reaching out! Your message has been sent successfully.";
    public static final String MSG_CONTACT_MESSAGE_STATUS_UPDATED = "Message status updated";

    // Media & Cloudinary Constants
    public static final String DEFAULT_CLOUDINARY_FOLDER = "portfolio";
    public static final String MSG_IMAGE_UPLOADED_SUCCESS = "Image uploaded successfully";
    public static final String MSG_IMAGE_DELETED_SUCCESS = "Image deleted successfully";
}
