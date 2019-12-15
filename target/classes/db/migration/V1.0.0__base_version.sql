create table organization
(
    organization_id BIGINT NOT NULL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL
);

create table post (
    post_id BIGINT NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    organization_id BIGINT NOT NULL,
    subject VARCHAR(512) NOT NULL,
    body text NOT NULL
--     FOREIGN KEY (organization_id) REFERENCES organization (organization_id)
);

CREATE INDEX post_organization_id_idx ON post (organization_id);
