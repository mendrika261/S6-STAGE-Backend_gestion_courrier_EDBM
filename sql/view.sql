create view app_active
            (id, authorized_role_prefix, created_at, description, logo_url, name, removed_at, updated_at, url,
             created_by_id, removed_by_id, updated_by_id)
as
SELECT app.id,
       app.authorized_role_prefix,
       app.created_at,
       app.description,
       app.logo_url,
       app.name,
       app.removed_at,
       app.updated_at,
       app.url,
       app.created_by_id,
       app.removed_by_id,
       app.updated_by_id
FROM app
WHERE app.removed_at IS NULL;

alter table app_active
    owner to mendrika;



create view location_active
            (id, created_at, latitude, longitude, name, removed_at, updated_at, created_by_id, removed_by_id,
             updated_by_id) as
SELECT location.id,
       location.created_at,
       location.latitude,
       location.longitude,
       location.name,
       location.removed_at,
       location.updated_at,
       location.created_by_id,
       location.removed_by_id,
       location.updated_by_id
FROM location
WHERE location.removed_at IS NULL;

alter table location_active
    owner to mendrika;



create view mail_incoming
            (id, confidentiality, created_at, description, note_for_messenger, object, priority, receiver, reference,
             sender, status, created_by_id, receiver_location_id, receiver_user_id, sender_location_id, sender_user_id)
as
SELECT mail.id,
       mail.confidentiality,
       mail.created_at,
       mail.description,
       mail.note_for_messenger,
       mail.object,
       mail.priority,
       mail.receiver,
       mail.reference,
       mail.sender,
       mail.status,
       mail.created_by_id,
       mail.receiver_location_id,
       mail.receiver_user_id,
       mail.sender_location_id,
       mail.sender_user_id
FROM mail
WHERE mail.reference::text ~~ 'REC%'::text;

alter table mail_incoming
    owner to mendrika;



create view mail_outgoing
            (id, confidentiality, created_at, description, note_for_messenger, object, priority, receiver, reference,
             sender, status, created_by_id, receiver_location_id, receiver_user_id, sender_location_id, sender_user_id)
as
SELECT mail.id,
       mail.confidentiality,
       mail.created_at,
       mail.description,
       mail.note_for_messenger,
       mail.object,
       mail.priority,
       mail.receiver,
       mail.reference,
       mail.sender,
       mail.status,
       mail.created_by_id,
       mail.receiver_location_id,
       mail.receiver_user_id,
       mail.sender_location_id,
       mail.sender_user_id
FROM mail
WHERE mail.reference::text ~~ 'ENV%'::text;

alter table mail_outgoing
    owner to mendrika;



create view notification_active(id, created_at, description, done_at, title, type, url, user_id) as
SELECT notification.id,
       notification.created_at,
       notification.description,
       notification.done_at,
       notification.title,
       notification.type,
       notification.url,
       notification.user_id
FROM notification
WHERE notification.done_at IS NULL;

alter table notification_active
    owner to mendrika;



create view role_active
            (id, code, color, created_at, name, removed_at, updated_at, created_by_id, removed_by_id, updated_by_id) as
SELECT role.id,
       role.code,
       role.color,
       role.created_at,
       role.name,
       role.removed_at,
       role.updated_at,
       role.created_by_id,
       role.removed_by_id,
       role.updated_by_id
FROM role
WHERE role.removed_at IS NULL;

alter table role_active
    owner to mendrika;



create view session_active
            (id, created_at, expired_at, ip_address, last_activity_at, query_count, status, token_value, user_agent,
             user_id) as
SELECT session.id,
       session.created_at,
       session.expired_at,
       session.ip_address,
       session.last_activity_at,
       session.query_count,
       session.status,
       session.token_value,
       session.user_agent,
       session.user_id
FROM session
WHERE session.status::text = 'ACTIVE'::text;

alter table session_active
    owner to mendrika;


create view user_active
            (id, created_at, email, first_name, last_name, password, phone_number, status, created_by_id,
             location_id) as
SELECT system_user.id,
       system_user.created_at,
       system_user.email,
       system_user.first_name,
       system_user.last_name,
       system_user.password,
       system_user.phone_number,
       system_user.status,
       system_user.created_by_id,
       system_user.location_id
FROM system_user
WHERE system_user.status::text = 'ACTIVE'::text;

alter table user_active
    owner to mendrika;