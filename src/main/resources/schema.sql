create table users
(
    id              uuid          not null primary key,
    username        varchar(50)   not null unique,
    email           varchar(50)   not null unique,
    first_name      varchar(50)   not null,
    last_name       varchar(100),
    password        varchar(1000) not null,
    created_at      timestamptz,
    updated_at      timestamptz,
    is_soft_deleted boolean       not null default false
);

create table authentications
(
    token text not null primary key
);

create table cases
(
    id                  uuid        not null primary key,
    user_id             uuid        not null references users (id),
    title               varchar(50) not null,
    description         varchar(500),
    duration_in_seconds int,
    created_at          timestamptz,
    updated_at          timestamptz,
    is_soft_deleted     boolean     not null default false
);

create type issuetype as enum ('SINGLE_CHOICE', 'MULTIPLE_CHOICE');

create table issues
(
    id         uuid      not null primary key,
    case_id    uuid      not null references cases (id),
    type       issuetype not null,
    body       text      not null,
    created_at timestamptz,
    updated_at timestamptz
);

create table choices
(
    id         uuid not null primary key,
    issue_id   uuid not null references issues (id),
    body       text not null,
    is_answer  boolean,
    created_at timestamptz,
    updated_at timestamptz
);

create table case_users
(
    id         uuid not null primary key,
    case_id    uuid not null references cases (id),
    user_id    uuid not null references users (id),
    created_at timestamptz
);

create unique index unique_case_users_case_id_user_id on case_users (case_id, user_id);

create table results
(
    id              uuid        not null primary key,
    user_id         uuid        not null references users (id),
    case_id         uuid        not null references cases (id),
    session_id      uuid unique,
    score           int                  default 0,
    ends_at         timestamptz not null,
    finished_at     timestamptz,
    created_at      timestamptz,
    updated_at      timestamptz,
    is_soft_deleted boolean     not null default false
);

create table result_issues
(
    id                uuid    not null primary key,
    result_id         uuid    not null references results (id),
    issue_id          uuid    not null references issues (id),
    choice_id         uuid references choices (id),
    is_correct        boolean not null default false,
    correct_choice_id uuid references choices (id)
);
