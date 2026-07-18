CREATE
EXTENSION IF NOT EXISTS pgcrypto;

CREATE TYPE offer_status AS ENUM (
    'DRAFT',
    'PENDING',
    'ACTIVE',
    'INACTIVE',
    'CLOSED'
);

CREATE TYPE job_application_status AS ENUM (
    'SUBMITTED',
    'IN_PROGRESS',
    'REJECTED',
    'HIRED',
    'WITHDRAWN'
);

CREATE TYPE application_step_status AS ENUM (
    'WAITING',
    'CURRENT',
    'COMPLETED',
    'REJECTED',
    'SKIPPED',
    'CANCELLED'
);

CREATE TYPE interview_status AS ENUM (
    'SCHEDULED',
    'COMPLETED',
    'CANCELLED',
    'NO_SHOW',
    'RESCHEDULED'
);

CREATE TYPE log_type AS ENUM (
    'INFO',
    'WARNING',
    'SUCCESS'
);

CREATE TYPE log_trigger AS ENUM (
    'JOB_OFFER_CREATED',
    'JOB_OFFER_UPDATED',
    'JOB_OFFER_STATUS_CHANGED',
    'LOCATION',
    'DEPARTMENT',
    'WORK_MODEL',
    'FULL_TIME_EQUIVALENT',
    'CONTRACT_TYPE',
    'RECRUITMENT_STEP_CREATED',
    'RECRUITMENT_STEP_UPDATED',
    'RECRUITMENT_STEP_DELETED',
    'RECRUITMENT_PROCESS_CREATED',
    'RECRUITMENT_PROCESS_UPDATED',
    'RECRUITMENT_PROCESS_DELETED',
    'USER_CREATED',
    'USER_DEPARTMENT_CHANGED',
    'USER_ROLE_CHANGED',
    'USER_LOCK_CHANGE',
    'ROLE_CREATED',
    'ROLE_UPDATED',
    'ROLE_DELETED'
);

CREATE TABLE role
(
    id                          UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    name                        VARCHAR(255) NOT NULL,
    description                 VARCHAR(1000),

    can_manage_job_applications BOOLEAN      NOT NULL DEFAULT FALSE,

    can_add_new_offer           BOOLEAN      NOT NULL DEFAULT FALSE,
    can_edit_existing_offer     BOOLEAN      NOT NULL DEFAULT FALSE,
    can_view_all_offers         BOOLEAN      NOT NULL DEFAULT FALSE,

    can_manage_users            BOOLEAN      NOT NULL DEFAULT FALSE,
    can_manage_roles            BOOLEAN      NOT NULL DEFAULT FALSE,

    can_view_logs               BOOLEAN      NOT NULL DEFAULT FALSE,
    deleted                     BOOLEAN      NOT NULL DEFAULT FALSE,

    created_at                  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at                  TIMESTAMP    NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_role_name UNIQUE (name)
);

CREATE TABLE department
(
    id          UUID PRIMARY KEY   DEFAULT gen_random_uuid(),
    name        VARCHAR(255),
    description VARCHAR(1000),
    deleted     BOOLEAN   NOT NULL DEFAULT FALSE,

    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE app_user
(
    id            UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    first_name    VARCHAR(100) NOT NULL,
    last_name     VARCHAR(100) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    password      VARCHAR(255) NOT NULL,
    description   VARCHAR(255) NOT NULL,
    locked        BOOLEAN      NOT NULL,

    role_id       UUID         NOT NULL,
    department_id UUID         NOT NULL,

    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_app_user_email UNIQUE (email),

    CONSTRAINT fk_app_user_role
        FOREIGN KEY (role_id)
            REFERENCES role (id),

    CONSTRAINT fk_app_user_department
        FOREIGN KEY (department_id)
            REFERENCES department (id)
);

CREATE TABLE contract_type
(
    id          UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    deleted     BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP   NOT NULL DEFAULT NOW()
);

CREATE TABLE location
(
    id          UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    city        VARCHAR(100) NOT NULL,
    country     VARCHAR(100) NOT NULL,
    description VARCHAR(1000),
    deleted     BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE full_time_equivalent
(
    id          UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    deleted     BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP   NOT NULL DEFAULT NOW()
);

CREATE TABLE work_model
(
    id          UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    deleted     BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP   NOT NULL DEFAULT NOW()
);

CREATE TABLE benefit
(
    id          UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    deleted     BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP   NOT NULL DEFAULT NOW()
);

CREATE TABLE recruitment_process
(
    id          UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    deleted     BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE recruitment_process_version
(
    id                     UUID PRIMARY KEY   DEFAULT gen_random_uuid(),
    recruitment_process_id UUID      NOT NULL,
    version                INT       NOT NULL,
    active                 BOOLEAN   NOT NULL DEFAULT FALSE,
    created_at             TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_recruitment_process_version_number
        UNIQUE(recruitment_process_id, version),

    CONSTRAINT fk_recruitment_process_version_process
        FOREIGN KEY (recruitment_process_id)
            REFERENCES recruitment_process (id)
            ON DELETE CASCADE
);

CREATE TABLE job_offer
(
    id                             UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    name                           VARCHAR(255) NOT NULL,
    description                    VARCHAR(255) NOT NULL,

    salary_from                    INT          NOT NULL,
    salary_to                      INT          NOT NULL,
    currency                       VARCHAR(4)   NOT NULL,

    contract_type_id               UUID         NOT NULL,
    location_id                    UUID         NOT NULL,
    full_time_equivalent_id        UUID         NOT NULL,
    work_model_id                  UUID         NOT NULL,
    department_id                  UUID         NOT NULL,

    must_have_requirements         TEXT[] NOT NULL,
    nice_to_have_requirements      TEXT[] NOT NULL,

    valid_from                     TIMESTAMP    NOT NULL,
    valid_to                       TIMESTAMP    NOT NULL,

    offer_status                   offer_status NOT NULL DEFAULT 'DRAFT',
    vacancy                        INT          NOT NULL,

    recruitment_process_version_id UUID         NOT NULL,
    recruiter_id                   UUID         NOT NULL,
    substitute_recruiter_id        UUID         NOT NULL,

    deleted                        BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at                     TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at                     TIMESTAMP    NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_job_offer_contract_type
        FOREIGN KEY (contract_type_id)
            REFERENCES contract_type (id),

    CONSTRAINT fk_job_offer_location
        FOREIGN KEY (location_id)
            REFERENCES location (id),

    CONSTRAINT fk_job_offer_full_time_equivalent
        FOREIGN KEY (full_time_equivalent_id)
            REFERENCES full_time_equivalent (id),

    CONSTRAINT fk_job_offer_work_model
        FOREIGN KEY (work_model_id)
            REFERENCES work_model (id),

    CONSTRAINT fk_job_offer_department
        FOREIGN KEY (department_id)
            REFERENCES department (id),

    CONSTRAINT fk_job_offer_recruitment_process_version
        FOREIGN KEY (recruitment_process_version_id)
            REFERENCES recruitment_process_version (id),

    CONSTRAINT fk_job_offer_recruiter
        FOREIGN KEY (recruiter_id)
            REFERENCES app_user (id),

    CONSTRAINT fk_job_offer_substitute_recruiter
        FOREIGN KEY (substitute_recruiter_id)
            REFERENCES app_user (id)
);

CREATE TABLE job_offer_benefit
(
    job_offer_id UUID NOT NULL,
    benefit_id   UUID NOT NULL,

    CONSTRAINT pk_job_offer_benefit
        PRIMARY KEY (job_offer_id, benefit_id),

    CONSTRAINT fk_job_offer_benefit_job_offer
        FOREIGN KEY (job_offer_id)
            REFERENCES job_offer (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_job_offer_benefit_benefit
        FOREIGN KEY (benefit_id)
            REFERENCES benefit (id)
            ON DELETE CASCADE
);

CREATE TABLE process_step
(
    id                           UUID PRIMARY KEY     DEFAULT gen_random_uuid(),

    name                         VARCHAR(50) NOT NULL,
    description                  VARCHAR(255),

    requires_interview           BOOLEAN     NOT NULL DEFAULT FALSE,
    requires_department_approval BOOLEAN     NOT NULL DEFAULT FALSE,

    deleted                      BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at                   TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at                   TIMESTAMP   NOT NULL DEFAULT NOW(),
);

CREATE TABLE process_version_step
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    recruitment_process_version_id UUID NOT NULL,
    process_step_id UUID NOT NULL,

    step_order INT NOT NULL,

    created_at             TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT NOW()

        CONSTRAINT fk_process_version_step_version
        FOREIGN KEY (recruitment_process_version_id)
            REFERENCES recruitment_process_version(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_process_version_step_step
        FOREIGN KEY (process_step_id)
            REFERENCES process_step(id),

    CONSTRAINT uq_process_version_step
        UNIQUE (recruitment_process_version_id, process_step_id),

    CONSTRAINT uq_process_version_step_order
        UNIQUE (recruitment_process_version_id, step_order)
);

CREATE TABLE job_application
(
    id                             UUID PRIMARY KEY                DEFAULT gen_random_uuid(),
    public_token                   UUID                   NOT NULL,
    job_offer_id                   UUID                   NOT NULL,
    recruitment_process_version_id UUID                   NOT NULL,

    first_name                     VARCHAR(50)            NOT NULL,
    last_name                      VARCHAR(50)            NOT NULL,
    email                          VARCHAR(100)           NOT NULL,

    phone_number                   VARCHAR(30),
    github_link                    VARCHAR(255),

    status                         job_application_status NOT NULL DEFAULT 'SUBMITTED',

    deleted                        BOOLEAN                NOT NULL DEFAULT FALSE,
    created_at                     TIMESTAMP              NOT NULL DEFAULT NOW(),
    updated_at                     TIMESTAMP              NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_job_application_public_token
        UNIQUE (public_token),

    CONSTRAINT fk_job_application_job_offer
        FOREIGN KEY (job_offer_id)
            REFERENCES job_offer (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_job_application_recruitment_process_version
        FOREIGN KEY (recruitment_process_version_id)
            REFERENCES recruitment_process_version (id)
);

CREATE TABLE job_application_step
(
    id                   UUID PRIMARY KEY                 DEFAULT gen_random_uuid(),
    application_id       UUID                    NOT NULL,
    process_step_id      UUID                    NOT NULL,

    step_order           INT                     NOT NULL,
    status               application_step_status NOT NULL DEFAULT 'WAITING',

    started_at           TIMESTAMP               NOT NULL,
    started_by_user_id   UUID                    NOT NULL,

    completed_at         TIMESTAMP,
    completed_by_user_id UUID,

    rejected_at          TIMESTAMP,
    rejected_by_user_id  UUID,

    decision_comment     VARCHAR(100),
    rejection_reason     VARCHAR(100),

    deleted              BOOLEAN                 NOT NULL DEFAULT FALSE,
    created_at           TIMESTAMP               NOT NULL DEFAULT NOW(),
    updated_at           TIMESTAMP               NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_job_application_step_application
        FOREIGN KEY (application_id)
            REFERENCES job_application (id),

    CONSTRAINT fk_job_application_step_process_step
        FOREIGN KEY (process_step_id)
            REFERENCES process_step (id),

    CONSTRAINT fk_job_application_step_started_by_user
        FOREIGN KEY (started_by_user_id)
            REFERENCES app_user (id),

    CONSTRAINT fk_job_application_step_completed_by_user
        FOREIGN KEY (completed_by_user_id)
            REFERENCES app_user (id),

    CONSTRAINT fk_job_application_step_rejected_by_user
        FOREIGN KEY (rejected_by_user_id)
            REFERENCES app_user (id)
);

CREATE TABLE attachment
(
    id                 UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    original_name      VARCHAR(255) NOT NULL,
    stored_name        VARCHAR(255) NOT NULL,
    path               VARCHAR(255) NOT NULL,
    is_cv              BOOLEAN      NOT NULL,

    job_application_id UUID         NOT NULL,

    deleted            BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at         TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMP    NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_attachment_job_application
        FOREIGN KEY (job_application_id)
            REFERENCES job_application (id)
);

CREATE TABLE interview
(
    id                      UUID PRIMARY KEY          DEFAULT gen_random_uuid(),

    job_application_id      UUID             NOT NULL,
    job_application_step_id UUID             NOT NULL,
    recruiter_id            UUID             NOT NULL,

    scheduled_start         TIMESTAMP        NOT NULL,
    scheduled_end           TIMESTAMP        NOT NULL,

    status                  interview_status NOT NULL DEFAULT 'SCHEDULED',

    location                VARCHAR(255)     NOT NULL,
    meeting_url             VARCHAR(1000),
    notes                   VARCHAR(255),

    deleted                 BOOLEAN          NOT NULL DEFAULT FALSE,
    created_at              TIMESTAMP        NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP        NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_interview_job_application
        FOREIGN KEY (job_application_id)
            REFERENCES job_application (id),

    CONSTRAINT uq_interview_job_application_step
        UNIQUE (job_application_step_id),

    CONSTRAINT fk_interview_job_application_step
        FOREIGN KEY (job_application_step_id)
            REFERENCES job_application_step (id),

    CONSTRAINT fk_interview_recruiter
        FOREIGN KEY (recruiter_id)
            REFERENCES app_user (id)
);

CREATE TABLE log
(
    id            UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    message       VARCHAR(255) NOT NULL,
    trigger       log_trigger  NOT NULL,
    type          log_type     NOT NULL DEFAULT 'INFO',
    created_by_id UUID         NOT NULL,

    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_user
        FOREIGN KEY (created_by_id)
            REFERENCES app_user (id)
            ON DELETE CASCADE
);