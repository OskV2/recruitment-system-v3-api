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
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
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

INSERT INTO recruitment_process_version(id,
                                        recruitment_process_id,
                                        version,
                                        active)
VALUES ('70000000-0000-0000-0000-000000000001',
        '60000000-0000-0000-0000-000000000001',
        '70000000-0000-0000-0000-000000000999',
        TRUE);

INSERT INTO process_step(id,
                         process_version_id,
                         step_order,
                         name,
                         requires_interview)
VALUES ('80000000-0000-0000-0000-000000000001',
        '70000000-0000-0000-0000-000000000001',
        1,
        'CV Review',
        FALSE),
       ('80000000-0000-0000-0000-000000000002',
        '70000000-0000-0000-0000-000000000001',
        2,
        'HR Interview',
        TRUE),
       ('80000000-0000-0000-0000-000000000003',
        '70000000-0000-0000-0000-000000000001',
        3,
        'Technical Interview',
        TRUE);


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