-- ROLES

INSERT INTO role (id,
                  name,
                  description,
                  can_manage_job_applications,
                  can_add_new_offer,
                  can_edit_existing_offer,
                  can_view_all_offers,
                  can_manage_users,
                  can_manage_roles,
                  can_view_logs)
VALUES ('11111111-1111-1111-1111-111111111111',
        'ADMIN',
        'System administrator',
        TRUE,
        TRUE,
        TRUE,
        TRUE,
        TRUE,
        TRUE,
        TRUE),
       ('22222222-2222-2222-2222-222222222222',
        'RECRUITER',
        'Recruitment specialist',
        TRUE,
        TRUE,
        TRUE,
        TRUE,
        FALSE,
        FALSE,
        FALSE),
       ('33333333-3333-3333-3333-333333333333',
        'HIRING_MANAGER',
        'Department manager',
        TRUE,
        FALSE,
        FALSE,
        TRUE,
        FALSE,
        FALSE,
        FALSE);

-- DEPARTMENTS

INSERT INTO department (id,
                        name,
                        description)
VALUES ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
        'IT',
        'Software Development Department'),
       ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
        'HR',
        'Human Resources'),
       ('cccccccc-cccc-cccc-cccc-cccccccccccc',
        'Finance',
        'Finance Department');

-- USERS

INSERT INTO app_user (id,
                      first_name,
                      last_name,
                      email,
                      password,
                      description,
                      locked,
                      role_id,
                      department_id,
                      created_at,
                      updated_at)
VALUES ('d1111111-1111-1111-1111-111111111111',
        'Admin',
        'User',
        'admin@test.com',
        '$2a$10$E6oV69g59b2cO2iNNE.uzeXfUtol/SGaRwDnqoIfSbERC7U34iYo.',  --  root
        'System administrator account',
        FALSE,
        '11111111-1111-1111-1111-111111111111',
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
        NOW(),
        NOW()),
       ('d2222222-2222-2222-2222-222222222222',
        'John',
        'Recruiter',
        'recruiter@test.com',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'Main recruiter',
        FALSE,
        '22222222-2222-2222-2222-222222222222',
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
        NOW(),
        NOW()),
       ('d3333333-3333-3333-3333-333333333333',
        'Alice',
        'Manager',
        'manager@test.com',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'Hiring manager',
        FALSE,
        '33333333-3333-3333-3333-333333333333',
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
        NOW(),
        NOW());


-- CONTRACT TYPES

INSERT INTO contract_type(id, name, description)
VALUES ('10000000-0000-0000-0000-000000000001', 'B2B', 'Business to Business'),
       ('10000000-0000-0000-0000-000000000002', 'UOP', 'Employment contract');

-- LOCATIONS

INSERT INTO location(id, city, country)
VALUES ('20000000-0000-0000-0000-000000000001', 'Warsaw', 'Poland'),
       ('20000000-0000-0000-0000-000000000002', 'Krakow', 'Poland');

-- FTE

INSERT INTO full_time_equivalent(id, name)
VALUES ('30000000-0000-0000-0000-000000000001', '1.0'),
       ('30000000-0000-0000-0000-000000000002', '0.5');

-- WORK MODEL

INSERT INTO work_model(id, name)
VALUES ('40000000-0000-0000-0000-000000000001', 'Remote'),
       ('40000000-0000-0000-0000-000000000002', 'Hybrid'),
       ('40000000-0000-0000-0000-000000000003', 'Office');

-- BENEFITS

INSERT INTO benefit(id, name)
VALUES ('50000000-0000-0000-0000-000000000001', 'Private Healthcare'),
       ('50000000-0000-0000-0000-000000000002', 'Multisport'),
       ('50000000-0000-0000-0000-000000000003', 'Training Budget');

INSERT INTO recruitment_process(id,
                                name,
                                description)
VALUES ('60000000-0000-0000-0000-000000000001',
        'Standard IT Recruitment',
        'Default recruitment process');

INSERT INTO recruitment_process_version(
    id,
    recruitment_process_id,
    version,
    active
)
VALUES (
           '70000000-0000-0000-0000-000000000001',
           '60000000-0000-0000-0000-000000000001',
           1,
           TRUE
       );

INSERT INTO process_step(
    id,
    name,
    requires_interview,
    requires_department_approval
)
VALUES
    (
        '80000000-0000-0000-0000-000000000001',
        'CV Review',
        FALSE,
        FALSE
    ),
    (
        '80000000-0000-0000-0000-000000000002',
        'HR Interview',
        TRUE,
        FALSE
    ),
    (
        '80000000-0000-0000-0000-000000000003',
        'Technical Interview',
        TRUE,
        TRUE
    );

INSERT INTO process_version_step(
    recruitment_process_version_id,
    process_step_id,
    step_order
)
VALUES
    (
        '70000000-0000-0000-0000-000000000001',
        '80000000-0000-0000-0000-000000000001',
        1
    ),
    (
        '70000000-0000-0000-0000-000000000001',
        '80000000-0000-0000-0000-000000000002',
        2
    ),
    (
        '70000000-0000-0000-0000-000000000001',
        '80000000-0000-0000-0000-000000000003',
        3
    );

INSERT INTO job_offer(id,
                      name,
                      description,
                      salary_from,
                      salary_to,
                      currency,
                      contract_type_id,
                      location_id,
                      full_time_equivalent_id,
                      work_model_id,
                      department_id,
                      must_have_requirements,
                      nice_to_have_requirements,
                      valid_from,
                      valid_to,
                      offer_status,
                      vacancy,
                      recruitment_process_version_id,
                      recruiter_id,
                      substitute_recruiter_id)
VALUES ('90000000-0000-0000-0000-000000000001',
        'Senior Java Developer',
        'Spring Boot developer',
        18000,
        25000,
        'PLN',
        '10000000-0000-0000-0000-000000000001',
        '20000000-0000-0000-0000-000000000001',
        '30000000-0000-0000-0000-000000000001',
        '40000000-0000-0000-0000-000000000002',
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
        ARRAY['Java'],
        ARRAY['Spring Boot'],
        NOW(),
        NOW() + INTERVAL '30 days',
        'ACTIVE',
        3,
        '70000000-0000-0000-0000-000000000001',
        'd2222222-2222-2222-2222-222222222222',
        'd2222222-2222-2222-2222-222222222222');


INSERT INTO job_offer_benefit(job_offer_id, benefit_id)
VALUES ('90000000-0000-0000-0000-000000000001',
        '50000000-0000-0000-0000-000000000001'),
       ('90000000-0000-0000-0000-000000000001',
        '50000000-0000-0000-0000-000000000002');

INSERT INTO job_application(id,
                            public_token,
                            job_offer_id,
                            recruitment_process_version_id,
                            first_name,
                            last_name,
                            email,
                            phone_number,
                            github_link,
                            status)
VALUES ('a0000000-0000-0000-0000-000000000001',
        'a0000000-0000-0000-0000-000000000999',
        '90000000-0000-0000-0000-000000000001',
        '70000000-0000-0000-0000-000000000001',
        'Jan',
        'Kowalski',
        'jan.kowalski@test.com',
        '+48123123123',
        'https://github.com/jankowalski',
        'IN_PROGRESS');


INSERT INTO job_application_step(id,
                                 application_id,
                                 process_step_id,
                                 step_order,
                                 status,
                                 started_at,
                                 started_by_user_id)
VALUES ('b0000000-0000-0000-0000-000000000001',
        'a0000000-0000-0000-0000-000000000001',
        '80000000-0000-0000-0000-000000000001',
        1,
        'CURRENT',
        NOW(),
        'd2222222-2222-2222-2222-222222222222');

INSERT INTO interview(id,
                      job_application_id,
                      job_application_step_id,
                      recruiter_id,
                      scheduled_start,
                      scheduled_end,
                      location,
                      meeting_url)
VALUES ('c0000000-0000-0000-0000-000000000001',
        'a0000000-0000-0000-0000-000000000001',
        'b0000000-0000-0000-0000-000000000001',
        'd2222222-2222-2222-2222-222222222222',
        NOW() + INTERVAL '2 day',
        NOW() + INTERVAL '2 day' + INTERVAL '1 hour',
        'Google Meet',
        'https://meet.google.com/test');


-- DODATKOWY REKRUTER (do testowania getAssignedInterviews per recruiter)

INSERT INTO app_user (id,
                      first_name,
                      last_name,
                      email,
                      password,
                      description,
                      locked,
                      role_id,
                      department_id,
                      created_at,
                      updated_at)
VALUES ('d4444444-4444-4444-4444-444444444444',
        'Ewa',
        'Nowak',
        'ewa.recruiter@test.com',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'Second recruiter',
        FALSE,
        '22222222-2222-2222-2222-222222222222',
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
        NOW(),
        NOW());

-- DODATKOWE APLIKACJE (na tę samą ofertę co istniejąca)

INSERT INTO job_application(id,
                            public_token,
                            job_offer_id,
                            recruitment_process_version_id,
                            first_name,
                            last_name,
                            email,
                            phone_number,
                            github_link,
                            status)
VALUES
    -- Anna: w trakcie HR Interview (CURRENT, ma już zaplanowany interview -> do testów GET/PATCH/DELETE/RESTORE)
    ('a0000000-0000-0000-0000-000000000002',
     'a0000000-0000-0000-0000-000000000998',
     '90000000-0000-0000-0000-000000000001',
     '70000000-0000-0000-0000-000000000001',
     'Anna', 'Kowalska', 'anna.kowalska@test.com',
     '+48111222333', 'https://github.com/annakowalska', 'IN_PROGRESS'),

    -- Piotr: krok HR Interview w stanie WAITING, BEZ interview -> do testów createInterview (POST)
    ('a0000000-0000-0000-0000-000000000003',
     'a0000000-0000-0000-0000-000000000997',
     '90000000-0000-0000-0000-000000000001',
     '70000000-0000-0000-0000-000000000001',
     'Piotr', 'Zieliński', 'piotr.zielinski@test.com',
     '+48222333444', 'https://github.com/piotrzielinski', 'IN_PROGRESS'),

    -- Marta: dopiero CV Review (krok nie wymaga interview) -> ciekawy edge case
    ('a0000000-0000-0000-0000-000000000004',
     'a0000000-0000-0000-0000-000000000996',
     '90000000-0000-0000-0000-000000000001',
     '70000000-0000-0000-0000-000000000001',
     'Marta', 'Wiśniewska', 'marta.wisniewska@test.com',
     '+48333444555', 'https://github.com/martawisniewska', 'SUBMITTED'),

    -- Tomasz: odrzucony na HR Interview, interview COMPLETED -> do testów statusów/filtrowania
    ('a0000000-0000-0000-0000-000000000005',
     'a0000000-0000-0000-0000-000000000995',
     '90000000-0000-0000-0000-000000000001',
     '70000000-0000-0000-0000-000000000001',
     'Tomasz', 'Nowak', 'tomasz.nowak@test.com',
     '+48444555666', 'https://github.com/tomasznowak', 'REJECTED'),

    -- Kasia: interview CANCELLED i USUNIĘTY (deleted=TRUE) -> do testów restoreInterview
    ('a0000000-0000-0000-0000-000000000006',
     'a0000000-0000-0000-0000-000000000994',
     '90000000-0000-0000-0000-000000000001',
     '70000000-0000-0000-0000-000000000001',
     'Kasia', 'Lewandowska', 'kasia.lewandowska@test.com',
     '+48555666777', 'https://github.com/kasialewandowska', 'IN_PROGRESS');

-- KROKI APLIKACJI

INSERT INTO job_application_step(id,
                                 application_id,
                                 process_step_id,
                                 step_order,
                                 status,
                                 started_at,
                                 started_by_user_id,
                                 completed_at,
                                 completed_by_user_id,
                                 rejected_at,
                                 rejected_by_user_id,
                                 decision_comment,
                                 rejection_reason)
VALUES
    -- Anna: CV Review COMPLETED
    ('b0000000-0000-0000-0000-000000000002',
     'a0000000-0000-0000-0000-000000000002',
     '80000000-0000-0000-0000-000000000001',
     1, 'COMPLETED',
     NOW() - INTERVAL '3 day', 'd2222222-2222-2222-2222-222222222222',
     NOW() - INTERVAL '2 day', 'd2222222-2222-2222-2222-222222222222',
     NULL, NULL, 'Good CV, solid experience', NULL),

    -- Anna: HR Interview CURRENT (będzie miała interview poniżej)
    ('b0000000-0000-0000-0000-000000000003',
     'a0000000-0000-0000-0000-000000000002',
     '80000000-0000-0000-0000-000000000002',
     2, 'CURRENT',
     NOW() - INTERVAL '1 day', 'd2222222-2222-2222-2222-222222222222',
     NULL, NULL, NULL, NULL, NULL, NULL),

    -- Piotr: CV Review COMPLETED
    ('b0000000-0000-0000-0000-000000000004',
     'a0000000-0000-0000-0000-000000000003',
     '80000000-0000-0000-0000-000000000001',
     1, 'COMPLETED',
     NOW() - INTERVAL '4 day', 'd2222222-2222-2222-2222-222222222222',
     NOW() - INTERVAL '3 day', 'd2222222-2222-2222-2222-222222222222',
     NULL, NULL, 'Meets requirements', NULL),

    -- Piotr: HR Interview WAITING, brak interview -> użyj do POST createInterview
    ('b0000000-0000-0000-0000-000000000005',
     'a0000000-0000-0000-0000-000000000003',
     '80000000-0000-0000-0000-000000000002',
     2, 'WAITING',
     NOW() - INTERVAL '3 day', 'd2222222-2222-2222-2222-222222222222',
     NULL, NULL, NULL, NULL, NULL, NULL),

    -- Marta: CV Review CURRENT (krok nie wymaga interview)
    ('b0000000-0000-0000-0000-000000000006',
     'a0000000-0000-0000-0000-000000000004',
     '80000000-0000-0000-0000-000000000001',
     1, 'CURRENT',
     NOW(), 'd4444444-4444-4444-4444-444444444444',
     NULL, NULL, NULL, NULL, NULL, NULL),

    -- Tomasz: CV Review COMPLETED
    ('b0000000-0000-0000-0000-000000000007',
     'a0000000-0000-0000-0000-000000000005',
     '80000000-0000-0000-0000-000000000001',
     1, 'COMPLETED',
     NOW() - INTERVAL '10 day', 'd2222222-2222-2222-2222-222222222222',
     NOW() - INTERVAL '9 day', 'd2222222-2222-2222-2222-222222222222',
     NULL, NULL, 'Ok candidate', NULL),

    -- Tomasz: HR Interview REJECTED (miał interview, poniżej COMPLETED)
    ('b0000000-0000-0000-0000-000000000008',
     'a0000000-0000-0000-0000-000000000005',
     '80000000-0000-0000-0000-000000000002',
     2, 'REJECTED',
     NOW() - INTERVAL '8 day', 'd4444444-4444-4444-4444-444444444444',
     NULL, NULL,
     NOW() - INTERVAL '6 day', 'd4444444-4444-4444-4444-444444444444',
     NULL, 'Insufficient communication skills'),

    -- Kasia: HR Interview CURRENT (interview do niej będzie CANCELLED + deleted)
    ('b0000000-0000-0000-0000-000000000009',
     'a0000000-0000-0000-0000-000000000006',
     '80000000-0000-0000-0000-000000000002',
     2, 'CURRENT',
     NOW() - INTERVAL '1 day', 'd4444444-4444-4444-4444-444444444444',
     NULL, NULL, NULL, NULL, NULL, NULL);

-- INTERVIEWS

INSERT INTO interview(id,
                      job_application_id,
                      job_application_step_id,
                      recruiter_id,
                      scheduled_start,
                      scheduled_end,
                      status,
                      location,
                      meeting_url,
                      notes,
                      deleted)
VALUES
    -- Anna: SCHEDULED, recruiter d2222222 (do testów GET/PATCH/DELETE)
    ('c0000000-0000-0000-0000-000000000002',
     'a0000000-0000-0000-0000-000000000002',
     'b0000000-0000-0000-0000-000000000003',
     'd2222222-2222-2222-2222-222222222222',
     NOW() + INTERVAL '3 day',
     NOW() + INTERVAL '3 day' + INTERVAL '45 minute',
     'SCHEDULED',
     'Google Meet',
     'https://meet.google.com/anna-hr',
     NULL,
     FALSE),

    -- Tomasz: COMPLETED, recruiter d4444444, w przeszłości (do testów filtrowania po statusie)
    ('c0000000-0000-0000-0000-000000000003',
     'a0000000-0000-0000-0000-000000000005',
     'b0000000-0000-0000-0000-000000000008',
     'd4444444-4444-4444-4444-444444444444',
     NOW() - INTERVAL '7 day',
     NOW() - INTERVAL '7 day' + INTERVAL '1 hour',
     'COMPLETED',
     'Office - Warsaw',
     NULL,
     'Candidate struggled with basic questions',
     FALSE),

    -- Kasia: CANCELLED i USUNIĘTY -> do testów restoreInterview
    ('c0000000-0000-0000-0000-000000000004',
     'a0000000-0000-0000-0000-000000000006',
     'b0000000-0000-0000-0000-000000000009',
     'd4444444-4444-4444-4444-444444444444',
     NOW() + INTERVAL '1 day',
     NOW() + INTERVAL '1 day' + INTERVAL '30 minute',
     'CANCELLED',
     'Google Meet',
     'https://meet.google.com/kasia-hr',
     'Candidate asked to reschedule, then withdrew',
     TRUE);