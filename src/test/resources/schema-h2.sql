CREATE TABLE users
(
    id              VARCHAR(36)   NOT NULL PRIMARY KEY,
    username        VARCHAR(50)   NOT NULL UNIQUE,
    email           VARCHAR(50)   NOT NULL UNIQUE,
    first_name      VARCHAR(50)   NOT NULL,
    last_name       VARCHAR(100),
    password        VARCHAR(1000) NOT NULL,
    created_at      TIMESTAMP,
    updated_at      TIMESTAMP,
    is_soft_deleted BOOLEAN       NOT NULL DEFAULT FALSE
);

CREATE TABLE authentications
(
    token TEXT NOT NULL PRIMARY KEY
);

CREATE TABLE cases
(
    id                  VARCHAR(36) NOT NULL PRIMARY KEY,
    user_id             VARCHAR(36) NOT NULL REFERENCES users (id),
    title               VARCHAR(50) NOT NULL,
    description         VARCHAR(500),
    duration_in_seconds INT,
    created_at          TIMESTAMP,
    updated_at          TIMESTAMP,
    is_soft_deleted     BOOLEAN     NOT NULL DEFAULT FALSE
);

CREATE TABLE issues
(
    id         VARCHAR(36)                              NOT NULL PRIMARY KEY,
    case_id    VARCHAR(36)                              NOT NULL REFERENCES cases (id),
    type       ENUM('SINGLE_CHOICE', 'MULTIPLE_CHOICE') NOT NULL,
    body       TEXT                                     NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE choices
(
    id         VARCHAR(36) NOT NULL PRIMARY KEY,
    issue_id   VARCHAR(36) NOT NULL REFERENCES issues (id),
    body       TEXT        NOT NULL,
    is_answer  BOOLEAN,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE case_users
(
    id         VARCHAR(36) NOT NULL PRIMARY KEY,
    case_id    VARCHAR(36) NOT NULL REFERENCES cases (id),
    user_id    VARCHAR(36) NOT NULL REFERENCES users (id),
    created_at TIMESTAMP
);

CREATE UNIQUE INDEX unique_case_users_case_id_user_id ON case_users (case_id, user_id);

CREATE TABLE results
(
    id              VARCHAR(36) NOT NULL PRIMARY KEY,
    user_id         VARCHAR(36) NOT NULL REFERENCES users (id),
    case_id         VARCHAR(36) NOT NULL REFERENCES cases (id),
    session_id      VARCHAR(36) UNIQUE,
    score           INT                  DEFAULT 0,
    ends_at         TIMESTAMP   NOT NULL,
    finished_at     TIMESTAMP,
    created_at      TIMESTAMP,
    updated_at      TIMESTAMP,
    is_soft_deleted BOOLEAN     NOT NULL DEFAULT FALSE
);

CREATE TABLE result_issues
(
    id                VARCHAR(36) NOT NULL PRIMARY KEY,
    result_id         VARCHAR(36) NOT NULL REFERENCES results (id),
    issue_id          VARCHAR(36) NOT NULL REFERENCES issues (id),
    choice_id         VARCHAR(36) REFERENCES choices (id),
    is_correct        BOOLEAN     NOT NULL DEFAULT FALSE,
    correct_choice_id VARCHAR(36) REFERENCES choices (id)
);
