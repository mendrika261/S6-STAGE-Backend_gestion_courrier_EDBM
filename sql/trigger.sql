CREATE OR REPLACE FUNCTION mail_history_trigger_function()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO mail_history (id, created_at, description, mail_confidentiality, note_for_messenger, object, priority,
                              receiver, reference, sender, status, created_by_id, mail_id, receiver_location_id, sender_location_id)
    VALUES (nextval('mail_history_seq'), now(), NEW.description, NEW.confidentiality, NEW.note_for_messenger,
            NEW.object, NEW.priority, NEW.receiver, NEW.reference, NEW.sender, NEW.status, NEW.created_by_id, NEW.id, NEW.receiver_location_id,
            NEW.sender_location_id);

    RETURN NEW;
END;
$$;

CREATE OR REPLACE TRIGGER mail_history_trigger
    AFTER INSERT OR UPDATE ON mail
    FOR EACH ROW
EXECUTE FUNCTION mail_history_trigger_function();





CREATE OR REPLACE FUNCTION user_history_trigger_function()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO user_history (id, created_at, email, first_name, last_name, password, phone_number, status, created_by_id, user_id)
    VALUES (nextval('user_history_seq'), now(), NEW.email, NEW.first_name, NEW.last_name, NEW.password, NEW.phone_number, NEW.status, NEW.created_by_id, NEW.id);

    RETURN NEW;
END;
$$;

CREATE OR REPLACE TRIGGER user_history_trigger
AFTER INSERT OR UPDATE ON sys_user
FOR EACH ROW
EXECUTE FUNCTION user_history_trigger_function();
