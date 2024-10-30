INSERT INTO public.role
(id, code, created_at, name)
VALUES
    (nextval('role_seq'), 'MAIL_ADMIN', now(), 'administrateur'),
    (nextval('role_seq'), 'MAIL_USER', now(), 'utilisateur'),
    (nextval('role_seq'), 'MAIL_RECEPTIONIST', now(), 'réceptionniste'),
    (nextval('role_seq'), 'MAIL_MESSENGER', now(), 'coursier');

INSERT INTO public.location
(id, created_at, name, latitude, longitude)
VALUES
(nextval('location_seq'), now(), 'Bâtiment EDBM Antaninarenina', -18.910656831424365, 47.52289447433926),
(nextval('location_seq'), now(), 'Rez de chaussez - EDBM Antaninarenina', -18.910656831424366, 47.52289447433927),
(nextval('location_seq'), now(), '1er étage - EDBM Antaninarenina', -18.910656831424367, 47.52289447433928),
(nextval('location_seq'), now(), '2ème étage - EDBM Antaninarenina', -18.910656831424368, 47.52289447433929),
(nextval('location_seq'), now(), '3ème étage - EDBM Antaninarenina', -18.910656831424369, 47.52289447433930),
(nextval('location_seq'), now(), '4ème étage - EDBM Antaninarenina', -18.910656831424370, 47.52289447433931);

INSERT INTO public.sys_user
    (id, phone_number, created_at, email, password, last_name, first_name, status, location_id)
VALUES
    ('51161df7-a5c1-41a5-87c5-c18cff313349', '0340000000', now(), 'admin@mendrika.dev', '$2a$10$dKiYSpPDnVv5x3LKbzQzvuZ704cMrTFWtcfEqLm82.Qw9rWA0MUZ6', 'rakoto', 'admin', 'ACTIVE', 1);

INSERT INTO public.sys_user (id, created_at, email, first_name, last_name, password, phone_number, status, created_by_id, location_id) VALUES ('c1d5a683-241a-46e2-81e8-abdfb612b89b', '2024-09-13 13:45:18.325043', 'user1@mendrika.dev', 'user1', 'ratsimandrava', '$2a$10$dKiYSpPDnVv5x3LKbzQzvuZ704cMrTFWtcfEqLm82.Qw9rWA0MUZ6', '0320000000', 'ACTIVE', '51161df7-a5c1-41a5-87c5-c18cff313349', 1);
INSERT INTO public.sys_user (id, created_at, email, first_name, last_name, password, phone_number, status, created_by_id, location_id) VALUES ('81cf6c96-ba7a-4b1b-a45e-cf72acd7f994', '2024-09-13 13:51:01.396140', 'user2@mendrika.dev', 'user2', 'andriambololona', '$2a$10$dKiYSpPDnVv5x3LKbzQzvuZ704cMrTFWtcfEqLm82.Qw9rWA0MUZ6', '0320000000', 'ACTIVE', '51161df7-a5c1-41a5-87c5-c18cff313349', 1);
INSERT INTO public.sys_user (id, created_at, email, first_name, last_name, password, phone_number, status, created_by_id, location_id) VALUES ('e893ae7f-5717-4bca-88a9-390f218692a9', '2024-09-13 13:52:47.098867', 'user3@mendrika.dev', 'user3', 'ramilijaona', '$2a$10$dKiYSpPDnVv5x3LKbzQzvuZ704cMrTFWtcfEqLm82.Qw9rWA0MUZ6', '0380000000', 'ACTIVE', '51161df7-a5c1-41a5-87c5-c18cff313349', 1);
INSERT INTO public.sys_user (id, created_at, email, first_name, last_name, password, phone_number, status, created_by_id, location_id) VALUES ('fa605cab-de12-45da-8326-2749270cafb1', '2024-09-13 13:51:47.941516', 'messenger1@mendrika.dev', 'messenger1', 'rabenatoandro', '$2a$10$dKiYSpPDnVv5x3LKbzQzvuZ704cMrTFWtcfEqLm82.Qw9rWA0MUZ6', '0320000000', 'ACTIVE', '51161df7-a5c1-41a5-87c5-c18cff313349', 1);
INSERT INTO public.sys_user (id, created_at, email, first_name, last_name, password, phone_number, status, created_by_id, location_id) VALUES ('0c5b2129-3e1c-43a2-bdf1-0e90a2324529', '2024-09-13 13:44:11.370112', 'reception1@mendrika.dev', 'reception1', 'rabenandrasana', '$2a$10$dKiYSpPDnVv5x3LKbzQzvuZ704cMrTFWtcfEqLm82.Qw9rWA0MUZ6', '0320000000', 'ACTIVE', '51161df7-a5c1-41a5-87c5-c18cff313349', 1);


INSERT INTO public.sys_user_roles
(user_id, roles_id)
VALUES
    ('51161df7-a5c1-41a5-87c5-c18cff313349', 1),
    ('c1d5a683-241a-46e2-81e8-abdfb612b89b', 51),
    ('81cf6c96-ba7a-4b1b-a45e-cf72acd7f994', 51),
    ('e893ae7f-5717-4bca-88a9-390f218692a9', 51),
    ('fa605cab-de12-45da-8326-2749270cafb1', 151),
    ('0c5b2129-3e1c-43a2-bdf1-0e90a2324529', 101);

UPDATE public.sys_user SET created_by_id = '51161df7-a5c1-41a5-87c5-c18cff313349' WHERE id = '51161df7-a5c1-41a5-87c5-c18cff313349';
UPDATE public.role SET created_by_id = '51161df7-a5c1-41a5-87c5-c18cff313349' WHERE 1=1;
UPDATE public.location SET created_by_id = '51161df7-a5c1-41a5-87c5-c18cff313349' WHERE 1=1;

INSERT INTO public.app(id, authorized_role_prefix, created_at, description, logo_url, name, removed_at, updated_at, url, created_by_id, removed_by_id, updated_by_id)
VALUES ('51161df7-a5c1-41a5-87c5-c18cff313349', 'MAIL_', now(), 'Application de gestion de courrier', 'http://localhost:3000/logo73.png', 'Gestion de courrier', NULL, null, 'http://localhost:3000', '51161df7-a5c1-41a5-87c5-c18cff313349', NULL, null);