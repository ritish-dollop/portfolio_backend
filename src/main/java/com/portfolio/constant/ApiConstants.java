package com.portfolio.constant;

public final class ApiConstants {

    private ApiConstants() {
        // Prevent instantiation
    }

    // Security Wildcards & Core Matchers
    public static final String API_PUBLIC_WILDCARD = "/api/public/**";
    public static final String API_ADMIN_WILDCARD = "/api/admin/**";
    public static final String API_ADMIN_AUTH_LOGIN = "/api/admin/auth/login";
    public static final String API_ADMIN_AUTH_REFRESH = "/api/admin/auth/refreshToken";
    public static final String API_ADMIN_AUTH_SEND_OTP = "/api/admin/auth/sendOtp";
    public static final String API_ADMIN_AUTH_LOGIN_WITH_OTP = "/api/admin/auth/loginWithOtp";

    // Admin Controller Base Routes
    public static final String ADMIN_AUTH = "/api/admin/auth";
    public static final String ADMIN_PROFILE = "/api/admin/profile";
    public static final String ADMIN_PROJECTS = "/api/admin/projects";
    public static final String ADMIN_SKILLS = "/api/admin/skills";
    public static final String ADMIN_EXPERIENCES = "/api/admin/experiences";
    public static final String ADMIN_EDUCATIONS = "/api/admin/educations";
    public static final String ADMIN_CERTIFICATIONS = "/api/admin/certifications";
    public static final String ADMIN_SOCIAL_LINKS = "/api/admin/social-links";
    public static final String ADMIN_CONTACT_MESSAGES = "/api/admin/contact-messages";
    public static final String ADMIN_SITE_SETTINGS = "/api/admin/site-settings";
    public static final String ADMIN_MEDIA = "/api/admin/media";
    public static final String ADMIN_NOTIFICATIONS = "/api/admin/notifications";

    // Public Controller Base Routes
    public static final String PUBLIC_PROFILE = "/api/public/profile";
    public static final String PUBLIC_PROJECTS = "/api/public/projects";
    public static final String PUBLIC_SKILLS = "/api/public/skills";
    public static final String PUBLIC_EXPERIENCES = "/api/public/experiences";
    public static final String PUBLIC_EDUCATIONS = "/api/public/educations";
    public static final String PUBLIC_CERTIFICATIONS = "/api/public/certifications";
    public static final String PUBLIC_SOCIAL_LINKS = "/api/public/social-links";
    public static final String PUBLIC_CONTACT = "/api/public/contact";
    public static final String PUBLIC_SITE_SETTINGS = "/api/public/site-settings";

    // Auth Sub-paths
    public static final String LOGIN = "/login";
    public static final String REFRESH_TOKEN = "/refreshToken";
    public static final String GET_CURRENT_USER = "/getCurrentUser";
    public static final String CHANGE_PASSWORD = "/changePassword";
    public static final String SEND_OTP = "/sendOtp";
    public static final String LOGIN_WITH_OTP = "/loginWithOtp";

    // Profile Sub-paths
    public static final String GET_ADMIN_PROFILE = "/getAdminProfile";
    public static final String GET_PUBLIC_PROFILE = "/getPublicProfile";
    public static final String CREATE_PROFILE = "/createProfile";
    public static final String UPDATE_PROFILE = "/updateProfile";

    // Projects Sub-paths
    public static final String GET_ALL_PROJECTS = "/getAllProjects";
    public static final String GET_PUBLISHED_PROJECTS = "/getPublishedProjects";
    public static final String GET_FEATURED_PROJECTS = "/getFeaturedProjects";
    public static final String GET_PROJECT_BY_ID = "/getProjectById/{id}";
    public static final String GET_PROJECT_BY_SLUG = "/getProjectBySlug/{slug}";
    public static final String CREATE_PROJECT = "/createProject";
    public static final String UPDATE_PROJECT = "/updateProject/{id}";
    public static final String SET_PUBLISH_STATUS = "/setPublishStatus/{id}";
    public static final String DELETE_PROJECT = "/deleteProject/{id}";

    // Skills Sub-paths
    public static final String GET_ALL_SKILLS = "/getAllSkills";
    public static final String GET_SKILLS = "/getSkills";
    public static final String GET_SKILL_BY_ID = "/getSkillById/{id}";
    public static final String CREATE_SKILL = "/createSkill";
    public static final String UPDATE_SKILL = "/updateSkill/{id}";
    public static final String DELETE_SKILL = "/deleteSkill/{id}";

    // Experiences Sub-paths
    public static final String GET_ALL_EXPERIENCES = "/getAllExperiences";
    public static final String GET_EXPERIENCE_BY_ID = "/getExperienceById/{id}";
    public static final String CREATE_EXPERIENCE = "/createExperience";
    public static final String UPDATE_EXPERIENCE = "/updateExperience/{id}";
    public static final String DELETE_EXPERIENCE = "/deleteExperience/{id}";

    // Educations Sub-paths
    public static final String GET_ALL_EDUCATIONS = "/getAllEducations";
    public static final String GET_EDUCATION_BY_ID = "/getEducationById/{id}";
    public static final String CREATE_EDUCATION = "/createEducation";
    public static final String UPDATE_EDUCATION = "/updateEducation/{id}";
    public static final String DELETE_EDUCATION = "/deleteEducation/{id}";

    // Certifications Sub-paths
    public static final String GET_ALL_CERTIFICATIONS = "/getAllCertifications";
    public static final String GET_CERTIFICATION_BY_ID = "/getCertificationById/{id}";
    public static final String CREATE_CERTIFICATION = "/createCertification";
    public static final String UPDATE_CERTIFICATION = "/updateCertification/{id}";
    public static final String DELETE_CERTIFICATION = "/deleteCertification/{id}";

    // Social Links Sub-paths
    public static final String GET_ALL_SOCIAL_LINKS = "/getAllSocialLinks";
    public static final String GET_ACTIVE_SOCIAL_LINKS = "/getActiveSocialLinks";
    public static final String GET_SOCIAL_LINK_BY_ID = "/getSocialLinkById/{id}";
    public static final String CREATE_SOCIAL_LINK = "/createSocialLink";
    public static final String UPDATE_SOCIAL_LINK = "/updateSocialLink/{id}";
    public static final String DELETE_SOCIAL_LINK = "/deleteSocialLink/{id}";

    // Contact Messages Sub-paths
    public static final String SUBMIT_CONTACT_MESSAGE = "/submitContactMessage";
    public static final String GET_MESSAGES = "/getMessages";
    public static final String GET_UNREAD_COUNT = "/getUnreadCount";
    public static final String GET_MESSAGE_BY_ID = "/getMessageById/{id}";
    public static final String MARK_AS_READ = "/markAsRead/{id}";
    public static final String DELETE_MESSAGE = "/deleteMessage/{id}";

    // Site Settings Sub-paths
    public static final String GET_ALL_SETTINGS = "/getAllSettings";
    public static final String GET_PUBLIC_SETTINGS = "/getPublicSettings";
    public static final String GET_SETTING_BY_KEY = "/getSettingByKey/{key}";
    public static final String CREATE_OR_UPDATE_SETTING = "/createOrUpdateSetting";
    public static final String DELETE_SETTING = "/deleteSetting/{id}";

    // Media Sub-paths
    public static final String UPLOAD_IMAGE = "/uploadImage";
    public static final String DELETE_IMAGE = "/deleteImage";

    // Notifications Sub-paths
    public static final String GET_SOCKET_STATUS = "/status";
    public static final String BROADCAST_NOTIFICATION = "/broadcast";
    public static final String SEED_NOTIFICATIONS = "/seed";
    public static final String CLEAR_ALL_NOTIFICATIONS = "/clearAll";
}
