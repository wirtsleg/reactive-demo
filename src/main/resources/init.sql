CREATE TABLE post
(
    post_id         BIGINT       NOT NULL PRIMARY KEY,
    user_id         BIGINT       NOT NULL,
    organization_id BIGINT       NOT NULL,
    subject         VARCHAR(512) NOT NULL,
    body            text         NOT NULL
);

CREATE INDEX post_organization_id_idx ON post (organization_id);


CREATE OR REPLACE FUNCTION notify_event() RETURNS TRIGGER AS
$$
DECLARE
    payload JSON;
BEGIN
    payload = row_to_json(NEW);
    PERFORM pg_notify('post_notification', payload::text);
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;;

DROP TRIGGER IF EXISTS notify_login_event ON post;

CREATE TRIGGER notify_login_event
    AFTER INSERT OR UPDATE OR DELETE
    ON post
    FOR EACH ROW
EXECUTE PROCEDURE notify_event();;
