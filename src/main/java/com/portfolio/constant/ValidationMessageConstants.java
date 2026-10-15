package com.portfolio.constant;

public final class ValidationMessageConstants {

    private ValidationMessageConstants() {
        // Prevent instantiation
    }

    // Common & Auth
    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String EMAIL_INVALID = "Invalid email format";
    public static final String EMAIL_MAX_150 = "Email must not exceed 150 characters";
    public static final String PASSWORD_REQUIRED = "Password is required";
    public static final String PASSWORD_MIN_6 = "Password must be at least 6 characters long";
    public static final String CURRENT_PASSWORD_REQUIRED = "Current password is required";
    public static final String NEW_PASSWORD_REQUIRED = "New password is required";
    public static final String NEW_PASSWORD_MIN_6 = "New password must be at least 6 characters long";
    public static final String REFRESH_TOKEN_REQUIRED = "Refresh token must not be blank";

    // User / Admin
    public static final String FULL_NAME_REQUIRED = "Full name is required";
    public static final String FULL_NAME_MAX_100 = "Full name must not exceed 100 characters";

    // Profile
    public static final String PROFESSIONAL_TITLE_REQUIRED = "Professional title is required";
    public static final String PROFESSIONAL_TITLE_MAX_150 = "Professional title must not exceed 150 characters";
    public static final String PROFILE_IMAGE_URL_MAX_500 = "Profile image URL must not exceed 500 characters";
    public static final String SHORT_INTRO_MAX_500 = "Short introduction must not exceed 500 characters";
    public static final String PHONE_MAX_30 = "Phone number must not exceed 30 characters";
    public static final String LOCATION_MAX_150 = "Location must not exceed 150 characters";
    public static final String RESUME_URL_MAX_500 = "Resume URL must not exceed 500 characters";
    public static final String YEARS_OF_EXPERIENCE_MIN_0 = "Years of experience cannot be negative";

    // Project
    public static final String PROJECT_TITLE_REQUIRED = "Project title is required";
    public static final String PROJECT_TITLE_MAX_150 = "Project title must not exceed 150 characters";
    public static final String PROJECT_SLUG_MAX_150 = "Slug must not exceed 150 characters";
    public static final String PROJECT_SHORT_DESC_MAX_500 = "Short description must not exceed 500 characters";
    public static final String PROJECT_THUMBNAIL_URL_MAX_500 = "Thumbnail URL must not exceed 500 characters";
    public static final String PROJECT_GITHUB_URL_MAX_500 = "GitHub URL must not exceed 500 characters";
    public static final String PROJECT_LIVE_DEMO_URL_MAX_500 = "Live demo URL must not exceed 500 characters";

    // Skill
    public static final String SKILL_NAME_REQUIRED = "Skill name is required";
    public static final String SKILL_NAME_MAX_100 = "Skill name must not exceed 100 characters";
    public static final String SKILL_CATEGORY_REQUIRED = "Skill category is required";
    public static final String SKILL_PROFICIENCY_MIN_0 = "Proficiency level must be at least 0";
    public static final String SKILL_PROFICIENCY_MAX_100 = "Proficiency level must not exceed 100";
    public static final String SKILL_ICON_URL_MAX_500 = "Icon URL must not exceed 500 characters";

    // Experience
    public static final String COMPANY_NAME_REQUIRED = "Company name is required";
    public static final String COMPANY_NAME_MAX_150 = "Company name must not exceed 150 characters";
    public static final String JOB_TITLE_REQUIRED = "Job title is required";
    public static final String JOB_TITLE_MAX_150 = "Job title must not exceed 150 characters";
    public static final String START_DATE_REQUIRED = "Start date is required";

    // Education
    public static final String DEGREE_REQUIRED = "Degree is required";
    public static final String DEGREE_MAX_150 = "Degree must not exceed 150 characters";
    public static final String INSTITUTION_REQUIRED = "Institution is required";
    public static final String INSTITUTION_MAX_200 = "Institution must not exceed 200 characters";
    public static final String FIELD_OF_STUDY_MAX_150 = "Field of study must not exceed 150 characters";
    public static final String GRADE_MAX_50 = "Grade must not exceed 50 characters";

    // Certification
    public static final String CERTIFICATION_NAME_REQUIRED = "Certification name is required";
    public static final String CERTIFICATION_NAME_MAX_150 = "Certification name must not exceed 150 characters";
    public static final String ISSUING_ORGANIZATION_REQUIRED = "Issuing organization is required";
    public static final String ISSUING_ORGANIZATION_MAX_150 = "Issuing organization must not exceed 150 characters";
    public static final String ISSUE_DATE_REQUIRED = "Issue date is required";
    public static final String CREDENTIAL_ID_MAX_150 = "Credential ID must not exceed 150 characters";
    public static final String CREDENTIAL_URL_MAX_500 = "Credential URL must not exceed 500 characters";
    public static final String CERTIFICATE_IMAGE_URL_MAX_500 = "Certificate image URL must not exceed 500 characters";

    // Social Link
    public static final String PLATFORM_REQUIRED = "Platform is required";
    public static final String PLATFORM_MAX_100 = "Platform must not exceed 100 characters";
    public static final String LABEL_REQUIRED = "Label is required";
    public static final String LABEL_MAX_100 = "Label must not exceed 100 characters";
    public static final String URL_REQUIRED = "URL is required";
    public static final String URL_MAX_500 = "URL must not exceed 500 characters";
    public static final String ICON_MAX_100 = "Icon must not exceed 100 characters";

    // Contact Message
    public static final String CONTACT_NAME_REQUIRED = "Name is required";
    public static final String CONTACT_NAME_MAX_100 = "Name must not exceed 100 characters";
    public static final String CONTACT_EMAIL_INVALID = "Please provide a valid email address";
    public static final String CONTACT_SUBJECT_REQUIRED = "Subject is required";
    public static final String CONTACT_SUBJECT_MAX_200 = "Subject must not exceed 200 characters";
    public static final String CONTACT_MESSAGE_REQUIRED = "Message cannot be empty";
    public static final String CONTACT_MESSAGE_MAX_5000 = "Message must not exceed 5000 characters";

    // Site Setting
    public static final String SETTING_KEY_REQUIRED = "Setting key is required";
    public static final String SETTING_KEY_MAX_100 = "Setting key must not exceed 100 characters";
    public static final String SETTING_VALUE_REQUIRED = "Setting value is required";
    public static final String SETTING_DESCRIPTION_MAX_255 = "Description must not exceed 255 characters";

    // Media & File Upload
    public static final String FILE_EMPTY = "File cannot be empty";
    public static final String FILE_NOT_IMAGE = "Only valid image files (JPEG, PNG, WEBP, GIF, SVG) are allowed";
    public static final String PUBLIC_ID_REQUIRED = "Public ID is required";
}
