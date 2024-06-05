CREATE TABLE IF NOT EXISTS users
(
    id              UUID          NOT NULL PRIMARY KEY,
    username        VARCHAR(50)   NOT NULL UNIQUE,
    email           VARCHAR(50)   NOT NULL UNIQUE,
    first_name      VARCHAR(50)   NOT NULL,
    last_name       VARCHAR(100),
    password        VARCHAR(1000) NOT NULL,
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ,
    is_soft_deleted BOOLEAN       NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS authentications
(
    token TEXT NOT NULL PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS cases
(
    id                  UUID        NOT NULL PRIMARY KEY,
    user_id             UUID        NOT NULL REFERENCES users (id),
    title               VARCHAR(50) NOT NULL,
    description         VARCHAR(500),
    duration_in_seconds INT,
    created_at          TIMESTAMPTZ,
    updated_at          TIMESTAMPTZ,
    is_soft_deleted     BOOLEAN     NOT NULL DEFAULT FALSE
);

CREATE TYPE ISSUETYPE AS ENUM ('SINGLE_CHOICE', 'MULTIPLE_CHOICE');

CREATE TABLE IF NOT EXISTS issues
(
    id         UUID      NOT NULL PRIMARY KEY,
    case_id    UUID      NOT NULL REFERENCES cases (id),
    type       ISSUETYPE NOT NULL,
    body       TEXT      NOT NULL,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS choices
(
    id         UUID NOT NULL PRIMARY KEY,
    issue_id   UUID NOT NULL REFERENCES issues (id),
    body       TEXT NOT NULL,
    is_answer  BOOLEAN,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS case_users
(
    id         UUID NOT NULL PRIMARY KEY,
    case_id    UUID NOT NULL REFERENCES cases (id),
    user_id    UUID NOT NULL REFERENCES users (id),
    created_at TIMESTAMPTZ
);

CREATE UNIQUE INDEX IF NOT EXISTS unique_case_users_case_id_user_id ON case_users (case_id, user_id);

CREATE TABLE IF NOT EXISTS results
(
    id              UUID        NOT NULL PRIMARY KEY,
    user_id         UUID        NOT NULL REFERENCES users (id),
    case_id         UUID        NOT NULL REFERENCES cases (id),
    session_id      UUID UNIQUE,
    score           INT                  DEFAULT 0,
    ends_at         TIMESTAMPTZ NOT NULL,
    finished_at     TIMESTAMPTZ,
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ,
    is_soft_deleted BOOLEAN     NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS result_issues
(
    id                UUID    NOT NULL PRIMARY KEY,
    result_id         UUID    NOT NULL REFERENCES results (id),
    issue_id          UUID    NOT NULL REFERENCES issues (id),
    choice_id         UUID REFERENCES choices (id),
    is_correct        BOOLEAN NOT NULL DEFAULT FALSE,
    correct_choice_id UUID REFERENCES choices (id)
);
