CREATE TYPE offer_status AS ENUM (
    'DRAFT', 'PENDING', 'ACTIVE', 'INACTIVE', 'CLOSED'
    );

CREATE TYPE job_application_status AS ENUM (
    'SUBMITTED', 'IN_PROGRESS', 'REJECTED', 'HIRED', 'WITHDRAWN'
    );

CREATE TYPE application_step_status AS ENUM (
    'WAITING', 'CURRENT', 'COMPLETED', 'REJECTED', 'SKIPPED', 'CANCELLED'
    );

CREATE TYPE interview_status AS ENUM (
    'SCHEDULED', 'COMPLETED', 'CANCELLED', 'NO_SHOW', 'RESCHEDULED'
    );


CREATE TABLE contract_type
(
    id          UUID PRIMARY KEY     DEFAULT GEN_RANDOM_UUID(),
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    deleted     BOOLEAN              DEFAULT false,
    created_at  TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP   NOT NULL DEFAULT NOW()
);

CREATE TABLE location
(
    id         UUID PRIMARY KEY      DEFAULT GEN_RANDOM_UUID(),
    city       VARCHAR(100) NOT NULL,
    country    VARCHAR(100) NOT NULL,
    deleted    BOOLEAN               DEFAULT false,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE full_time_equivalent
(
    id          UUID PRIMARY KEY     DEFAULT GEN_RANDOM_UUID(),
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    deleted     BOOLEAN              DEFAULT false,
    created_at  TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP   NOT NULL DEFAULT NOW()
);

CREATE TABLE work_model
(
    id          UUID PRIMARY KEY     DEFAULT GEN_RANDOM_UUID(),
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    deleted     BOOLEAN              DEFAULT false,
    created_at  TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP   NOT NULL DEFAULT NOW()
);

CREATE TABLE benefit
(
    id          UUID PRIMARY KEY     DEFAULT GEN_RANDOM_UUID(),
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    deleted     BOOLEAN              DEFAULT false,
    created_at  TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP   NOT NULL DEFAULT NOW()
);


CREATE TABLE recruitment_process
(
    id          UUID PRIMARY KEY      DEFAULT GEN_RANDOM_UUID(),
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    deleted     BOOLEAN               DEFAULT false,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE recruitment_process_version
(
    id                     UUID PRIMARY KEY   DEFAULT GEN_RANDOM_UUID(),
    recruitment_process_id UUID      NOT NULL,
    version                UUID               DEFAULT GEN_RANDOM_UUID(),
    active                 BOOLEAN            DEFAULT false,
    created_at             TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_recruitment_process
        FOREIGN KEY (recruitment_process_id)
            REFERENCES recruitment_process (id)
            ON DELETE CASCADE
);

CREATE TABLE job_offer
(
    id                             UUID PRIMARY KEY      DEFAULT GEN_RANDOM_UUID(),
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

    must_have_requirements         TEXT[]       NOT NULL,
    nice_to_have_requirements      TEXT[]       NOT NULL,

    valid_from                     TIMESTAMP    NOT NULL,
    valid_to                       TIMESTAMP    NOT NULL,

    offer_status                   offer_status NOT NULL DEFAULT 'DRAFT',
    vacancy                        INT          NOT NULL,

    recruitment_process_version_id UUID         NOT NULL,
    recruiter_id                   UUID         NOT NULL,
    substitute_recruiter_id        UUID,

    deleted                        BOOLEAN               DEFAULT false,
    created_at                     TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at                     TIMESTAMP    NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_contract_type FOREIGN KEY (contract_type_id) REFERENCES contract_type (id),
    CONSTRAINT fk_location FOREIGN KEY (location_id) REFERENCES location (id),
    CONSTRAINT fk_full_time_equivalent FOREIGN KEY (full_time_equivalent_id) REFERENCES full_time_equivalent (id),
    CONSTRAINT fk_work_model FOREIGN KEY (work_model_id) REFERENCES work_model (id),

    CONSTRAINT fk_recruitment_process_version FOREIGN KEY (recruitment_process_version_id)
        REFERENCES recruitment_process_version (id),

    CONSTRAINT fk_department FOREIGN KEY (department_id) REFERENCES department (id),

    CONSTRAINT fk_recruiter FOREIGN KEY (recruiter_id)
        REFERENCES app_user (id),

    CONSTRAINT fk_substitute_recruiter FOREIGN KEY (substitute_recruiter_id)
        REFERENCES app_user (id)
);

CREATE TABLE job_offer_benefit
(
    id           UUID PRIMARY KEY DEFAULT GEN_RANDOM_UUID(),
    job_offer_id UUID NOT NULL,
    benefit_id   UUID NOT NULL,

    CONSTRAINT uq_job_offer_benefit UNIQUE (job_offer_id, benefit_id),

    CONSTRAINT fk_job_offer FOREIGN KEY (job_offer_id)
        REFERENCES job_offer (id) ON DELETE CASCADE,

    CONSTRAINT fk_benefit FOREIGN KEY (benefit_id)
        REFERENCES benefit (id) ON DELETE CASCADE
);

CREATE TABLE process_step
(
    id                           UUID PRIMARY KEY     DEFAULT GEN_RANDOM_UUID(),
    process_version_id           UUID        NOT NULL,
    step_order                   INT         NOT NULL,
    name                         VARCHAR(50) NOT NULL,
    description                  VARCHAR(255),

    requires_interview           BOOLEAN              DEFAULT false,
    requires_department_approval BOOLEAN              DEFAULT false,

    deleted                      BOOLEAN              DEFAULT false,
    created_at                   TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at                   TIMESTAMP   NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_recruitment_process_version FOREIGN KEY (process_version_id)
        REFERENCES recruitment_process_version (id),

    CONSTRAINT uq_process_step_order UNIQUE (process_version_id, step_order)
);

CREATE TABLE job_application
(
    id                              UUID PRIMARY KEY                DEFAULT GEN_RANDOM_UUID(),
    public_token                    UUID UNIQUE            NOT NULL,
    job_offer_id                    UUID                   NOT NULL,
    recruitment_process_version_id  UUID                   NOT NULL,

    first_name                      VARCHAR(50)            NOT NULL,
    last_name                       VARCHAR(50)            NOT NULL,
    email                           VARCHAR(100)           NOT NULL,

    phone_number                    VARCHAR(30),
    github_link                     VARCHAR(255),

    status                          job_application_status NOT NULL DEFAULT 'SUBMITTED',

    deleted                         BOOLEAN                         DEFAULT false,
    created_at                      TIMESTAMP              NOT NULL DEFAULT NOW(),
    updated_at                      TIMESTAMP              NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_job_application_job_offer FOREIGN KEY (job_offer_id)
        REFERENCES job_offer (id) ON DELETE CASCADE,

    CONSTRAINT fk_job_application_process_version FOREIGN KEY (recruitment_process_version_id)
        REFERENCES recruitment_process_version (id)

);

CREATE TABLE job_application_step
(
    id                   UUID PRIMARY KEY                 DEFAULT GEN_RANDOM_UUID(),
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

    deleted              BOOLEAN                          DEFAULT false,
    created_at           TIMESTAMP               NOT NULL DEFAULT NOW(),
    updated_at           TIMESTAMP               NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_application FOREIGN KEY (application_id)
        REFERENCES job_application (id),

    CONSTRAINT fk_process_step FOREIGN KEY (process_step_id)
        REFERENCES process_step (id),

    CONSTRAINT fk_started_by_user FOREIGN KEY (started_by_user_id)
        REFERENCES app_user (id),

    CONSTRAINT fk_completed_by_user FOREIGN KEY (completed_by_user_id)
        REFERENCES app_user (id),

    CONSTRAINT fk_rejected_by_user FOREIGN KEY (rejected_by_user_id)
        REFERENCES app_user (id)
);

CREATE TABLE attachment
(
    id                 UUID PRIMARY KEY      DEFAULT GEN_RANDOM_UUID(),
    original_name      VARCHAR(255) NOT NULL,
    stored_name        VARCHAR(255) NOT NULL,
    path               VARCHAR(255) NOT NULL,
    is_cv              BOOLEAN      NOT NULL,

    job_application_id UUID         NOT NULL,

    deleted            BOOLEAN               DEFAULT false,
    created_at         TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMP    NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_job_application FOREIGN KEY (job_application_id)
        REFERENCES job_application (id)
);

CREATE TABLE interview
(
    id                      UUID PRIMARY KEY          DEFAULT GEN_RANDOM_UUID(),

    job_application_id      UUID             NOT NULL,
    job_application_step_id UUID             NOT NULL,
    recruiter_id            UUID             NOT NULL,

    scheduled_start         TIMESTAMP        NOT NULL,
    scheduled_end           TIMESTAMP        NOT NULL,

    status                  interview_status NOT NULL DEFAULT 'SCHEDULED',

    location                VARCHAR(255)     NOT NULL,
    meeting_url             VARCHAR(1000),
    notes                   VARCHAR(255),

    deleted                 BOOLEAN                   DEFAULT false,
    created_at              TIMESTAMP        NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP        NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_job_application FOREIGN KEY (job_application_id)
        REFERENCES job_application (id),

    CONSTRAINT fk_job_application_step FOREIGN KEY (job_application_step_id)
        REFERENCES job_application_step (id),

    CONSTRAINT fk_recruiter FOREIGN KEY (recruiter_id)
        REFERENCES app_user (id)
);


CREATE TABLE log
(
    id         UUID PRIMARY KEY      DEFAULT GEN_RANDOM_UUID(),
    message    VARCHAR(255) NOT NULL,
    trigger    VARCHAR(255) NOT NULL,
    type       VARCHAR(100) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP    NOT NULL DEFAULT NOW()
);