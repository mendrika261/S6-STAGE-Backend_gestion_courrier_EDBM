INSERT INTO public.role
(id, code, created_at, name)
VALUES
    (nextval('role_seq'), 'MAIL_ADMIN', now(), 'administrateur');

INSERT INTO public.location
(id, created_at, name, latitude, longitude)
VALUES
    (nextval('location_seq'), now(), 'Bâtiment EDBM Antaninarenina', -18.910656831424365, 47.52289447433926);


INSERT INTO public.sys_user
(id, phone_number, created_at, email, password, last_name, first_name, status, location_id)
VALUES
    ('51161df7-a5c1-41a5-87c5-c18cff313349', '0340000000', now(), 'admin@mendrika.dev', '$2a$10$dKiYSpPDnVv5x3LKbzQzvuZ704cMrTFWtcfEqLm82.Qw9rWA0MUZ6', 'Razafitiana', 'Ralainasolo', 'ACTIVE', 1);

INSERT INTO public.sys_user_roles
(user_id, roles_id)
VALUES
    ('51161df7-a5c1-41a5-87c5-c18cff313349', 1);

UPDATE public.sys_user SET created_by_id = '51161df7-a5c1-41a5-87c5-c18cff313349' WHERE id = '51161df7-a5c1-41a5-87c5-c18cff313349';
UPDATE public.role SET created_by_id = '51161df7-a5c1-41a5-87c5-c18cff313349' WHERE 1=1;
UPDATE public.location SET created_by_id = '51161df7-a5c1-41a5-87c5-c18cff313349' WHERE 1=1;


INSERT INTO public.app(id, authorized_role_prefix, created_at, description, logo_url, name, removed_at, updated_at, url, created_by_id, removed_by_id, updated_by_id)
VALUES ('51161df7-a5c1-41a5-87c5-c18cff313349', 'MAIL_', now(), 'Application de gestion de courrier', 'http://localhost:3000/logo73.png', 'Gestion de courrier', NULL, null, 'http://localhost:3000/home', '51161df7-a5c1-41a5-87c5-c18cff313349', NULL, null);
