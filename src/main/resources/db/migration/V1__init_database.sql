create table if not exists building
(
    id      bigserial primary key,
    name    varchar(255)
);

create table if not exists secret
(
    id      bigserial primary key,
    name    varchar(64)
);

-- CREATING USERS TABLE
create table if not exists users
(
    id          bigserial primary key,
    firstname   varchar(255),
    lastname    varchar(255),
    phone       varchar(255),
    active      boolean not null,
    role        varchar(255) constraint users_role_check check ((role)::text = ANY
        ((ARRAY ['ADMIN'::character varying, 'USER'::character varying, 'EXECUTOR'::character varying])::text[]))
    );

-- CREATING AUTH TABLE
create table if not exists auth
(
    id       bigserial primary key,
    enabled  boolean not null,
    password varchar(255),
    user_id  bigint,
    username varchar(255)
);

-- CREATING ingoing TABLE
create table if not exists ingoing
(
    id                     bigserial primary key,
    document_number          varchar(128),
    description            varchar(255),
    resolution            varchar(255),
    closed_timestamp       bigint,
    created_timestamp      bigint,
    estimated_timestamp    bigint,
    document_timestamp    bigint,
    status                 varchar(255) constraint ingoing_status_check
            check ((status)::text = ANY
                   (ARRAY [('OPENED'::character varying)::text, ('CLOSED'::character varying)::text])),
    building_id             bigint constraint fkditu6lr4ek16tkxtdsne0gxib references building,
    secret_id             bigint constraint fkgucpa7a16tdxn1x4twhy2jf9 references secret,
    copy_number int,
    copy_sheet int,
    sheet int,
    schedule int,
    user_last_updated_id bigint constraint fk61tgpffh9bl4vcry3uwsiilad references users
);

-- CREATING outgoing TABLE
create table if not exists outgoing
(
    id                     bigserial primary key,
    document_number          varchar(128),
    exemplar varchar(64),
    description            varchar(255),
    closed_timestamp       bigint,
    created_timestamp      bigint,
    estimated_timestamp    bigint,
    document_timestamp    bigint,
    sending_timestamp    bigint,
    status                 varchar(255) constraint ingoing_status_check
                    check ((status)::text = ANY
                (ARRAY [('OPENED'::character varying)::text, ('CLOSED'::character varying)::text])),
    building_id             bigint constraint fkditu6lr4ek16tkxtdsne0gxib references building,
    secret_id             bigint constraint fkgucpa7a16tdxn1x4twhy2jf9 references secret,
    executor_id             bigint constraint fkgucpa7a16tdxn1x4twho2jf9 references users,
    copy_number int,
    copy_sheet int,
    sheet int,
    schedule int,
    doc_department_index varchar(64),
    doc_copy_sheet int,
    doc_copy_print int,
    user_last_updated_id bigint constraint fk61tgpffh9bl4vcry3uwsdslad references users
);

create table ingoing_status_history
(
    id              bigserial primary key,
    ingoing_id       bigint,
    status_from     varchar(255) constraint ingoing_status_from_check check ((status_from)::text = ANY
                                                                          ((ARRAY ['OPENED'::character varying, 'CLOSED'::character varying])::text[])),
    status_to       varchar(255) constraint ingoing_status_to_check check ((status_to)::text = ANY
                                                                          ((ARRAY ['OPENED'::character varying, 'CLOSED'::character varying])::text[])),
    timestamp       bigint,
    user_id         bigint
);

create table outgoing_status_history
(
    id              bigserial primary key,
    outgoing_id       bigint,
    status_from     varchar(255) constraint ingoing_status_from_check check ((status_from)::text = ANY
        ((ARRAY ['OPENED'::character varying, 'CLOSED'::character varying])::text[])),
    status_to       varchar(255) constraint ingoing_status_to_check check ((status_to)::text = ANY
                                                                          ((ARRAY ['OPENED'::character varying, 'CLOSED'::character varying])::text[])),
    timestamp       bigint,
    user_id         bigint
);