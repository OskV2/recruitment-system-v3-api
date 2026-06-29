# recruitment-system-v3-api

```mermaid
    erDiagram
    AppUser {
        uuid id PK
        string firstName
        string lastName
        string email UK
        string password
        string description
        boolean locked
        uuid roleId FK
        uuid departmentId FK
        datetime createdAt
        datetime updatedAt
    }

    Role {
        uuid id PK
        string name UK
        string description
        boolean canManageUsers
        boolean canManageRoles
        boolean canAddNewOffer
        boolean canEditExistingOffer
        boolean canViewAllOffers
        boolean canManageJobApplications
        boolean canViewLogs
        boolean deleted
        datetime createdAt
        datetime updatedAt
    }

    Department {
        uuid id PK
        string name
        string description
        boolean deleted
        datetime createdAt
        datetime updatedAt
    }

    JobOffer {
        uuid id PK
        string name
        string description
        int salaryFrom
        int salaryTo
        string currency
        uuid contractTypeId FK
        uuid locationId FK
        uuid fullTimeEquivalentId FK
        uuid workModelId FK
        uuid departmentId FK
        string[] mustHaveRequirements
        string[] niceToHaveRequirements
        datetime validFrom
        datetime validTo
        string offerStatus "ENUM('DRAFT', 'PENDING', 'ACTIVE', 'INACTIVE', 'CLOSED')"
        int vacancy
        uuid recruitmentProcessVersionId FK
        uuid recruiterId FK
        uuid substituteRecruiterId FK
        boolean deleted
        datetime createdAt
        datetime updatedAt
    }

    ContractType {
        uuid id PK
        string name
        string description
        boolean deleted
        datetime createdAt
        datetime updatedAt
    }

    Location {
        uuid id PK
        string country
        string city
        string description
        boolean deleted
        datetime createdAt
        datetime updatedAt
    }

    FullTimeEquivalent {
        uuid id PK
        string name
        string description
        boolean deleted
        datetime createdAt
        datetime updatedAt
    }

    WorkModel {
        uuid id PK
        string name
        string description
        boolean deleted
        datetime createdAt
        datetime updatedAt
    }

    Benefit {
        uuid id PK
        string name
        string description
        boolean deleted
        datetime createdAt
        datetime updatedAt
    }

    JobOfferBenefit {
        uuid id PK
        uuid jobOfferId FK "UNIQUE(jobOfferId, benefitId)"
        uuid benefitId FK "UNIQUE(jobOfferId, benefitId)"
    }

    RecruitmentProcess {
        uuid id PK
        string name
        string description
        boolean deleted
        datetime createdAt
        datetime updatedAt
    }

    RecruitmentProcessVersion {
        uuid id PK
        uuid recruitmentProcessId FK
        uuid version
        boolean active
        datetime createdAt
    }

    ProcessStep {
        uuid id PK
        uuid processVersionId FK
        int stepOrder
        string name
        string description
        boolean requiresInterview
        boolean requiresDepartmentApproval
        boolean deleted
        datetime createdAt
        datetime updatedAt
    }

    JobApplication {
        uuid id PK
        uuid publicToken UK
        uuid jobOfferId FK
        uuid recruitmentProcessVersionId FK
        string firstName
        string lastName
        string email
        string phoneNumber
        string githubLink
        string status "ENUM('SUBMITTED', 'IN_PROGRESS', 'REJECTED', 'HIRED', 'WITHDRAWN')"
        boolean deleted
        datetime createdAt
        datetime updatedAt
    }

    JobApplicationStep {
        uuid id PK
        uuid applicationId FK
        uuid processStepId FK
        int stepOrder
        string status "ENUM('WAITING', 'CURRENT', 'COMPLETED', 'REJECTED', 'SKIPPED', 'CANCELLED')"
        datetime startedAt
        uuid startedByUserId FK
        datetime completedAt "nullable"
        uuid completedByUserId FK "nullable"
        datetime rejectedAt "nullable"
        uuid rejectedByUserId FK "nullable"
        string decisionComment
        string rejectionReason
        boolean deleted
        datetime createdAt
        datetime updatedAt
    }

    Interview {
        uuid id PK
        uuid jobApplicationId FK
        uuid jobApplicationStepId FK
        uuid recruiterId FK
        datetime scheduledStart
        datetime scheduledEnd
        string status "ENUM('SCHEDULED', 'COMPLETED', 'CANCELLED', 'NO_SHOW', 'RESCHEDULED')"
        string location
        string meetingUrl
        string notes
        boolean deleted
        datetime createdAt
        datetime updatedAt
    }

    Attachment {
        uuid id PK
        string originalName
        string storedName
        string path
        boolean isCV
        uuid jobApplicationId FK
        boolean deleted
        datetime createdAt
        datetime updatedAt
    }

    Log {
        uuid id PK
        string message
        string trigger "ENUM"
        string type "ENUM ('INFO', 'WARNING', 'SUCCESS')"
        uuid created_by_id FK
        datetime createdAt
        datetime updatedAt
    }

%% Relacje użytkowników, ról i działów

    Role ||--o{ AppUser : has
    Department ||--o{ AppUser : has_users

    AppUser ||--o{ JobOffer : recruiter
    AppUser ||--o{ JobOffer : substitute_recruiter

    AppUser ||--o{ Interview : conducts

    AppUser ||--o{ JobApplicationStep : started_step
    AppUser |o--o{ JobApplicationStep : completed_step
    AppUser |o--o{ JobApplicationStep : rejected_step
    
    AppUser ||--|{ Log : created_by_id

%% Relacje ofert pracy

    ContractType ||--o{ JobOffer : contract_type
    Department ||--o{ JobOffer : department
    Location ||--o{ JobOffer : location
    FullTimeEquivalent ||--o{ JobOffer : fte
    WorkModel ||--o{ JobOffer : work_model

    JobOffer ||--o{ JobApplication : receives

    JobOffer ||--o{ JobOfferBenefit : has
    Benefit ||--o{ JobOfferBenefit : assigned_to

%% Relacje procesu rekrutacyjnego

    RecruitmentProcess ||--o{ RecruitmentProcessVersion : has_versions
    RecruitmentProcessVersion ||--o{ ProcessStep : contains_steps

    RecruitmentProcessVersion ||--o{ JobOffer : used_by_offer
    RecruitmentProcessVersion ||--o{ JobApplication : followed_by_application

%% Relacje aplikacji kandydata

    JobApplication ||--o{ JobApplicationStep : has_steps
    ProcessStep ||--o{ JobApplicationStep : instantiated_as

    JobApplication ||--o{ Attachment : contains

    JobApplication ||--o{ Interview : has_interviews
    JobApplicationStep ||--|| Interview : interview_for_step
```

Project structure
```
src
└── main
    ├── java
    │   └── com
    │       └── example
    │           └── recruitmentsystem
    │               ├── RecruitmentSystemApplication.java
    │               │
    │               ├── auth
    │               │   ├── controller
    │               │   │   └── AuthController.java
    │               │   ├── dto
    │               │   │   ├── LoginRequest.java
    │               │   │   ├── LoginResponse.java
    │               │   │   └── RefreshTokenRequest.java
    │               │   ├── service
    │               │   │   ├── AuthService.java
    │               │   │   └── JwtService.java
    │               │   └── security
    │               │       ├── JwtAuthenticationFilter.java
    │               │       ├── CustomUserDetailsService.java
    │               │       └── SecurityUser.java
    │               │
    │               ├── user
    │               │   ├── controller
    │               │   │   └── UserController.java
    │               │   ├── dto
    │               │   │   ├── CreateUserRequest.java
    │               │   │   ├── UpdateUserRequest.java
    │               │   │   └── UserResponse.java
    │               │   ├── mapper
    │               │   │   └── UserMapper.java
    │               │   ├── model
    │               │   │   └── User.java
    │               │   ├── repository
    │               │   │   └── UserRepository.java
    │               │   └── service
    │               │       └── UserService.java
    │               │
    │               ├── role
    │               │   ├── controller
    │               │   │   └── RoleController.java
    │               │   ├── dto
    │               │   │   ├── CreateRoleRequest.java
    │               │   │   ├── UpdateRoleRequest.java
    │               │   │   └── RoleResponse.java
    │               │   ├── mapper
    │               │   │   └── RoleMapper.java
    │               │   ├── model
    │               │   │   └── Role.java
    │               │   ├── repository
    │               │   │   └── RoleRepository.java
    │               │   └── service
    │               │       └── RoleService.java
    │               │
    │               ├── joboffer
    │               │   ├── controller
    │               │   │   ├── JobOfferController.java
    │               │   │   └── PublicJobOfferController.java
    │               │   ├── dto
    │               │   │   ├── CreateJobOfferRequest.java
    │               │   │   ├── UpdateJobOfferRequest.java
    │               │   │   ├── PublishJobOfferRequest.java
    │               │   │   ├── JobOfferResponse.java
    │               │   │   ├── JobOfferListItemResponse.java
    │               │   │   └── JobOfferDetailsResponse.java
    │               │   ├── mapper
    │               │   │   └── JobOfferMapper.java
    │               │   ├── model
    │               │   │   ├── JobOffer.java
    │               │   │   ├── JobOfferBenefit.java
    │               │   │   └── JobOfferStatus.java
    │               │   ├── repository
    │               │   │   ├── JobOfferRepository.java
    │               │   │   └── JobOfferBenefitRepository.java
    │               │   └── service
    │               │       ├── JobOfferService.java
    │               │       ├── JobOfferPublishingService.java
    │               │       └── PublicJobOfferService.java
    │               │
    │               ├── recruitmentprocess
    │               │   ├── controller
    │               │   │   └── RecruitmentProcessController.java
    │               │   ├── dto
    │               │   │   ├── CreateRecruitmentProcessRequest.java
    │               │   │   ├── UpdateRecruitmentProcessRequest.java
    │               │   │   ├── CreateProcessStepRequest.java
    │               │   │   ├── RecruitmentProcessResponse.java
    │               │   │   ├── RecruitmentProcessVersionResponse.java
    │               │   │   └── ProcessStepResponse.java
    │               │   ├── mapper
    │               │   │   ├── RecruitmentProcessMapper.java
    │               │   │   └── ProcessStepMapper.java
    │               │   ├── model
    │               │   │   ├── RecruitmentProcess.java
    │               │   │   ├── RecruitmentProcessVersion.java
    │               │   │   └── ProcessStep.java
    │               │   ├── repository
    │               │   │   ├── RecruitmentProcessRepository.java
    │               │   │   ├── RecruitmentProcessVersionRepository.java
    │               │   │   └── ProcessStepRepository.java
    │               │   └── service
    │               │       ├── RecruitmentProcessService.java
    │               │       ├── RecruitmentProcessVersionService.java
    │               │       └── ProcessStepService.java
    │               │
    │               ├── jobapplication
    │               │   ├── controller
    │               │   │   ├── JobApplicationController.java
    │               │   │   └── PublicApplicationStatusController.java
    │               │   ├── dto
    │               │   │   ├── CreateJobApplicationRequest.java
    │               │   │   ├── ApplicationStatusLookupRequest.java
    │               │   │   ├── JobApplicationResponse.java
    │               │   │   ├── JobApplicationListItemResponse.java
    │               │   │   ├── ApplicationStatusResponse.java
    │               │   │   ├── ChangeApplicationStepRequest.java
    │               │   │   └── JobApplicationStepResponse.java
    │               │   ├── mapper
    │               │   │   ├── JobApplicationMapper.java
    │               │   │   └── JobApplicationStepMapper.java
    │               │   ├── model
    │               │   │   ├── JobApplication.java
    │               │   │   ├── JobApplicationStatus.java
    │               │   │   ├── JobApplicationStep.java
    │               │   │   └── JobApplicationStepStatus.java
    │               │   ├── repository
    │               │   │   ├── JobApplicationRepository.java
    │               │   │   └── JobApplicationStepRepository.java
    │               │   └── service
    │               │       ├── JobApplicationService.java
    │               │       ├── JobApplicationStepService.java
    │               │       ├── ApplicationStatusService.java
    │               │       └── ApplicationSubmissionService.java
    │               │
    │               ├── interview
    │               │   ├── controller
    │               │   │   └── InterviewController.java
    │               │   ├── dto
    │               │   │   ├── CreateInterviewRequest.java
    │               │   │   ├── UpdateInterviewRequest.java
    │               │   │   └── InterviewResponse.java
    │               │   ├── mapper
    │               │   │   └── InterviewMapper.java
    │               │   ├── model
    │               │   │   ├── Interview.java
    │               │   │   └── InterviewStatus.java
    │               │   ├── repository
    │               │   │   └── InterviewRepository.java
    │               │   └── service
    │               │       └── InterviewService.java
    │               │
    │               ├── attachment
    │               │   ├── controller
    │               │   │   └── AttachmentController.java
    │               │   ├── dto
    │               │   │   └── AttachmentResponse.java
    │               │   ├── mapper
    │               │   │   └── AttachmentMapper.java
    │               │   ├── model
    │               │   │   └── Attachment.java
    │               │   ├── repository
    │               │   │   └── AttachmentRepository.java
    │               │   ├── service
    │               │   │   ├── AttachmentService.java
    │               │   │   └── FileStorageService.java
    │               │   └── validator
    │               │       └── AttachmentValidator.java
    │               │
    │               ├── dictionary
    │               │   ├── controller
    │               │   │   ├── ContractTypeController.java
    │               │   │   ├── LocationController.java
    │               │   │   ├── FullTimeEquivalentController.java
    │               │   │   ├── WorkModelController.java
    │               │   │   └── BenefitController.java
    │               │   ├── dto
    │               │   │   ├── DictionaryItemRequest.java
    │               │   │   ├── DictionaryItemResponse.java
    │               │   │   └── LocationResponse.java
    │               │   ├── mapper
    │               │   │   └── DictionaryMapper.java
    │               │   ├── model
    │               │   │   ├── ContractType.java
    │               │   │   ├── Location.java
    │               │   │   ├── FullTimeEquivalent.java
    │               │   │   ├── WorkModel.java
    │               │   │   └── Benefit.java
    │               │   ├── repository
    │               │   │   ├── ContractTypeRepository.java
    │               │   │   ├── LocationRepository.java
    │               │   │   ├── FullTimeEquivalentRepository.java
    │               │   │   ├── WorkModelRepository.java
    │               │   │   └── BenefitRepository.java
    │               │   └── service
    │               │       ├── ContractTypeService.java
    │               │       ├── LocationService.java
    │               │       ├── FullTimeEquivalentService.java
    │               │       ├── WorkModelService.java
    │               │       └── BenefitService.java
    │               │
    │               ├── log
    │               │   ├── controller
    │               │   │   └── LogController.java
    │               │   ├── dto
    │               │   │   └── LogResponse.java
    │               │   ├── mapper
    │               │   │   └── LogMapper.java
    │               │   ├── model
    │               │   │   └── Log.java
    │               │   ├── repository
    │               │   │   └── LogRepository.java
    │               │   └── service
    │               │       └── LogService.java
    │               │
    │               ├── dashboard
    │               │   ├── controller
    │               │   │   └── DashboardController.java
    │               │   ├── dto
    │               │   │   ├── RecruiterDashboardResponse.java
    │               │   │   ├── AdminDashboardResponse.java
    │               │   │   └── ApplicationsChartResponse.java
    │               │   └── service
    │               │       └── DashboardService.java
    │               │
    │               ├── notification
    │               │   ├── dto
    │               │   │   └── EmailMessage.java
    │               │   ├── model
    │               │   │   └── EmailTemplate.java
    │               │   ├── repository
    │               │   │   └── EmailTemplateRepository.java
    │               │   └── service
    │               │       ├── EmailService.java
    │               │       ├── EmailTemplateService.java
    │               │       └── ApplicationNotificationService.java
    │               │
    │               ├── config
    │               │   ├── OpenApiConfig.java
    │               │   ├── SecurityConfig.java
    │               │   ├── CorsConfig.java
    │               │   ├── JpaConfig.java
    │               │   └── MailConfig.java
    │               │
    │               ├── common
    │               │   ├── exception
    │               │   │   ├── GlobalExceptionHandler.java
    │               │   │   ├── NotFoundException.java
    │               │   │   ├── BadRequestException.java
    │               │   │   ├── ForbiddenException.java
    │               │   │   └── ErrorResponse.java
    │               │   ├── pagination
    │               │   │   └── PageResponse.java
    │               │   ├── validation
    │               │   │   ├── ValidationConstants.java
    │               │   │   └── ValidFile.java
    │               │   ├── util
    │               │   │   ├── TokenGenerator.java
    │               │   │   └── DateTimeProvider.java
    │               │   └── audit
    │               │       └── AuditableEntity.java
    │               │
    │               └── seed
    │                   └── SuperAdminSeeder.java
    │
    └── resources
        ├── application.yml
        ├── application-dev.yml
        ├── application-prod.yml
        ├── db
        │   └── migration
        │       ├── V1__init_schema.sql
        │       ├── V2__seed_roles.sql
        │       ├── V3__seed_super_admin.sql
        │       └── V4__seed_dictionaries.sql
        └── templates
            └── email
                ├── application-submitted.html
                ├── application-status-changed.html
                ├── interview-scheduled.html
                └── application-rejected.html
```