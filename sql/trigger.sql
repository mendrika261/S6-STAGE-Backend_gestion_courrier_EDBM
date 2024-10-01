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
AFTER INSERT OR UPDATE ON system_user
FOR EACH ROW
EXECUTE FUNCTION user_history_trigger_function();

