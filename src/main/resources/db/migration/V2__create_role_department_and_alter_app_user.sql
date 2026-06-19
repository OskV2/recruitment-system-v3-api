CREATE TABLE role (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(1000),

    can_manage_job_applications BOOLEAN NOT NULL DEFAULT FALSE,

    can_add_new_offer BOOLEAN NOT NULL DEFAULT FALSE,
    can_edit_existing_offer BOOLEAN NOT NULL DEFAULT FALSE,
    can_view_all_offers BOOLEAN NOT NULL DEFAULT FALSE,

    can_manage_users BOOLEAN NOT NULL DEFAULT FALSE,
    can_manage_roles BOOLEAN NOT NULL DEFAULT FALSE,

    can_view_logs BOOLEAN NOT NULL DEFAULT FALSE,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE department (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(1000),
    deleted BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE app_user
    ADD COLUMN role_id UUID NOT NULL,
    ADD COLUMN department_id UUID NOT NULL,
    ADD COLUMN updated_at TIMESTAMP NOT NULL;

ALTER TABLE app_user
    ADD CONSTRAINT fk_app_user_role
        FOREIGN KEY (role_id)
        REFERENCES role(id);

ALTER TABLE app_user
    ADD CONSTRAINT fk_app_user_department
        FOREIGN KEY (department_id)
        REFERENCES department(id);